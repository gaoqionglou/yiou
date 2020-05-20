package com.design.fragment;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.design.model.Moment;
import com.design.model.OldPic;

import java.util.List;

public class MyViewPagerAdapter extends FragmentPagerAdapter {

    private FragmentManager fm;
    private List<Fragment> fragments;

    public MyViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        this(fm);
        this.fm = fm;
        this.fragments = fragments;
    }

    public MyViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        fm.beginTransaction().show(fragment).commit();
        return fragment;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Fragment fragment = getItem(position);
        fm.beginTransaction().hide(fragment).commit();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "照片广场";
        } else {
            return "活动";
        }

    }



}
