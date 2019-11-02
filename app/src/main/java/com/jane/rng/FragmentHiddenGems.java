package com.jane.rng;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentHiddenGems extends Fragment implements View.OnClickListener {
    private ImageView iv1,iv2,iv3;
    private int day_record;
    private int number_record;
    private Handler mHandler;
    // 两次点击按钮之间的最小点击间隔时间(单位:ms)
    private static final int MIN_CLICK_DELAY_TIME = 1500;
    // 最后一次点击的时间
    private long lastClickTime;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        return inflater.inflate(R.layout.hidden_gems, container, false);
    }

    @SuppressLint({"ClickableViewAccessibility", "HandlerLeak"})
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences sp = getActivity().getSharedPreferences("hiddenGems", getContext().MODE_PRIVATE);
        day_record = sp.getInt("day_key", 0);
        number_record = sp.getInt("number_key", 0);

        iv1 = getView().findViewById(R.id.imageView1);
        iv2 = getView().findViewById(R.id.imageView2);
        iv3 = getView().findViewById(R.id.imageView3);

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                ImageView iv = (ImageView) msg.obj;
                int up = msg.arg1;
                int down = msg.arg2;
                iv.setImageResource(up);
                if(down == R.mipmap.success){
                    Intent it = new Intent(getContext(), Article.class);
                    startActivity(it);
                }
                super.handleMessage(msg);
            }
        };

        iv1.setOnClickListener(this);
        iv2.setOnClickListener(this);
        iv3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
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
                OnClickChange(iv, R.mipmap.fail, imgview);
            }
            else {
                if(day != day_record){
                    //今日未选
                    SharedPreferences sp = getActivity().getSharedPreferences("hiddenGems", getContext().MODE_PRIVATE);
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
            Toast.makeText(getContext(), R.string.toast, Toast.LENGTH_SHORT).show();
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
}
