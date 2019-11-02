package com.jane.rng;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

public class ViewPage extends AppCompatActivity {
    // 默认情况下的icon
    final int[] imageResId = {
            R.drawable.selected_real_time_news,
            R.drawable.selected_gossip,
            R.drawable.selected_hidden_gems
    };
    private String tabTitles[] = new String[] {"动态", "闲聊", "彩蛋"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_page);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        ViewPager viewPager = findViewById(R.id.viewPage);
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        // 设置TabLayout.Tab的icon和title
        /*for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setIcon(imageResId[i]);
            tab.setText(tabTitles[i]);
        }*/
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setCustomView(getTabView(tabTitles[i], imageResId[i]));
        }

        // 默认选中第二个Tab
        tabLayout.getTabAt(0).select();
    }

    public View getTabView(String title, int image_src) {
        View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_tab, null);
        TextView textView = v.findViewById(R.id.textview);
        textView.setText(title);
        ImageView imageView = v.findViewById(R.id.imageview);
        imageView.setImageResource(image_src);
        return v;
    }
}
