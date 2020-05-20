package com.design;

import android.app.Application;
import android.content.Context;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

public class MyApplication extends Application {
    public static String APPID = "71aa042289ed6bcf1bd96b4abf7f14e8";
    private static MyApplication myApplication;
    private static Context context;


    public static Context getContext() {
        return context;
    }

    public static MyApplication getMyApplication() {
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        myApplication = this;

        // 创建bmob数据库的唯一标识码，在bmob数据库的设置里面可以看到
        Bmob.initialize(this, APPID);
        BmobConfig config = new BmobConfig.Builder(this)
                //设置appkey
                .setApplicationId(APPID)
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(1024 * 1024)
                //文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(2500)
                .build();
        Bmob.initialize(config);
    }
}
