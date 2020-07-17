package com.example.androdev.ddxr.UserFragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Asus on 2/1/2020.
 */

public class FragmentBlogVPAdapter extends FragmentPagerAdapter {
    public FragmentBlogVPAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new BlogOrganizer();
        }
        else if (position == 1) {
            fragment = new BlogStudent();

        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
