package com.design.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.design.NavigationActivity;
import com.design.R;
import com.design.databinding.FragmentViewBinding;
import com.design.databinding.HomeActionBarLayoutBinding;
import com.design.model.Moment;
import com.design.model.OldPic;
import com.design.model._User;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class ViewFragment extends Fragment {
    public static RequestOptions OPTIONS = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.meng)
            .error(R.drawable.meng).dontAnimate();
    private FragmentViewBinding viewBinding;
    private MyViewPagerAdapter mAdapter;
    private HomeActionBarLayoutBinding actionBarLayoutBinding;
    private MyViewModel myViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinding = FragmentViewBinding.inflate(inflater);
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        NavigationActivity activity = (NavigationActivity) getActivity();
        actionBarLayoutBinding = activity.setHomeFragmentActionBar();
        actionBarLayoutBinding.title.setText("老照片");
        _User user = BmobUser.getCurrentUser(_User.class);
        if (user != null) {
            Glide.with(this).load(user.getAvatar()).apply(OPTIONS).into(actionBarLayoutBinding.ivHead);
        }
        viewBinding.tab.setTabMode(TabLayout.MODE_FIXED);
        viewBinding.tab.setupWithViewPager(viewBinding.viewPager);
        FragmentOldPic oldPic = FragmentOldPic.newInstance();
        FragmentActivity fragmentActivity = FragmentActivity.newInstance();
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(oldPic);
        fragments.add(fragmentActivity);
        mAdapter = new MyViewPagerAdapter(getChildFragmentManager(), fragments);
        viewBinding.viewPager.setAdapter(mAdapter);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


}
