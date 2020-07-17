package com.example.androdev.ddxr.UserFragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Asus on 1/25/2019.
 */

public class ProfilePagerAdapter extends FragmentPagerAdapter {
    public ProfilePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch(i){

            case 0:
                return new FragmentNewsFeed();
            case 1:
                return new FragmentBlogs();
            case 2:
                return new FragmentSearch();
            case 3:
                return new FragmentProfile();

            default:
                return new FragmentProfile();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }



}

