package com.example.androdev.ddxr.UserFragments;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.androdev.ddxr.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSearch extends Fragment  {

    LinearLayout search;


 public  static  EditText SearchEdittext;

    TabLayout tabLayout;
    ViewPager viewPager;
    SearchPagerAdapter pagerAdapter;


    public FragmentSearch() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        search = view.findViewById(R.id.ll_searchWithBack);
        SearchEdittext = (EditText) view.findViewById(R.id.editText_search);



        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);


        SearchEdittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    SearchEdittext.setCursorVisible(true);
                    SearchEdittext.setBackgroundColor(Color.parseColor("#ffffff"));
                    SearchEdittext.setCompoundDrawableTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.black)));
                    SearchEdittext.setHintTextColor(Color.parseColor("#000000"));
                }else{
                    SearchEdittext.setCursorVisible(false);
                    SearchEdittext.setBackgroundColor(Color.parseColor("#563C5C"));
                    SearchEdittext.setCompoundDrawableTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.white)));
                    SearchEdittext.setHintTextColor(Color.parseColor("#ffffff"));
                }
            }

        });


        SearchEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SearchEdittext.setCursorVisible(true);
                SearchEdittext.setBackgroundColor(Color.parseColor("#ffffff"));
                SearchEdittext.setCompoundDrawableTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(),R.color.black)));
                SearchEdittext.requestFocus();
                SearchEdittext.setHintTextColor(Color.parseColor("#000000"));

              //  getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchEdittext.setBackgroundColor(Color.parseColor("#ffffff"));
                SearchEdittext.setCursorVisible(true);
                SearchEdittext.setCompoundDrawableTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(),R.color.black)));
                SearchEdittext.requestFocus();
                SearchEdittext.setHintTextColor(Color.parseColor("#000000"));

            }
        });



        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("COLLEGE"));
        tabLayout.addTab(tabLayout.newTab().setText("PEOPLE"));
        tabLayout.addTab(tabLayout.newTab().setText("ORGANIZERS"));
        tabLayout.addTab(tabLayout.newTab().setText("EVENTS"));


        LinearLayout layout = ((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(0));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        layoutParams.weight = 0.7f;
        layout.setLayoutParams(layoutParams);

        LinearLayout layout2 = ((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(1));
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) layout2.getLayoutParams();
        layoutParams2.weight = 0.65f;
        layout2.setLayoutParams(layoutParams2);

        LinearLayout layout3 = ((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(2));
        LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) layout3.getLayoutParams();
        layoutParams3.weight = 1.0f;
        layout3.setLayoutParams(layoutParams3);


        LinearLayout layout4 = ((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(3));
        LinearLayout.LayoutParams layoutParams4 = (LinearLayout.LayoutParams) layout4.getLayoutParams();
        layoutParams4.weight = 0.65f;
        layout4.setLayoutParams(layoutParams4);

     /*   tabLayout.addTab(tabLayout.newTab().setText("LOCATION"));*/
       /* tabLayout.addTab(tabLayout.newTab().setText("TAGS"));*/



        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager.setOffscreenPageLimit(3);
        pagerAdapter = new SearchPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());


        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {


            }


        });

        }

   public static String getEdittext(){
        return SearchEdittext.getText().toString();


    }






}
