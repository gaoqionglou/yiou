package com.design.web;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;


import com.design.base.BaseActivity;
import com.design.databinding.ActivityH5Binding;
import com.design.databinding.H5ActionBarLayoutBinding;

import static android.view.View.GONE;

public class H5Activity extends BaseActivity {

    private String mUrl = "";
    private String mTitle = "";

    private ActivityH5Binding activityH5Binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityH5Binding = ActivityH5Binding.inflate(LayoutInflater.from(this));

        setContentView(activityH5Binding.getRoot());


        initWebView();

        mUrl = getIntent().getStringExtra("url");
        mTitle = getIntent().getStringExtra("title");


        Router.loadPage(activityH5Binding.webView, mUrl);
    }

    @Override
    public void setCustomActionBar() {
        H5ActionBarLayoutBinding actionBarViewBinding = H5ActionBarLayoutBinding.inflate(LayoutInflater.from(this));
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        View mActionBarView = actionBarViewBinding.getRoot();
        actionBarViewBinding.arrowBack.setVisibility(View.VISIBLE);
        actionBarViewBinding.arrowBack.setOnClickListener(v -> {
            this.finish();
        });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {

            return;
        }
        actionBar.setCustomView(mActionBarView, lp);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBarViewBinding.title.setText(getIntent().getStringExtra("title"));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initWebView() {
        new WebViewInitializer().setUpWebView(activityH5Binding.webView);
        activityH5Binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                view.loadUrl();
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        activityH5Binding.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    activityH5Binding.progressBar.setVisibility(GONE);
                } else {
                    if (activityH5Binding.progressBar.getVisibility() == GONE)
                        activityH5Binding.progressBar.setVisibility(View.VISIBLE);
                    activityH5Binding.progressBar.setProgress(newProgress);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityH5Binding.webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityH5Binding.webView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityH5Binding.webView.removeAllViews();
        activityH5Binding.webView.destroy();
    }

}
