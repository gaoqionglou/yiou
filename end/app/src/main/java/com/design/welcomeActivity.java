package com.design;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class welcomeActivity extends AppCompatActivity {

    private int dt = 5;
    private TextView jump;
    private Runnable runnable;
    private Timer timer;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    jump.setText("跳过 " + dt);
                    dt--;
                    if(dt<0){
                        //关闭定时器
                        timer.cancel();
                        startMainActivity();
                    }
            }
        }
    };

    private void countDown(){
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        };
        //开启计时器，时间间隔为1000ms
        timer.schedule(timerTask,1,1000);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//隐藏标题栏
        setContentView(R.layout.activity_welcome);
        jump = (TextView) findViewById(R.id.jump);
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                startMainActivity();
            }
        });
        countDown();
    }

    private void startMainActivity() {
        startActivity(new
                Intent(welcomeActivity.this,MainActivity.class));
        //关闭当前页面
        finish();
    }

    protected void onDestroy() {
        super.onDestroy();
        //移除消息
        handler.removeCallbacksAndMessages(null);
    }

}
