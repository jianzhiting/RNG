package com.jane.rng;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public class HiddenGems extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {
    private String TAG = "here";
    private ImageView iv1,iv2,iv3;
    private int day_record;
    private int number_record;
    private Handler mHandler;
    // 两次点击按钮之间的最小点击间隔时间(单位:ms)
    private static final int MIN_CLICK_DELAY_TIME = 1500;
    // 最后一次点击的时间
    private long lastClickTime;

    @SuppressLint({"ClickableViewAccessibility", "HandlerLeak"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hidden_gems);

        SharedPreferences sp = getSharedPreferences("hiddenGems", MODE_PRIVATE);
        day_record = sp.getInt("day_key", 0);
        number_record = sp.getInt("number_key", 0);

        iv1 = findViewById(R.id.imageView1);
        iv2 = findViewById(R.id.imageView2);
        iv3 = findViewById(R.id.imageView3);

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                ImageView iv = (ImageView) msg.obj;
                int up = msg.arg1;
                int down = msg.arg2;
                iv.setImageResource(up);
                if(down == R.mipmap.success){
                    Intent it = new Intent(HiddenGems.this, Article.class);
                    startActivity(it);
                }
                super.handleMessage(msg);
            }
        };

//        iv1.setOnTouchListener(this);
//        iv2.setOnTouchListener(this);
//        iv3.setOnTouchListener(this);
        iv1.setOnClickListener(this);
        iv2.setOnClickListener(this);
        iv3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //获取date 比较date 如果不同就更新+将当前图片编号写入number
        //如果相同，就判断当前number是否一致，一致则跳转，不一致则失败
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {// 两次点击的时间间隔大于最小限制时间，则触发点击事件
            lastClickTime = currentTime;

            Calendar cal= Calendar.getInstance();
            int day=cal.get(Calendar.DATE);
            int number = view.getId();
            ImageView iv = (ImageView) view;
            int imgview;
            if (number == iv1.getId())
                imgview = R.mipmap.left;
            else if (number == iv2.getId())
                imgview = R.mipmap.middle;
            else
                imgview = R.mipmap.right;

            if(day == day_record&&number != number_record){
                //今日已选，但没命中
                Log.i(TAG, "onTouch: 错的答案");
                OnClickChange(iv, R.mipmap.fail, imgview);
            }
            else {
                if(day != day_record){
                    //今日未选
                    Log.i(TAG, "onTouch: 第一次选");
                    SharedPreferences sp = getSharedPreferences("hiddenGems", MODE_PRIVATE);
                    SharedPreferences.Editor ed = sp.edit();
                    ed.putInt("day_key", day);
                    ed.putInt("number_key", number);
                    ed.apply();
                    day_record = day;
                    number_record = number;
                }
                OnClickChange(iv, R.mipmap.success, imgview);
            }
        }
    }

    private void OnClickChange(final ImageView iv, final int down, final int up){
        iv.setImageResource(down);
        if (down == R.mipmap.fail) {
            Toast.makeText(HiddenGems.this, R.string.toast, Toast.LENGTH_SHORT).show();
        }
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = mHandler.obtainMessage();
                msg.arg1 = up;
                msg.arg2 = down;
                msg.obj = iv;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //获取date 比较date 如果不同就更新+将当前图片编号写入number
        //如果相同，就判断当前number是否一致，一致则跳转，不一致则失败
        Calendar cal= Calendar.getInstance();
        int day=cal.get(Calendar.DATE);
        int number = view.getId();
        ImageView iv = (ImageView) view;
        int imgview;
        if (number == iv1.getId())
            imgview = R.mipmap.left;
        else if (number == iv2.getId())
            imgview = R.mipmap.middle;
        else
            imgview = R.mipmap.right;

        if(day == day_record&&number != number_record){
            //今日已选，但没命中
            Log.i(TAG, "onTouch: 错的答案");
            OnTouchChange(motionEvent, iv, R.mipmap.fail, imgview);
        }
        else {
            if(day != day_record){
                //今日未选
                Log.i(TAG, "onTouch: 第一次选");
                SharedPreferences sp = getSharedPreferences("hiddenGems", MODE_PRIVATE);
                SharedPreferences.Editor ed = sp.edit();
                ed.putInt("day_key", day);
                ed.putInt("number_key", number);
                ed.apply();
                day_record = day;
                number_record = number;
            }
            OnTouchChange(motionEvent, iv, R.mipmap.success, imgview);
        }
        return true;
    }

    private void OnTouchChange(MotionEvent motionEvent, ImageView iv, int down, int up){
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (down == R.mipmap.fail){
                    Toast.makeText(HiddenGems.this, R.string.toast, Toast.LENGTH_SHORT).show();
                }
                iv.setImageResource(down);
                break;
            case MotionEvent.ACTION_UP:
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                iv.setImageResource(up);
                if(down == R.mipmap.success){
                    Intent it = new Intent(HiddenGems.this, Article.class);
                    startActivity(it);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.funcmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent it;
        if(item.getItemId() == R.id.real_time_news){
            it = new Intent(HiddenGems.this, RealTimeNews.class);
            startActivity(it);
            this.finish();
        }
        else if(item.getItemId() == R.id.gossip){
            it = new Intent(HiddenGems.this, Gossip.class);
            startActivity(it);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
