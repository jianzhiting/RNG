package com.jane.rng;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyViewPagerAdapter extends FragmentPagerAdapter {

    public MyViewPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0)
            return new FragmentRealTimeNews();
        else if(position == 1)
            return new FragmentGossip();
        else
            return new FragmentHiddenGems();
    }

    @Override
    public int getCount() {
        return 3;
    }
}
