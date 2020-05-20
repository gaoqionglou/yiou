import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import androidx.viewpager.widget.PagerAdapter;

public class MyPagerAdapter extends PagerAdapter {

    public static final int MAX_SCROLL_VALUE = 10000;

    private List<ImageView> mImageViewList;

    public MyPagerAdapter(List<ImageView> imageViewList) {
        mImageViewList = imageViewList;
    }

    /**
     * @param container
     * @param position
     * @return 对position进行求模操作
     * 因为当用户向左滑时position可能出现负值，所以必须进行处理
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mImageViewList.get(position);
        container.addView(view);
        return view;
    }
    /**
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mImageViewList != null ? mImageViewList.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (View) object;
    }
}

