package com.jane.rng;

import android.graphics.Bitmap;
import android.util.LruCache;

public class ImageLoader {

    //添加缓存，
    public static LruCache<String, Bitmap> cache;// 缓存，本质相当于一个map


    public ImageLoader() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 4;
        cache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                // TODO Auto-generated method stub
                // 每次存入时调用，返回Bitmap 的实际大小
                return value.getByteCount();
            }
        };
    }
}
