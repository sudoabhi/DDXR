package com.example.androdev.ddxr.UserFragments;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androdev.ddxr.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlogOrganizer extends Fragment {


    public BlogOrganizer() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blog_organizer, container, false);
    }

}
