package com.design;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.design.model.News;
import com.design.web.H5Activity;
import com.lljjcoder.citypickerview.widget.CityPicker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

@SuppressWarnings("ALL")
public class firstPageActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {


    private TextView tv_page_title;//标题
    private ImageButton tv_head;
    private ImageButton point;

    //选择城市
    private Button select;
    private CityPicker cityPicker;

    //实现定位
    private TextView locationText;
    public LocationClient mLocationClient; //初始化LocationClient类
    public MyLocationListener myListener = new MyLocationListener();

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

    //新闻推送
    private List<News> newsList;
    private NewsAdapter adapter;
    private Handler handler;
    private ListView lv;

    private static final String TAG1 = "1";


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        init();//自定义标题栏
        getSupportActionBar().hide();//隐藏标题栏
        initView();//获取选择城市

        // 实现定位
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //声明LocationClient类
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(myListener); //注册监听函数
        setLocationOption(); //定义setLocationOption()方法
        locationText = (TextView) findViewById(R.id.tv_location); //初始化文本
        mLocationClient.requestLocation();
        mLocationClient.start(); //执行定位


        //轮播图
        initView1();//初始化布局
        initImages();//初始化图片
        initViewPager();//初始化vp
        initPoints();//初始化点
        mViewPager.setCurrentItem(1, false);//让轮播图初始化时显示第一张
        mTitle.setText(titles[0]);// 设置轮播图第一张的标题显示
        mViewPager.setActivity(this, titles);//传入firstPageActivity对象和数据给ViewPager
        startSwitch();//轮播图开启切换

        //新闻推送
        handleSSLHandshake();
        newsList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.news_lv);
        getNews();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    adapter = new NewsAdapter(firstPageActivity.this, newsList);
                    lv.setAdapter(adapter);
                    setListViewHeightBasedOnChildren(lv);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            News news = newsList.get(position);
                            Intent intent = new Intent(firstPageActivity.this, H5Activity.class);

                            intent.putExtra("url", news.getNewsTitle());
                            intent.putExtra("url", news.getNewsUrl());
                            startActivity(intent);
                        }
                    });
                }
            }
        };


    }

    //标题栏
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

    //标题栏菜单选项
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
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //将获取的City赋值给txt
            //当前设备位置所在的省
            String province = location.getProvince();
            //当前设备位置所在的市
            String city = location.getCity();
            Log.i("TAG", "location.getCity()=" + location.getCity());
            //返回码
            int i = location.getLocType();
            locationText.setText(province + "," + city);
            Toast.makeText(firstPageActivity.this, province + "," + city + "," + i, Toast.LENGTH_LONG).show();
        }

        public void onReceivePoi(BDLocation arg0) {
        }
    }

    //执行onDestroy()方法，停止定位
    @Override
    public void onDestroy() {
        mLocationClient.stop();
        super.onDestroy();
    }

    //
    //设置相关参数
    @SuppressWarnings("deprecation")
    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setOpenGps(true); //打开gps
        option.setAddrType("all");//返回定位结果包含地址信息
        option.setPriority(LocationClientOption.NetWorkFirst); // 设置网络优先
        option.setPriority(LocationClientOption.GpsFirst);       //gps
        option.disableCache(true);//禁止启用缓存定位
        mLocationClient.setLocOption(option);
    }

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
     * 防止资源泄漏
     */
    @Override
    protected void onPause() {
        super.onPause();
        stopSwitch();
    }


    //新闻推送
    private void getNews() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //获取新闻页的数据，网址为：https://www.ncnews.com.cn/xwzx/ncxw/
                    Document doc;
                    try {
                        String URL = "https://www.ncnews.com.cn/xwzx/ncxw/";
                        doc = Jsoup.connect(URL).get();
                        Elements ElementsUl = doc.select("ul.chrsh");
                        for (Element elementLi : ElementsUl) {
                            Elements titleLinks = elementLi.select("li");
                            for (Element element : titleLinks) {
                                String title = element.select("h3").select("a").text();
                                String uri = element.select("h3").select("a").attr("abs:href");
                                String desc = element.select("p").text();
                                handleSSLHandshake();
                                Document doc1;
                                doc1 = Jsoup.connect(uri).get();
                                Elements timeLinks = doc1.getElementsByClass("newsinfo");   //解析来获取每条新闻的时间与来源
                                for (Element e : timeLinks) {
                                    String time = e.text();
                                    News news = new News(title, uri, desc, time);
                                    //News news = new News(title,uri,desc,time);
                                    newsList.add(news);
                                }
                            }
                        }

                    } catch (Exception e) {
                        // e.printStackTrace();
                        Log.d(TAG1, "run: " + e);
                    }
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);

                } catch (Exception e) {
                    //e.printStackTrace();
                    Log.d(TAG1, "run: " + e);
                }
            }
        }).start();
    }

    //忽略https的证书校验
    //在获取sslParams时，修改并自定义TrustManager为trustAllCerts
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("TLS");
            // trustAllCerts信任所有的证书
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }


    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

}


















