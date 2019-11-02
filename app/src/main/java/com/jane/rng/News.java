package com.jane.rng;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class News extends Activity implements Runnable{

    Handler mHandler;
    String TAG = "here";
    String file;
    String titleLink;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.news);

        Intent it = getIntent();
        String titles = it.getStringExtra("titles");
        titleLink = it.getStringExtra("titleLink");

        TextView newsTitle = findViewById(R.id.newsTitle);
        final TextView news = findViewById(R.id.news);
        newsTitle.setText(titles);

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == 1){
                    String html = (String) msg.obj;
                    news.setText(html);
                }
            }
        };
        Thread tr = new Thread(this);
        tr.start();
    }

    @Override
    public void run() {
        //获取网页主内容
        file = Environment.getExternalStorageDirectory() + "/download.html";

        get(titleLink);
        String  html = parser.Main.getChinese(file);

        Message msg = mHandler.obtainMessage(1);
        msg.obj = html;
        mHandler.sendMessage(msg);
    }

    public void get(String url){
        String html;
        PrintWriter pw;
        try {
            Document doc = Jsoup.connect(url).get();
            pw = new PrintWriter(new File(file));
            html = doc.html();
            pw.print(html);
            pw.close();
        } catch (IOException e) {
            Log.i(TAG, "Download "+url+" fail");
        }
    }
}
