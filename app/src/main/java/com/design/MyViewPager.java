package com.design;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.design.fragment.HomeFragment;

import static com.design.utils.ToastUtil.toast;

public class MyViewPager extends ViewPager {

    private firstPageActivity mActivity;
    private int mStartX;
    private int mStartY;
    private int[] titles;
    private HomeFragment mFragment;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setActivity(firstPageActivity firstPageActivity, int[] titles) {
        mActivity = firstPageActivity;
        this.titles = titles;
    }

    public void setFragment(HomeFragment fragment, int[] titles) {
        mFragment = fragment;
        this.titles = titles;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = (int) ev.getX();
                mStartY = (int) ev.getY();
                //停止轮播图的切换
                if (mActivity != null) {
                    mActivity.stopSwitch();
                } else if (mFragment != null) {
                    mFragment.stopSwitch();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) ev.getX();
                int upY = (int) ev.getY();
                if (mStartX == upX && mStartY == upY) {
                    int currentItem = getCurrentItem();

                    toast(titles[currentItem - 1] + "");
                }
                //恢复轮播图的切换
                if (mActivity != null) {
                    mActivity.startSwitch();
                } else if (mFragment != null) {
                    mFragment.startSwitch();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
