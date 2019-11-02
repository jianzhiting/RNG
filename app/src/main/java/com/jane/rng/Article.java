package com.jane.rng;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class Article extends Activity {

    private String TAG = "here";
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.article);

        WebView wv = findViewById(R.id.webView);

        WebSettings webSettings = wv.getSettings();//获取webview设置属性
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setTextZoom(200);

        String file[] = new String[]{
                "file:///android_asset/xiao_hu.html",
                "file:///android_asset/karsa.html",
                "file:///android_asset/uzi.html",
                "file:///android_asset/shi_sen_ming.html",
                "file:///android_asset/way_to_win.html"
        };
        SharedPreferences sp = getSharedPreferences("hiddenGems", MODE_PRIVATE);
        int day = sp.getInt("day_key", 0);
        int num = sp.getInt("number_key", 0);
        int choice = (day*3 + num)%file.length;
        wv.loadUrl(file[choice]);
    }
}
