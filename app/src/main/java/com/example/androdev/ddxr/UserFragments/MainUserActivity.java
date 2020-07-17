package com.example.androdev.ddxr.UserFragments;

import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.androdev.ddxr.R;

public class MainUserActivity extends AppCompatActivity {

    private static final int NUM_PAGES = 4;
    int position;

   /* private int[] tabIcons = {
            R.drawable.ic_home_black_24dp,
            R.drawable.ic_library_books_black_24dp,
            R.drawable.ic_search_grey_24dp,
            R.drawable.ic_person_grey_24dp

    };*/

    private int[] navIcons = {
            R.drawable.ic_trophy_inactive,
            R.drawable.ic_library_books_inactive_24dp,
            R.drawable.ic_search_inactive_24dp,
            R.drawable.ic_person_inactive_24dp
    };
    private int[] navLabels = {
            R.string.events,
            R.string.discussions,
            R.string.search,
            R.string.profile
    };
    // another resouces array for active state for the icon
    private int[] navIconsActive = {
            R.drawable.ic_trophy_active,
            R.drawable.ic_library_books_active_24dp,
            R.drawable.ic_search_active_24dp,
            R.drawable.ic_person_active_24dp
    };


    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    public ViewPager mPager;
    TabLayout tabLayout;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        position = -2;



        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            position = extras.getInt("viewpager_position");
        }


        tabLayout = (TabLayout) findViewById(R.id.tab_layout);



        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        mPager = (ViewPager) findViewById(R.id.viewpager);
       // mPager.setOffscreenPageLimit(4);

        mPagerAdapter = new ProfilePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(mPager, true);
      /*  mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                invalidateFragmentMenus(position);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });*/
        if(position!=-2){
            mPager.setCurrentItem(position);
            /*tabLayout.getTabAt(0).setIcon(navIcons[0]);
            tabLayout.getTabAt(1).setIcon(navIconsActive[1]);*/

        }


        for (int i = 0; i < tabLayout.getTabCount(); i++) {

            LinearLayout tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.parent_custom_tab, null);
            TextView tab_label = (TextView) tab.findViewById(R.id.nav_label);
            ImageView tab_icon = (ImageView) tab.findViewById(R.id.nav_icon);

            tab_label.setText(getResources().getString(navLabels[i]));

            // set the home to be active at first
            if (i == 0) {
                if(position!=-2){
                   // tab_label.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.purple));
                    tab_icon.setImageResource(navIcons[i]);
                }
                else{
                    tab_label.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.purple));
                    tab_icon.setImageResource(navIconsActive[i]);
                }

            } else if(i==1) {
                if(position!=-2){
                    tab_label.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.purple));
                    tab_icon.setImageResource(navIconsActive[i]);
                }
                else{
                    tab_icon.setImageResource(navIcons[i]);
                }

            }
            else{
                tab_icon.setImageResource(navIcons[i]);
            }



            // finally publish this custom view to navigation tab
            tabLayout.getTabAt(i).setCustomView(tab);
        }






       /* tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(0).setText("Home");
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);*/



      //  mPagerAdapter = new ProfilePagerAdapter(getSupportFragmentManager());



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                View tabView = tab.getCustomView();

                // get inflated children Views the icon and the label by their id
                TextView tab_label = (TextView) tabView.findViewById(R.id.nav_label);
                ImageView tab_icon = (ImageView) tabView.findViewById(R.id.nav_icon);

                // change the label color, by getting the color resource value
                tab_label.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.purple));
                // change the image Resource
                // i defined all icons in an array ordered in order of tabs appearances
                // call tab.getPosition() to get active tab index.
                tab_icon.setImageResource(navIconsActive[tab.getPosition()]);
                /*int tabIconColor = ContextCompat.getColor(MainUserActivity.this, R.color.tabSelectedMain);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);*/

                if(position!=-2){
                    mPager.setCurrentItem(position);
                }
                else{
                    mPager.setCurrentItem(tab.getPosition());
                }



            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                View tabView = tab.getCustomView();
                TextView tab_label = (TextView) tabView.findViewById(R.id.nav_label);
                ImageView tab_icon = (ImageView) tabView.findViewById(R.id.nav_icon);

                // back to the black color
                tab_label.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.tabUnSelectedIconColor));
                // and the icon resouce to the old black image
                // also via array that holds the icon resources in order
                // and get the one of this tab's position
                tab_icon.setImageResource(navIcons[tab.getPosition()]);
               /* int tabIconColor = ContextCompat.getColor(MainUserActivity.this, R.color.tabUnselectedMain);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);*/
               // tab.getIcon().set


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            /*    int tabIconColor = ContextCompat.getColor(MainUserActivity.this, R.color.tabSelectedMain );
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);*/

            }
        });


    }

    /*public void  invalidateFragmentMenus(int position){
        for(int i = 0; i < mPagerAdapter.getCount(); i++){
            mPagerAdapter.getItem(i).setHasOptionsMenu(i == position);
        }
        invalidateOptionsMenu(); //or respectively its support method.
    }*/


}
