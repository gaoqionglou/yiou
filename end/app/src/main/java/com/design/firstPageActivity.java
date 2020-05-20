package com.design;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lljjcoder.citypickerview.widget.CityPicker;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class firstPageActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {


    private TextView tv_page_title;//标题
    private ImageButton tv_head;
    private ImageButton point;
    private ImageButton ib_location;
    private TextView locationText;
    private Button select;


    //选择城市
    private CityPicker cityPicker;
    private TextView tv_location;
    //实现定位
    private LocationManager locationManager;
    private GpsStatus.Listener mylistener;

    //轮播图
    private static final String TAG = "MainActivity";//logt快捷键自动生成
    private int[] images = {R.mipmap.pic0, R.mipmap.pic1, R.mipmap.pic2};//数据源
    private List<ImageView> mImageViewList = new ArrayList<>();//存放ImageView对象的集合
    private int[] titles = {R.string.t01, R.string.t02, R.string.t03};//标题
    private Handler mHandler = new Handler();//轮播图的自动切换
    private boolean isSwitch;//判断是否在切换
    private MyViewPager mViewPager;//轮播图
    private LinearLayout mLayout;//指示器的容器
    private TextView mTitle;//标题


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        init();//自定义标题栏
        getSupportActionBar().hide();//隐藏标题栏
        //buttom_barActivity buttom_barActivity=new buttom_barActivity();
        //buttom_barActivity.onCreate();
        initView();//获取选择城市
        // 实现定位
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        //LocationView();

        //Intent i = new Intent(firstPageActivity.this , buttom_barActivity.class);
        //启动 
        //startActivity(i);

        //轮播图

        initView1();//初始化布局
        initImages();//初始化图片
        initViewPager();//初始化vp
        initPoints();//初始化点
        mViewPager.setCurrentItem(1, false);//让轮播图初始化时显示第一张
        mTitle.setText(titles[0]);// 设置轮播图第一张的标题显示
        mViewPager.setActivity(this, titles);//传入firstPageActivity对象和数据给ViewPager
        startSwitch();//轮播图开启切换
    }


    private void init() {
        tv_page_title = findViewById(R.id.tv_page_title);
        tv_page_title.setText("首页");
        tv_head = findViewById(R.id.tv_head);
        tv_head.setBackgroundDrawable(getResources().getDrawable(R.drawable.meng));
        point = findViewById(R.id.point);
        point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(point);
            }
        });


    }

    private void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.page_menu, popupMenu.getMenu());
        // menu的item点击事件（未添加页面跳转）
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        // PopupMenu关闭事件（未设置）
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {

            }
        });

        popupMenu.show();
    }

    //选择城市
    public void initCityPicker() {
        cityPicker = new CityPicker.Builder(firstPageActivity.this)
                .textSize(20)//滚轮文字的大小
                .title("选择城市")
                .backgroundPop(0xa0000000)
                .titleBackgroundColor("#0CB6CA")
                .titleTextColor("#000000")
                .backgroundPop(0xa0000000)
                .confirTextColor("#000000")
                .cancelTextColor("#000000")
                .province("xx省")
                .city("xx市")
                .district("xx区")
                .textColor(Color.parseColor("#000000"))//滚轮文字的颜色
                .provinceCyclic(true)//省份滚轮是否循环显示
                .cityCyclic(false)//城市滚轮是否循环显示
                .districtCyclic(false)//地区（县）滚轮是否循环显示
                .visibleItemsCount(7)//滚轮显示的item个数
                .itemPadding(10)//滚轮item间距
                .onlyShowProvinceAndCity(true)
                .build();

        //监听方法，获取选择结果
        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                String province = citySelected[0];
                //城市
                String city = citySelected[1];
                //区县（如果设定了两级联动，那么该项返回空）
                String district = citySelected[2];
                //邮编
                String code = citySelected[3];

                select.setText(province + city + district);
                Log.e("aaaaaaaaaaaaaa", select.getText().toString());
            }

            @Override
            public void onCancel() {


            }
        });
    }

    //隐藏软键盘
    //    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    //    imm.hideSoftInputFromWindow(view.getWindowToken(),0); //强制隐藏键盘

    private void initView() {
        select = findViewById(R.id.select);
        select = (Button) findViewById(R.id.select);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCityPicker();
                cityPicker.show();
            }
        });
    }

    //实现定位
