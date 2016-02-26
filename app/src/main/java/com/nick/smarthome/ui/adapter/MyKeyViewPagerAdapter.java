package com.nick.smarthome.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nick.smarthome.ui.fragment.MyKeyFragment;


public class MyKeyViewPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT =2;
    private String titles[] ;

    public MyKeyViewPagerAdapter(FragmentManager fm, String[] titles2) {
        super(fm);
        titles=titles2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            // Open FragmentTab1.java
            case 0:
                return MyKeyFragment.newInstance(position);
            case 1:
                return MyKeyFragment.newInstance(position);

        }
        return null;
    }

    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

}