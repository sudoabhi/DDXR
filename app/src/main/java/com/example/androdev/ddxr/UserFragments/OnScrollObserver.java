package com.example.androdev.ddxr.UserFragments;

import android.widget.AbsListView;

/**
 * Created by Asus on 3/19/2019.
 */

public abstract class OnScrollObserver implements AbsListView.OnScrollListener {

    public abstract void onScrollUp();

    public abstract void onScrollDown();

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    int last = 0;
    boolean control = true;

    @Override
    public void onScroll(AbsListView view, int current, int visibles, int total) {
        if (current < last && !control) {
            onScrollUp();
            control = true;
        } else if (current > last && control) {
            onScrollDown();
            control = false;
        }

        last = current;
    }

}