//    private void LocationView() {
//        //实现定位
//        locationText = (TextView) findViewById(R.id.tv_location);
//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//        ib_location = (ImageButton) findViewById(R.id.location);
//        ib_location.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                locationUpdates(getLocation());
//            }
//        });
//    }
//
//    //定位获取经纬度：
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private Location getLocation() {
//        //获取位置管理服务
//
//        //查找服务信息
//        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_FINE); //定位精度: 最高
//        criteria.setAltitudeRequired(false); //海拔信息：不需要
//        criteria.setBearingRequired(false); //方位信息: 不需要
//        criteria.setCostAllowed(true);  //是否允许付费
//        criteria.setPowerRequirement(Criteria.POWER_LOW); //耗电量: 低功耗
//        String provider = locationManager.getBestProvider(criteria, true); //获取GPS信息
////        myLocationManager.requestLocationUpdates(provider,2000,5,locationListener);
////        Log.e("provider", provider);
////        List<String> list = myLocationManager.getAllProviders();
////        Log.e("provider", list.toString());
////
//        Location gpsLocation = null;
//        Location netLocation = null;
//
//        if (netWorkIsOpen()) {
//            //2000代表每2000毫秒更新一次，5代表每5秒更新一次
//            //权限检查
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                getLocation();
//
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    Activity#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for Activity#requestPermissions for more details.
//                    return null;
//                }}
//
//            locationManager.requestLocationUpdates("net", 2000, 5,
//                    new LocationListener() {//监听定位信息是否改变
//
//                        @Override
//                        public void onLocationChanged(Location location) {//GPS定位信息发生改变
//                            Log.e("location", location.toString() + "....");
//                            // TODO Auto-generated method stub
//                            if (location != null) {
//                                //获取国家，省份，城市的名称
//                                Log.e("location", location.toString());
//                                List<Address> m_list = getAddress(location);
//                                // Log.e("str", m_list.toString());
//                                String city="" ;
//                                if (m_list != null && m_list.size() > 0) {
//                                    city = m_list.get(0).getLocality();//获取城市
//                                    locationText.setText("当前城市"+city);
//                                }
//                            } else {
//                                locationText.setText("获取不到数据");
//                            }
//                        }
//
//                        @Override
//                        public void onStatusChanged(String s, int i, Bundle bundle) {//GPS定位状态发生改变
//
//                        }
//
//                        @Override
//                        public void onProviderEnabled(String s) {//GPS定位提供者启动
//
//                        }
//
//                        @Override
//                        public void onProviderDisabled(String s) {//GPS定位提供者关闭
//
//                        }
//                    });
//            netLocation=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);//获取最新定位信息
//            locationUpdates(netLocation);
//
//        }
//        if (gpsIsOpen()) {
//            locationManager.requestLocationUpdates("gps", 2000, 5,
//                    new LocationListener(){//监听定位信息是否改变
//
//                        @Override
//                        public void onLocationChanged(Location location) {//GPS定位信息发生改变
//                            Log.e("location", location.toString() + "....");
//                            // TODO Auto-generated method stub
//                            if (location != null) {
//                                //获取国家，省份，城市的名称
//                                Log.e("location", location.toString());
//                                List<Address> m_list = getAddress(location);
//                                // Log.e("str", m_list.toString());
//                                String city="" ;
//                                if (m_list != null && m_list.size() > 0) {
//                                    city = m_list.get(0).getLocality();//获取城市
//                                    locationText.setText("当前城市"+city);
//                                }
//                            } else {
//                                locationText.setText("获取不到数据");
//                            }
//                        }
//
//                        @Override
//                        public void onStatusChanged(String s, int i, Bundle bundle) {//GPS定位状态发生改变
//
//                        }
//
//                        @Override
//                        public void onProviderEnabled(String s) {//GPS定位提供者启动
//
//                        }
//
//                        @Override
//                        public void onProviderDisabled(String s) {//GPS定位提供者关闭
//
//                        }
//                    });
//            gpsLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);//获取最新定位信息
//            locationUpdates(gpsLocation);
//        }
//        if (gpsLocation == null && netLocation == null) {
//            locationText.setText("获取不到数据");
//            return null;
//        }
//        if (gpsLocation != null && netLocation != null) {
//            if (gpsLocation.getTime() < netLocation.getTime()) {
//                gpsLocation = null;
//                return netLocation;
//            } else {
//                netLocation = null;
//                return gpsLocation;
//            }
//        }
//        if (gpsLocation == null) {
//            return netLocation;
//        } else {
//            return gpsLocation;
//        }
//
//    }
//    private boolean gpsIsOpen() {
//        boolean isOpen = true;
//        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {//没有开启GPS
//            isOpen = false;
//        }
//        return isOpen;
//    }
//
//    private boolean netWorkIsOpen() {
//        boolean netIsOpen = true;
//        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {//没有开启网络定位
//            netIsOpen = false;
//        }
//        return netIsOpen;
//    }
//
//    // 获取地址信息
//    private List<Address> getAddress(Location location) {
//        List<Address> result = null;
//        try {
//            if (location != null) {
//                Geocoder gc = new Geocoder(this, Locale.getDefault());
//                result = gc.getFromLocation(location.getLatitude(),
//                        location.getLongitude(), 1);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    //实现定位
//    public void locationUpdates(Location location){
//        if(location==null) {
//            //获取城市
//            locationText.setText("没有获取到GPS信息");
//
//        }
//
//    }

    //实现定位（城市）
    private Location getLocation() {
        //获取位置管理服务

        //查找服务信息
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); //定位精度: 最高
        criteria.setAltitudeRequired(false); //海拔信息：不需要
        criteria.setBearingRequired(false); //方位信息: 不需要
        criteria.setCostAllowed(true);  //是否允许付费
        criteria.setPowerRequirement(Criteria.POWER_LOW); //耗电量: 低功耗
//        String provider = myLocationManager.getBestProvider(criteria, true); //获取GPS信息
//        myLocationManager.requestLocationUpdates(provider,2000,5,locationListener);
//        Log.e("provider", provider);
//        List<String> list = myLocationManager.getAllProviders();
//        Log.e("provider", list.toString());
//
        Location gpsLocation = null;
        Location netLocation = null;
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return null;
        }
        locationManager.addGpsStatusListener(mylistener);
        if (netWorkIsOpen()) {
            //2000代表每2000毫秒更新一次，5代表每5秒更新一次
            locationManager.requestLocationUpdates("network", 2000, 5, locationListener);
            netLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if (gpsIsOpen()) {
            locationManager.requestLocationUpdates("gps", 2000, 5, locationListener);
            gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        if (gpsLocation == null && netLocation == null) {
            return null;
        }
        if (gpsLocation != null && netLocation != null) {
            if (gpsLocation.getTime() < netLocation.getTime()) {
                gpsLocation = null;
                return netLocation;
            } else {
                netLocation = null;
                return gpsLocation;
            }
        }
        if (gpsLocation == null) {
            return netLocation;
        } else {
            return gpsLocation;
        }
    }

    private boolean gpsIsOpen() {
        boolean isOpen = true;
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {//没有开启GPS
            isOpen = false;
        }
        return isOpen;
    }

    private boolean netWorkIsOpen() {
        boolean netIsOpen = true;
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {//没有开启网络定位
            netIsOpen = false;
        }
        return netIsOpen;
    }

    private LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            Log.e("location", location.toString() + "....");
            // TODO Auto-generated method stub
            if (location != null) {
                //获取国家，省份，城市的名称
                Log.e("location", location.toString());
//                List<Address> m_list = getAddress(location);
                new MyAsyncExtue().execute(location);
//                Log.e("str", m_list.toString());
//                String city = "";
////                if (m_list != null && m_list.size() > 0) {
////                    city = m_list.get(0).getLocality();//获取城市
////                }
//                city = m_list;
//                show_GPS.setText("location:" + m_list.toString() + "\n" + "城市:" + city + "\n精度:" + location.getLongitude() + "\n纬度:" + location.getLatitude() + "\n定位方式:" + location.getProvider());
            } else {
                show_GPS.setText("获取不到数据");
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

    };
    //轮播图
    private void initView1() {
        mViewPager = (MyViewPager) findViewById(R.id.bt_main_vp);
        mLayout = (LinearLayout) findViewById(R.id.ll_point_container);
        mTitle = (TextView) findViewById(R.id.tv_title);
    }

    private void initViewPager() {
        /**
         * ① 让轮播图自适应大小指的是自适应高度
         * ② 获取屏幕的宽，屏幕的宽就是viewpager的宽
         * ③ 屏幕宽/vp应有的高度 = 图片的宽/图片的高
         * ④ 有了宽和高，利用layoutParams动态设置vp的宽和高即可
         */
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;
        int height = widthPixels * 460 / 800;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
        mViewPager.setLayoutParams(params);

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(mImageViewList);
        mViewPager.setAdapter(myPagerAdapter);

        mViewPager.addOnPageChangeListener(this);
    }

    /**
     * 初始化轮播图的图片并自适应大小
     */
    private void initImages() {
        int size = images.length;
        /**
         * 要想实现无限轮播，在轮播图左右各添加一张图片，右边添加的是第一张图片，左边添加的是最后一张图片
         * 给人的感觉是从最后一张轮播到了第一张，实际上是往右面添加了一张，左边同理
         */
        for (int i = -1; i < size + 1; i++) {
            ImageView imageView = new ImageView(this);
            // 第一个ImageView显示数据源最后一张图片
            if (i == -1) {
                imageView.setImageResource(images[size - 1]);
            } else if (i == size) {
                //最后一个ImageView显示数据源第一张图片
                imageView.setImageResource(images[0]);
            } else {
                imageView.setImageResource(images[i]);
            }
            mImageViewList.add(imageView);
        }
    }

    /**
     * 初始化点
     */
    private void initPoints() {
        //清空容器里面的布局
        mLayout.removeAllViews();
        for (int i = 0; i < images.length; i++) {
            //小圆点
            View view = new View(this);
            //设置背景颜色
            view.setBackgroundResource(R.drawable.indicator_no_select);
            //小圆点布局参数
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(10, 10);
            //右边距
            layoutParams.rightMargin = 10;
            //添加布局
            mLayout.addView(view, layoutParams);
        }
        //让第一个点的背景为红色
        mLayout.getChildAt(0).setBackgroundResource(R.drawable.indicator_select);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //动态设置标题内容
        //修正标题下标：初始化
        int pageIndex = 0;
        //数据长度
        int size = titles.length;
        //加载第一张ImageView,实际标题是数据源最后一个
        if (position == 0) {
            pageIndex = size - 1;
            //切换导致最后一个页面
            mViewPager.setCurrentItem(size, false);
            //position为最后一个ImageView,实际标题是数据源第一个
        } else if (position == size + 1) {
            pageIndex = 0;
            //切换到第一个页面
            mViewPager.setCurrentItem(1, false);
        } else {
            pageIndex = position - 1;
        }
        mTitle.setText(titles[pageIndex]);
        //切换点
        int childCount = mLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = mLayout.getChildAt(i);
            //点根据标题走即可，因为数据的数量一致
            if (pageIndex == i) {
                //选中
                view.setBackgroundResource(R.drawable.indicator_select);
            } else {
                //未选中
                view.setBackgroundResource(R.drawable.indicator_no_select);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 开始切换
     */
    public void startSwitch() {
        if (!isSwitch) {
            mHandler.postDelayed(new SwitchTask(), 3000);
        }
    }

    /**
     * 停止切换
     */
    public void stopSwitch() {
        mHandler.removeCallbacksAndMessages(null);
        isSwitch = false;
    }

    /**
     * 切换的任务
     */
    private class SwitchTask implements Runnable {

        @Override
        public void run() {
            //切换逻辑
            int currentItem = mViewPager.getCurrentItem();
            if (currentItem == mImageViewList.size() - 1) {
                currentItem = 0;
            } else {
                currentItem++;
            }
            mViewPager.setCurrentItem(currentItem);
            mHandler.postDelayed(this, 3000);
        }
    }

    /**
     *  防止资源泄漏
     */
    @Override
    protected void onPause() {
        super.onPause();
        stopSwitch();
    }
}


















