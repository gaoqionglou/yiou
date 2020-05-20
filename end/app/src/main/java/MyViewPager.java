import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;

import com.design.firstPageActivity;

import androidx.viewpager.widget.ViewPager;

public class MyViewPager extends ViewPager {

    private firstPageActivity mActivity;
    private int mStartX;
    private int mStartY;
    private int[] titles;

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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = (int) ev.getX();
                mStartY = (int) ev.getY();
                //停止轮播图的切换
                mActivity.stopSwitch();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) ev.getX();
                int upY = (int) ev.getY();
                if (mStartX == upX && mStartY == upY) {
                    int currentItem = getCurrentItem();
                    Toast.makeText(mActivity, titles[currentItem-1], Toast.LENGTH_SHORT).show();
                }
                //恢复轮播图的切换
                mActivity.startSwitch();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
