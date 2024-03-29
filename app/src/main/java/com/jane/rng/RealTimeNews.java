package com.jane.rng;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public class RealTimeNews extends AppCompatActivity implements Runnable{

    private String TAG = "here";
    Handler handler;
    private ArrayList<HashMap<String, String>> listItems;
    ListView listView;
//    private SimpleAdapter listItemAdapter;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.real_time_news);

        listView = findViewById(R.id.mylist);
        listView.setEmptyView(findViewById(R.id.loading));

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1){
//                    listItemAdapter = new SimpleAdapter(RealTimeNews.this, listItems,
//                            R.layout.custom_list,
//                            new String[]{"titles", "item_author", "item_time"},
//                            new int[] {R.id.titles, R.id.item_author, R.id.item_time});
//                    listView.setAdapter(listItemAdapter);
                    final ImageLoader imageLoader = new ImageLoader();
                    final CountDownLatch countDownLatch = new CountDownLatch(15);
                    for (HashMap<String, String> map: listItems){
                        final String url = map.get("imgLink");
                        new Thread(){
                            @Override
                            public void run() {
                                if(ImageLoader.cache.get(url) == null){
                                    Bitmap bitmap = getURLimage(url);
                                    ImageLoader.cache.put(url, bitmap);
                                }
                                countDownLatch.countDown();
                            }
                        }.start();
                    }
                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    MyAdapter myAdapter = new MyAdapter(RealTimeNews.this, R.layout.custom_list, listItems);
                    listView.setAdapter(myAdapter);
                }
                super.handleMessage(msg);
            }
        };

        Thread t = new Thread(this);
        t.start();

        //Android6.0以上的新规
        int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int permission = ActivityCompat.checkSelfPermission(RealTimeNews.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    RealTimeNews.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String, String> map = (HashMap<String, String>) listView.getItemAtPosition(i);
                String titles = map.get("titles");
                String titleLink = map.get("titleLink");

                Intent it = new Intent(RealTimeNews.this, News.class);
                it.putExtra("titles", titles);
                it.putExtra("titleLink", titleLink);
                startActivity(it);
            }
        });
    }

    private Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    @Override
    public void run() {
        listItems = new ArrayList<HashMap<String, String>>();
        try {
            String url = "http://lol.duowan.com/tag/296216563281.html?pc";
            Document doc = Jsoup.connect(url).get();
            Elements divs = doc.select("ul > li > div");

            for(int i=0; i<divs.size(); i+=2){
                Elements item_cover = divs.get(i).select("img[src]");
                Elements item_cont = divs.get(i+1).children();
                Elements item_info = item_cont.get(2).children();

                String imgLink = item_cover.first().attr("src");
                String titles = item_cont.get(0).text();
                String titleLink = "http://lol.duowan.com" + item_cont.select("a[href]").first().attr("href");
                String item_author = item_info.get(0).text();
                String item_time = item_info.get(1).text();

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("titles", titles);
                map.put("item_author", item_author);
                map.put("item_time", item_time);
                map.put("titleLink", titleLink);
                map.put("imgLink", imgLink);
                listItems.add(map);
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        Message msg = handler.obtainMessage(1);
//        msg.obj = listItems;
        handler.sendMessage(msg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.funcmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent it;
        if(item.getItemId() == R.id.hidden_gems){
            it = new Intent(RealTimeNews.this, HiddenGems.class);
            startActivity(it);
            this.finish();
        }
        else if(item.getItemId() == R.id.gossip){
            it = new Intent(RealTimeNews.this, Gossip.class);
            startActivity(it);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}