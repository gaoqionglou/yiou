package com.design.web;

import android.webkit.URLUtil;
import android.webkit.WebView;

public class Router {


    private static void loadWebPage(WebView webView, String url) {
        webView.loadUrl(url);
    }

    public static Boolean loadPage(WebView webView, String url) {
        if (URLUtil.isNetworkUrl(url) || URLUtil.isAssetUrl(url)) {
            loadWebPage(webView, url);
        } else {
            loadLocalPage(webView, url);
        }
        return true;
    }


    private static void loadLocalPage(WebView webView, String url) {
        loadWebPage(webView, "file:///android_asset/$url");
    }


}
