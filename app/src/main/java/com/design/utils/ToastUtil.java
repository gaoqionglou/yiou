package com.design.utils;

import android.widget.Toast;


import com.design.MyApplication;

public class ToastUtil {
    public static void toast(String text) {
        Toast.makeText(MyApplication.getMyApplication().getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
