package com.jane.rng;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyAdapter extends ArrayAdapter {
    private String url;
    private Handler mHandler;
    private ImageView imageView;
    private static final String TAG = "here";

    public MyAdapter(Context context, int resource, ArrayList<HashMap<String, String>> list) {
        super(context, resource, list);
    }

    @SuppressLint("HandlerLeak")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list, parent, false);
        }

        final Map<String, String> map = (Map<String, String>) getItem(position);
        TextView titles = itemView.findViewById(R.id.titles);
        TextView item_author = itemView.findViewById(R.id.item_author);
        TextView item_time = itemView.findViewById(R.id.item_time);
        imageView = itemView.findViewById(R.id.imageView);

        titles.setText(map.get("titles"));
        item_author.setText(map.get("item_author"));
        item_time.setText(map.get("item_time"));

        url = map.get("imgLink");

        Bitmap result = ImageLoader.cache.get(url);
        if(result != null){
            imageView.setImageBitmap(result);
        }
        return itemView;
    }
}