package com.example.androdev.ddxr.UserFragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androdev.ddxr.UserActivities.MyPosts;
import com.example.androdev.ddxr.ClubDetailsPackage.ClubDetails;
import com.example.androdev.ddxr.EventDetails.EventDetail;
import com.example.androdev.ddxr.Notification.ManualNotification;
import com.example.androdev.ddxr.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNewsFeed extends Fragment implements TextWatcher {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    FirebaseDatabase events_database;
    DatabaseReference events_reference;
    DatabaseReference event_node_reference;
    FirebaseUser user;
    DatabaseReference parentReference;
    DatabaseReference adminReference;
    int itemcount;
    TextView event_domain;
    TextView filter;
    String JDistrictName;
    ArrayAdapter jadapter;
    String ClubLocation;
    String MyImageURL;
    ImageView notification;
    TabLayout tabLayout;

    int postSize;
    SharedPreferences myPrefs2;
    RelativeLayout splash_screen;


    // List<MyPosts> posts;


    List<MyPosts> posts;

    ArrayList<String> districts;
    String CollegeName;

    String ClubName;
    String EventDescription;
    String EventName;
    String Event_Dates;
    String key;

    String EventBanner;
    String Club_Image_Url;

    String DistrictName;
    String EventType;
    String UpdatedDay;
    String UploadedBy;

    String likeS;
    String sort_category;

    String Key;
    View sortby_view;
    View scroll_view;
    int mLastFirstVisibleItem;
    AlertDialog alertDialog2;
    AlertDialog alertDialog;

    ImageView noresults_iv;
    TextView noresults_tv;
    ArrayList<String> newList;
   /* FrameLayout filter_head;
    FrameLayout domain_head;

    LinearLayout domain_head_1;
    LinearLayout domain_head_2;
    LinearLayout filter_head_1;
    LinearLayout filter_head_2;*/


    SwipeRefreshLayout swipeRefreshLayout;


    ChildEventListener mchildeventlistener;
    LinearLayout no_event_happening;


    /*TextView domain_category;
    TextView filter_category;*/



    public FragmentNewsFeed() {
        // Required empty public constru;ctor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_fragment_news_feed_2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout=(TabLayout) getActivity().findViewById(R.id.tab_layout);
        tabLayout.setVisibility(View.GONE);
        tabLayout.setActivated(false);
        tabLayout.setClickable(false);



       // domain_category=(TextView) view.findViewById(R.id.domain_category);
      //  filter_category=(TextView) view.findViewById(R.id.filter_category);
     //   Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      //  domain_head=(FrameLayout) view.findViewById(R.id.domain_head);
     //   filter_head=(FrameLayout) view.findViewById(R.id.filter_head);

     //   domain_head_1=(LinearLayout) view.findViewById(R.id.domain_head_1);
     //   domain_head_2=(LinearLayout) view.findViewById(R.id.domain_head_2);
     //   sortby_view = (LinearLayout) view.findViewById(R.id.sortby_view);
    //    sortby_view.setEnabled(false);
    //    filter_head.setEnabled(false);
     //   domain_head.setEnabled(false);
    //    filter_head_1=(LinearLayout) view.findViewById(R.id.filter_head_1);
    //    filter_head_2=(LinearLayout) view.findViewById(R.id.filter_head_2);
     //   toolbar.setTitleTextColor(Color.WHITE);
      /*  ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Toolbar");*/


      FirebaseMessaging.getInstance().subscribeToTopic("all");



        events_database = FirebaseDatabase.getInstance("https://ddrx-events-172d3.firebaseio.com/");
        events_reference = events_database.getReference();
        events_reference.keepSynced(true);

        parentReference=FirebaseDatabase.getInstance().getReference();
        adminReference=FirebaseDatabase.getInstance("https://ddrx-admin-172d3.firebaseio.com/").getReference();


        getProfileImage(new FirebaseCallbackX() {
            @Override
            public void onCallBack(String string) {

                MyImageURL=string;
                Log.e("url",""+MyImageURL);

            }


        } );






        posts = new ArrayList<>();

        districts=new ArrayList<>();
        newList = new ArrayList<>();
        mAdapter = new NewsFeedAdapter(getActivity(), posts);
        jadapter = new ArrayAdapter<String>(getActivity(),R.layout.listview,newList);
        notification =(ImageView) view.findViewById(R.id.notif);

        noresults_iv=(ImageView)view.findViewById(R.id.noresults_iv);
        noresults_tv=(TextView) view.findViewById(R.id.noresults_tv);
        splash_screen=(RelativeLayout) view.findViewById(R.id.splash_screen);



        user = FirebaseAuth.getInstance().getCurrentUser();



        event_node_reference = events_reference.push();
        key = event_node_reference.getKey();


        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);



        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);

        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);


        mRecyclerView.setAdapter(mAdapter);



        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                posts.clear();
                fetch();
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.mainColor,R.color.colorAccent,R.color.blue,R.color.mainColor);



      //  mAdapter.notifyDataSetChanged();
        posts.clear();

        fetch();

       /* domain_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                domain();

            }
        });

        filter_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                filter();
            }
        });*/







    notification.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(getActivity(), ManualNotification.class);
            startActivity(intent);
        }
    });





    }




    private void fetch(){


       // splash_screen.setVisibility(View.GONE);







        ChildEventListener childEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {
                int StartingPrice;
                int EndingPrice;
                String PriceSegment=null;


                String end_date = snapshot.child("EndDate").getValue(String.class);

                String start_date = snapshot.child("StartDate").getValue(String.class);

                final String dates = start_date + " - " + end_date;

                EventName = snapshot.child("EventName").getValue(String.class);

                final String EventTypeX=snapshot.child("EventTypeX").getValue(String.class);
                final String ClubDomain=snapshot.child("ClubDomain").getValue(String.class);
                final String EventLocation=snapshot.child("EventLocation").getValue(String.class);
                final long InterestedCount=snapshot.child("Interested").getChildrenCount();
                EventBanner = snapshot.child("EventThumbnailURL").getValue(String.class);
                if(snapshot.hasChild("StartingPrice")){
                    StartingPrice=snapshot.child("StartingPrice").getValue(Integer.class);
                }
                else{
                     StartingPrice=0;
                }
                if(snapshot.hasChild("EndingPrice")){
                     EndingPrice=snapshot.child("EndingPrice").getValue(Integer.class);
                }
                else{
                    EndingPrice=0;
                }

                if(StartingPrice==0&&EndingPrice==0){

                    PriceSegment="Free";

                }
                else if(StartingPrice!=0&&StartingPrice!=EndingPrice){
                    PriceSegment=String.valueOf(StartingPrice)+" - "+String.valueOf(EndingPrice);
                }
                else if(StartingPrice==0&&EndingPrice!=0){
                    PriceSegment="Upto "+String.valueOf(EndingPrice);
                }
                else if(StartingPrice==EndingPrice&&StartingPrice!=0){
                    PriceSegment=String.valueOf(StartingPrice);
                }

                int likeCount=0;
                int bookmarkCount=0;
                if(snapshot.hasChild("LikeCount")){
                    likeCount= Integer.parseInt(snapshot.child("LikeCount").getValue(String.class));
                }
                if(snapshot.hasChild("BookmarkCount")){
                    bookmarkCount=Integer.parseInt(snapshot.child("BookmarkCount").getValue(String.class));
                }



                ClubName = snapshot.child("ClubName").getValue(String.class);



                ClubLocation = snapshot.child("ClubLocation").getValue(String.class);

                EventType = snapshot.child("EventType").getValue(String.class);
                Club_Image_Url = snapshot.child("ClubImageURL").getValue(String.class);

                UploadedBy = snapshot.child("UploadedBy").getValue(String.class);
                Key = snapshot.child("EventKey").getValue(String.class);
                likeS=snapshot.child("Interested").child(user.getUid()).child("Like").getValue(String.class);
                final String Interested=snapshot.child("Interested").child(user.getUid()).child("Status").getValue(String.class);

                final String bookmark=snapshot.child("Interested").child(user.getUid()).child("Bookmark").getValue(String.class);
                 final String InterestedPerson1=snapshot.child("InterestedPerson1").getValue(String.class);
                final String InterestedPerson2=snapshot.child("InterestedPerson2").getValue(String.class);

                final ArrayList<String> InterestedPhoto = new ArrayList<>();

                if(InterestedPerson1!=null){
                    InterestedPhoto.add(InterestedPerson1);

                }
                if(InterestedPerson2!=null){
                    InterestedPhoto.add(InterestedPerson2);
                }

                if(InterestedPhoto.size()!=InterestedCount&&InterestedPhoto.size()<2){
                    Query query= events_reference.child(Key).child("Interested").limitToLast(2);
                    query.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                           String InterestedPerson1X=InterestedPerson1;
                           String InterestedPerson2X=InterestedPerson2;
                            if(InterestedPerson1X==null){
                              InterestedPerson1X= dataSnapshot.child("ImageUrl").getValue(String.class);
                              if(InterestedPhoto.get(0)!=null&&InterestedPerson1X!=null){
                                  if(!InterestedPerson1X.equals(InterestedPhoto.get(0))){
                                      events_reference.child(Key).child("InterestedPerson1").setValue(InterestedPerson1X);
                                      Log.e("Interested1","h"+InterestedPerson1X);
                                      Log.e("shimp","shimp"+InterestedPerson2);
                                      InterestedPhoto.add(InterestedPerson1X);
                                  }
                              }


                            }
                             if(InterestedPerson2X==null) {
                                InterestedPerson2X= dataSnapshot.child("ImageUrl").getValue(String.class);
                                 Log.e("AAx","xhemp");
                                 if(InterestedPhoto.get(0)!=null&&InterestedPerson2X!=null){
                                     if(!InterestedPerson2X.equals(InterestedPhoto.get(0))) {
                                         Log.e("AAxx","x"+InterestedPerson2);
                                         events_reference.child(Key).child("InterestedPerson2").setValue(InterestedPerson2X);
                                         Log.e("Interested2","h"+InterestedPerson2X);
                                         InterestedPhoto.add(InterestedPerson2X);
                                     }
                                 }

                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

              /*  getInterestedPhotos(new FirebaseCallBackX2() {
                    @Override
                    public void onCallBack(ArrayList<String> string) {
                        InterestedPhoto.add(0,string.get(0));
                        Log.e("ChildListener3","Hii "+InterestedPhoto.get(0));
                        if(string.size()>1){
                            InterestedPhoto.add(1,string.get(1));
                            Log.e("ChildListener4",""+InterestedPhoto.get(1));
                        }





                    }
                },Key);*/


             /*  final Query query=snapshot.child("Interested").getRef().limitToLast(2);

               query.addChildEventListener(new ChildEventListener() {
                   @Override
                   public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                       InterestedPhoto.add(dataSnapshot.child("ImageUrl").getValue(String.class));

                       Log.e("ChildListener1",""+InterestedPhoto.get(0));

                       query.addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                               posts.add(new MyPosts(ClubName, ClubLocation, EventDescription, dates, EventName, Club_Image_Url, EventBanner, UpdatedDay, UploadedBy, likeS, Key,bookmark,EventTypeX,ClubDomain,EventLocation,EventType,Interested,InterestedCount,InterestedPhoto));



                               mAdapter.notifyDataSetChanged();

                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {

                           }
                       });









                *//*if(InterestedPhotoS.get(1)!=null){
                    Log.e("ChildListener2",""+InterestedPhotoS.get(1));
                    firebaseCallBackX2.onCallBack(InterestedPhotoS);
                }*//*

                   }

                   @Override
                   public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                   }

                   @Override
                   public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                   }

                   @Override
                   public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });*/




              /*  final TaskCompletionSource<ArrayList<String>> tcs = new TaskCompletionSource<>();

                query.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        final ArrayList<String> InterestedPhotoS=new ArrayList<>();

                        InterestedPhotoS.add(dataSnapshot.child("ImageUrl").getValue(String.class));

                        Log.e("ChildListener1",""+InterestedPhotoS.get(0));

                        tcs.setResult(InterestedPhotoS);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                Task<ArrayList<String>> t = tcs.getTask();


                    final Task<ArrayList<String>> finalT = t;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Tasks.await(finalT);
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });



                if(t.isSuccessful()) {
                     InterestedPhoto = t.getResult();
                }





*/





                   Log.e("ChildListener4", "Hii" + InterestedPhoto.size());
               //   final String dates="24/12/19 - 25/12/19";


                   posts.add(new MyPosts(ClubName, ClubLocation, EventDescription, dates, EventName, Club_Image_Url,
                           EventBanner, UpdatedDay, UploadedBy, likeS, Key, bookmark, EventTypeX, ClubDomain,
                           EventLocation, EventType, Interested, InterestedCount, InterestedPhoto,PriceSegment,likeCount,
                           bookmarkCount));

                   Log.e("Interested","hii "+posts.size());
                 //  Toast.makeText(getActivity(),""+posts.size(),Toast.LENGTH_SHORT).show();
                   mAdapter.notifyDataSetChanged();
                   splash_screen.setVisibility(View.GONE);
                   tabLayout.setVisibility(View.VISIBLE);
                   //tabLayout.clearOnTabSelectedListeners();
                   tabLayout.setClickable(true);
                tabLayout.setActivated(true);

                   Log.e("Size2","hii "+posts.size());


                   adminReference.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           Log.e("Size3","hii "+posts.size());
                           if(posts.size()==0){

                               noresults_iv.setVisibility(View.VISIBLE);
                               noresults_tv.setVisibility(View.VISIBLE);

                               //   Toast.makeText(getActivity(),"No content available",Toast.LENGTH_SHORT).show();
                           }
                           else{



                               noresults_iv.setVisibility(View.INVISIBLE);
                               noresults_tv.setVisibility(View.INVISIBLE);

                           }


                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });


                  /* if (mAdapter.getItemCount() != 0) {

                       mRecyclerView.post(new Runnable() {
                           @Override
                           public void run() {

                               mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                           }
                       });

                   }*/






















/*
                if (sort_category2.equals("Liked Events")) {




                        if (likeS != null && likeS.equals("true")) {


                            posts.add(new MyPosts(ClubName, ClubLocation, EventDescription, dates, EventName, Club_Image_Url, EventBanner, UpdatedDay, UploadedBy, likeS, Key,bookmark,EventTypeX,ClubDomain,EventLocation,EventType,Interested,InterestedCount,InterestedPhoto));

                           mAdapter.notifyDataSetChanged();



                            if(mAdapter.getItemCount()!=0){

                                mRecyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Call smooth scroll
                                        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                                    }
                                });

                            }

                        } else {





                            mAdapter.notifyDataSetChanged();
                        }


                    }
                    if (sort_category2.equals("Subscribed Clubs")) {




                        mAdapter.notifyDataSetChanged();
                        if(mAdapter.getItemCount()!=0){

                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    // Call smooth scroll
                                    mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                                }
                            });

                        }



                    }*/



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                mAdapter.notifyDataSetChanged();


            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        events_reference.addChildEventListener(childEventListener);














    }
   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.newsfeed_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }*/

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.searchbtn:
                openLocationSearchWindow();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {





    }

    @Override
    public void afterTextChanged(Editable editable) {

        String k = editable.toString();
        districts.clear();
        newList.clear();
        jadapter.notifyDataSetChanged();

        if (k.length() == 0) {
            districts.clear();
            newList.clear();
            no_event_happening.setVisibility(View.VISIBLE);
            jadapter.notifyDataSetChanged();

        } else {

            mchildeventlistener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    JDistrictName = dataSnapshot.child("t").getValue(String.class);
                    districts.add(JDistrictName);
                   newList= removeDuplicateDistricts(districts);
                    jadapter.notifyDataSetChanged();

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            Query query = events_reference.orderByChild("t").startAt(k).endAt(k + "\uf8ff");

            query.addChildEventListener(mchildeventlistener);

            events_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(newList.size()==0){
                        no_event_happening.setVisibility(View.VISIBLE);
                    }
                    else
                        no_event_happening.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    private ArrayList<String>  removeDuplicateDistricts(ArrayList<String> list){

        for (String element : list) {

            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {

                newList.add(element);
            }
        }

        return newList;

    }

    private void filter(){

        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.newsfeed_filter, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());


        alertDialogBuilder.setView(promptsView);


        final AlertDialog alertDialog2 = alertDialogBuilder.create();


        alertDialog2.show();

        TextView AllEvents=(TextView)promptsView.findViewById(R.id.one);
        AllEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                alertDialog2.cancel();
                setVisiblitiesDomain();
                posts.clear();
                mAdapter.notifyDataSetChanged();
                fetch();



            }
        });

        TextView LikedEvents=(TextView)promptsView.findViewById(R.id.two);
        LikedEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                alertDialog2.cancel();
                setVisiblitiesDomain();
                posts.clear();
                mAdapter.notifyDataSetChanged();
                fetch();


            }
        });

        TextView SubscribedClubs=(TextView)promptsView.findViewById(R.id.three);
        SubscribedClubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                alertDialog2.cancel();
                setVisiblitiesDomain();
                posts.clear();
                mAdapter.notifyDataSetChanged();
                fetch();


            }
        });











    }

    private void domain (){


        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.domain_filter, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());


        alertDialogBuilder.setView(promptsView);


        final AlertDialog alertDialog2 = alertDialogBuilder.create();


        alertDialog2.show();
        TextView Entrepreneurship=(TextView)promptsView.findViewById(R.id.one);
        Entrepreneurship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                alertDialog2.cancel();

                //move it to the fetch code
                //domain_category.setText("Entrepreneurship");
                setVisiblitiesFilter();
               /* domain_head_2.setVisibility(View.VISIBLE);
                domain_head_1.setVisibility(View.INVISIBLE);*/


            }
        });

        TextView None=(TextView)promptsView.findViewById(R.id.zero);
        None.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                alertDialog2.cancel();

                //move it to the fetch code
                setVisiblitiesFilter();
               /* domain_head_1.setVisibility(View.VISIBLE);
                domain_head_2.setVisibility(View.INVISIBLE);*/


            }
        });











    }

    private void setVisiblitiesDomain(){

        /*domain_head_2.setVisibility(View.INVISIBLE);
        domain_head_1.setVisibility(View.VISIBLE);*/


    }

    private void setVisiblitiesFilter(){
        /*filter_head_1.setVisibility(View.VISIBLE);
        filter_head_2.setVisibility(View.INVISIBLE);*/

    }



    private void openLocationSearchWindow(){

        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.search_new_location, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);


        final AlertDialog alertDialog2 = alertDialogBuilder.create();

        // show it
        alertDialog2.show();



        EditText location_find=(EditText)promptsView.findViewById(R.id.location_find);
        no_event_happening=(LinearLayout)promptsView.findViewById(R.id.no_event_happening);
        location_find.addTextChangedListener(this);
        location_find.requestFocus();
        InputMethodManager imm = (InputMethodManager)getActivity(). getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);



        ListView listView = (ListView)promptsView.findViewById(R.id.find_location_listview);
        listView.setAdapter(jadapter);


















    }


    private void open_allusers_meanu(final String key,final int position){

        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.newsfeed_allusers_options, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());


        alertDialogBuilder.setView(promptsView);


        final AlertDialog alertDialog2 = alertDialogBuilder.create();


        alertDialog2.show();

        TextView report=(TextView)promptsView.findViewById(R.id.report);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Toast.makeText(getActivity(),"This feature is not working currently",Toast.LENGTH_LONG).show();

                alertDialog2.cancel();


            }
        });

        TextView share=(TextView)promptsView.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shareEvent(key);

                alertDialog2.cancel();
            }
        });





    }
    private void open_admin_menu(final String key,final int position){
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.newsfeed_admin_options, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);


        final AlertDialog alertDialog2 = alertDialogBuilder.create();

        // show it
        alertDialog2.show();

        TextView delete=(TextView)promptsView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteblog(key,position);
                alertDialog2.cancel();


            }
        });

        TextView edit=(TextView)promptsView.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog2.cancel();
            }
        });

        TextView share=(TextView)promptsView.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                shareEvent(key);

                alertDialog2.cancel();
            }
        });




    }

    private void deleteblog(final String key,final int position) {
        FirebaseDatabase  events_database = FirebaseDatabase.getInstance("https://ddrx-172d3-64f25.firebaseio.com/");

        final DatabaseReference events_reference = events_database.getReference();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        final AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
        alertDialogBuilder.setTitle("Attention !")
                .setMessage("Are you sure you want to delete this post?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        events_reference.child(key).removeValue();
                        posts.remove(position);
                        mAdapter.notifyDataSetChanged();

                      Toast.makeText(getActivity(),"Event Deleted",Toast.LENGTH_LONG).show();


                        alertDialog.cancel();

                    }
                })










                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                alertDialog.cancel();

                            }
                        })

                // create alert dialog
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();




    }




    class NewsFeedAdapter extends RecyclerView.Adapter<com.example.androdev.ddxr.UserFragments.FragmentNewsFeed.NewsFeedAdapter.MyViewHolder>   {

        private List<MyPosts> posts;
        private Context context;





        public  class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            CardView parentCardView;

            TextView ClubName;
            TextView ClubLocation;
            CircleImageView ClubProfileImage;
            TextView EventLocation;
            ImageView EventBanner;
            TextView EventTypeX;
            TextView ClubDomain;
            TextView update_date;
            TextView EventName;
            ImageView LikeButton;
            RelativeLayout views;
            TextView InterestedCount;
            FrameLayout InterestedPhotosFL;
            CircleImageView InterestedPhoto2;
            CircleImageView InterestedPhoto1;


            TextView Event_Dates;
            TextView PriceSegment;
            ImageView bookmark;
            LinearLayout GetDetails;
            ProgressBar progressBar;
            LinearLayout TextLayout;
            TextView InterestedTextView;
            ImageView Discuss;


           // TextView EventDescription;
            ImageView Options;
            public MyViewHolder(View v) {
                super(v);
                parentCardView=(CardView)v.findViewById(R.id.parent_card_view) ;

                ClubName = (TextView)v.findViewById(R.id.club_name);
                parentCardView.setVisibility(View.INVISIBLE);
                Discuss=v.findViewById(R.id.discuss) ;
                bookmark=(ImageView) v.findViewById(R.id.bookmark);
                EventTypeX=(TextView) v.findViewById(R.id.event_typex);
                ClubDomain=(TextView)v.findViewById(R.id.club_domain);
                EventLocation=(TextView) v.findViewById(R.id.location);
                ClubLocation=(TextView) v.findViewById(R.id.club_location);
                LikeButton=(ImageView) v.findViewById(R.id.like_button);
                views=(RelativeLayout) v.findViewById(R.id.views);
                InterestedCount=(TextView) v.findViewById(R.id.interested_count);
                InterestedPhotosFL=(FrameLayout) v.findViewById(R.id.interested_photos);
                GetDetails=(LinearLayout) v.findViewById(R.id.GetDetails);
                TextLayout=(LinearLayout) v.findViewById(R.id.text_layout);
                PriceSegment=(TextView) v.findViewById(R.id.event_cost);
                InterestedTextView=(TextView) v.findViewById(R.id.interested_textview);




                Options=(ImageView) v.findViewById(R.id.options);
                ClubProfileImage=(CircleImageView) v.findViewById(R.id.club_profile_image);

                EventBanner=(ImageView)v.findViewById(R.id.event_banner);
              //  UpdatedDay=(TextView)v.findViewById(R.id.update_day);
                //  update_date=(TextView)v.findViewById(R.id.update_day);
              //  mShimmerFrameLayout=(ShimmerFrameLayout) v.findViewById(R.id.shimmer_view_container);
                EventName=(TextView)v.findViewById(R.id.event_name);
                Event_Dates=(TextView)v.findViewById(R.id.event_dates);
                InterestedPhoto2=(CircleImageView)v.findViewById(R.id.interested_photo2);
                InterestedPhoto1=(CircleImageView)v.findViewById(R.id.interested_photo1);
               // EventDescription=(TextView)v.findViewById(R.id.event_description);


            }

        }

        // Provide a suitable constructor (depends on the kind of dataset)

        public NewsFeedAdapter(Context context,List<MyPosts> posts) {
            this.context=context;
            this.posts=posts;

        }

        // Create new views (invoked by the layout manager)
        @Override
        public com.example.androdev.ddxr.UserFragments.FragmentNewsFeed.NewsFeedAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                                       int viewType) {
            // create a new view
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.demo,parent,false);
            com.example.androdev.ddxr.UserFragments.FragmentNewsFeed.NewsFeedAdapter.MyViewHolder vh = new com.example.androdev.ddxr.UserFragments.FragmentNewsFeed.NewsFeedAdapter.MyViewHolder(v);
            return vh;
        }


        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(@NonNull final com.example.androdev.ddxr.UserFragments.FragmentNewsFeed.NewsFeedAdapter.MyViewHolder holder, final int position) {

            FirebaseDatabase  events_database = FirebaseDatabase.getInstance("https://ddrx-events-172d3.firebaseio.com/");


            final DatabaseReference events_reference = events_database.getReference();
            final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
            final MyPosts c = posts.get(position);

            holder.InterestedCount.setText(""+c.getInterestedCount());
            if(c.getInterestedCount()==0){

                holder.InterestedPhotosFL.setVisibility(View.GONE);

            }

            holder.InterestedPhotosFL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(c.getInterestedCount()!=0) {
                        Intent intent = new Intent(getActivity(), InterestedPeople.class);
                        intent.putExtra("NewsFeedKey", c.getKey());
                        // intent.putExtra("InterestedCountX",5);
                        Log.e("MMS", "" + c.getInterestedCount());
                        startActivity(intent);

                    }
                }
            });
            holder.InterestedCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(c.getInterestedCount()!=0) {
                        Intent intent = new Intent(getActivity(), InterestedPeople.class);
                        intent.putExtra("NewsFeedKey", c.getKey());
                        // intent.putExtra("InterestedCountX",5);
                        Log.e("MMS", "" + c.getInterestedCount());
                        startActivity(intent);

                    }

                }
            });

            holder.InterestedTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(c.getInterestedCount()!=0) {
                        Intent intent = new Intent(getActivity(), InterestedPeople.class);
                        intent.putExtra("NewsFeedKey", c.getKey());
                        // intent.putExtra("InterestedCountX",5);
                        Log.e("MMS", "" + c.getInterestedCount());
                        startActivity(intent);

                    }

                }
            });

            if(c.getInterestedCount()==1){

                holder.InterestedPhotosFL.setVisibility(View.VISIBLE);
                holder.InterestedPhoto1.setVisibility(View.VISIBLE);

                holder.InterestedPhoto2.setVisibility(View.GONE);

                if(c.getInterestedPhoto().size()>0&&c.getInterestedPhoto().get(0)!=null){
                    Log.e("gg","hii "+c.getInterestedPhoto().get(0));
                    Picasso.get()
                            .load(Uri.parse(c.getInterestedPhoto().get(0)))
                            .noFade()
                            .into(holder.InterestedPhoto1);


                }


            }

            holder.GetDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getActivity(), EventDetail.class);
                    intent.putExtra("Key",c.getKey());
                    intent.putExtra("position",position);
                    startActivity(intent);
                }
            });

            if(c.getInterestedCount()>1){

                holder.InterestedPhotosFL.setVisibility(View.VISIBLE);

                holder.InterestedPhoto2.setVisibility(View.VISIBLE);
                if(c.getInterestedPhoto().size()>0&&c.getInterestedPhoto().get(0)!=null){
                    Picasso.get()
                            .load(Uri.parse(c.getInterestedPhoto().get(0)))
                            .noFade()
                            .into(holder.InterestedPhoto1);

                    if(c.getInterestedPhoto().size()>1&&c.getInterestedPhoto().get(1)!=null){
                        Picasso.get()
                                .load(Uri.parse(c.getInterestedPhoto().get(1)))
                                .noFade()
                                .into(holder.InterestedPhoto2);

                    }



                }



            }



            holder.bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(c.getBookmark()==null||c.getBookmark().equals("false")){
                        c.setBookmark("true");

                        int bCount=c.getBookmarkCount();
                        bCount=bCount+1;
                        c.setBookmarkCount(bCount);
                        events_reference.child(c.getKey()).child("BookmarkCount").setValue(""+(bCount));

                        if(c.getLike()==null||c.getLike().equals("false")){
                            events_reference.child(c.getKey()).child("Interested").child(user.getUid()).child("Status").setValue("true");


                            events_reference.child(c.getKey()).child("Interested").child(user.getUid()).child("ImageUrl").setValue(MyImageURL);
                            c.setInterestedCount(c.getInterestedCount()+1);
                            if(c.getInterestedCount()==1){

                                events_reference.child(c.getKey()).child("InterestedPerson1").setValue(MyImageURL);
                                c.getInterestedPhoto().add(0,MyImageURL);
                            }
                            if(c.getInterestedCount()==2){

                                events_reference.child(c.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.hasChild("InterestedPerson1")){
                                            dataSnapshot.getRef().child("InterestedPerson2").setValue(MyImageURL);
                                            c.getInterestedPhoto().add(1,MyImageURL);
                                        }
                                        else{
                                            dataSnapshot.getRef().child("InterestedPerson1").setValue(MyImageURL);
                                            c.getInterestedPhoto().add(1,MyImageURL);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }

                        }
                        events_reference.child(c.getKey()).child("Interested").child(user.getUid()).child("Bookmark").setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getActivity(),"Added to bookmarks",Toast.LENGTH_SHORT).show();

                            }
                        });


                    }
                    else{
                        c.setBookmark("false");
                        int bCount=c.getBookmarkCount();
                        bCount=bCount-1;
                        c.setBookmarkCount(bCount);
                        events_reference.child(c.getKey()).child("BookmarkCount").setValue(""+(bCount));

                        if(c.getLike()==null||c.getLike().equals("false")){


                            events_reference.child(c.getKey()).child("Interested").child(user.getUid()).removeValue();
                            c.setInterestedCount(c.getInterestedCount()-1);
                          //  if(c.getInterestedCount()==1){
                            if(c.getInterestedPhoto().size()>0&&c.getInterestedPhoto().get(0).equals(MyImageURL)){
                                events_reference.child(c.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.child("InterestedPerson1").getValue(String.class)!=null){
                                            if(dataSnapshot.child("InterestedPerson1").getValue(String.class).equals(MyImageURL)){
                                                events_reference.child(c.getKey()).child("InterestedPerson1").removeValue();
                                            }
                                        }
                                         if(dataSnapshot.child("InterestedPerson2").getValue(String.class)!=null){
                                            if(dataSnapshot.child("InterestedPerson2").getValue(String.class).equals(MyImageURL)){
                                                events_reference.child(c.getKey()).child("InterestedPerson2").removeValue();
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                             //   events_reference.child(c.getKey()).child("InterestedPerson1").removeValue();
                                c.getInterestedPhoto().remove(0);

                            }
                            else if(c.getInterestedPhoto().size()>1&&c.getInterestedPhoto().get(1).equals(MyImageURL)){
                                events_reference.child(c.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.child("InterestedPerson1").getValue(String.class)!=null){
                                            if(dataSnapshot.child("InterestedPerson1").getValue(String.class).equals(MyImageURL)){
                                                events_reference.child(c.getKey()).child("InterestedPerson1").removeValue();
                                            }
                                        }
                                         if(dataSnapshot.child("InterestedPerson2").getValue(String.class)!=null){
                                            if(dataSnapshot.child("InterestedPerson2").getValue(String.class).equals(MyImageURL)){
                                                events_reference.child(c.getKey()).child("InterestedPerson2").removeValue();
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                c.getInterestedPhoto().remove(1);
                            }
                               /* events_reference.child(c.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.child("InterestedPerson1").getValue(String.class).equals(MyImageURL)){
                                            events_reference.child(c.getKey()).child("InterestedPerson1").removeValue();
                                            c.getInterestedPhoto().remove(0);

                                        }
                                        else{
                                            events_reference.child(c.getKey()).child("InterestedPerson2").removeValue();
                                            c.getInterestedPhoto().remove(1);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });*/
                               /* if(events_reference.child(c.getKey()).child("InterestedPerson2").equals(MyImageURL)){

                                }
                                events_reference.child(c.getKey()).child("InterestedPerson2").removeValue();
                                c.getInterestedPhoto().remove(1);*/

                            /*if(c.getInterestedCount()==0){
                                events_reference.child(c.getKey()).child("InterestedPerson1").removeValue();
                                c.getInterestedPhoto().remove(0);
                            }*/

                        }
                        else{
                            events_reference.child(c.getKey()).child("Interested").child(user.getUid()).child("Bookmark").setValue("false");
                        }

                    }

                    mAdapter.notifyDataSetChanged();



                }
            });

            if(c.getBookmark()==null||c.getBookmark().equals("false")){

                holder.bookmark.setBackgroundResource(R.drawable.ic_bookmark_white);
            }
            else{

                holder.bookmark.setBackgroundResource(R.drawable.ic_bookmark_red);


            }


            holder.LikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(c.getLike()==null||c.getLike().equals("false")){
                        c.setLike("true");
                        int lCount=c.getLikeCount();
                        lCount=lCount+1;
                        c.setLikeCount(lCount);
                        events_reference.child(c.getKey()).child("LikeCount").setValue(""+(lCount));

                        if(c.getBookmark()==null||c.getBookmark().equals("false")){



                            events_reference.child(c.getKey()).child("Interested").child(user.getUid()).child("Status").setValue("true");
                            events_reference.child(c.getKey()).child("Interested").child(user.getUid()).child("ImageUrl").setValue(MyImageURL);
                            c.setInterestedCount(c.getInterestedCount()+1);
                            if(c.getInterestedCount()==1){
                                events_reference.child(c.getKey()).child("InterestedPerson1").setValue(MyImageURL);
                                c.getInterestedPhoto().add(0,MyImageURL);
                            }
                            if(c.getInterestedCount()==2){
                                events_reference.child(c.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.hasChild("InterestedPerson1")){
                                            dataSnapshot.getRef().child("InterestedPerson2").setValue(MyImageURL);
                                            c.getInterestedPhoto().add(1,MyImageURL);
                                        }
                                        else{
                                            dataSnapshot.getRef().child("InterestedPerson1").setValue(MyImageURL);
                                            c.getInterestedPhoto().add(1,MyImageURL);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                              //  events_reference.child(c.getKey()).child("InterestedPerson2").setValue(MyImageURL);
                                //c.getInterestedPhoto().add(1,MyImageURL);
                            }

                          /*  c.setInterested("true");
                            if(c.getInterestedPhoto().size()<2){
                                c.setInterestedPhoto(c.getInterestedPhoto().add(MyImageURL));

                            }*/

                        }
                        events_reference.child(c.getKey()).child("Interested").child(user.getUid()).child("Like").setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                              //  Toast.makeText(getActivity(),"Added to bookmarks",Toast.LENGTH_SHORT).show();

                            }
                        });



                    }
                    else{
                        c.setLike("false");
                        int lCount=c.getLikeCount();
                        lCount=lCount-1;
                        c.setLikeCount(lCount);
                        events_reference.child(c.getKey()).child("LikeCount").setValue(""+(lCount));

                        if(c.getBookmark()==null||c.getBookmark().equals("false")){
                           // events_reference.child(c.getKey()).child("Interested").child(user.getUid()).child("Status").setValue("false");
                            events_reference.child(c.getKey()).child("Interested").child(user.getUid()).removeValue();
                            c.setInterestedCount(c.getInterestedCount()-1);
                           /* if(c.getInterestedCount()==1){
                                events_reference.child(c.getKey()).child("InterestedPerson2").removeValue();
                                c.getInterestedPhoto().remove(1);
                            }
                            if(c.getInterestedCount()==0){
                                events_reference.child(c.getKey()).child("InterestedPerson1").removeValue();
                                c.getInterestedPhoto().remove(0);
                            }*/



                           if(c.getInterestedPhoto().size()>0&&c.getInterestedPhoto().get(0).equals(MyImageURL)){
                               Log.e("Kex10","1");
                               events_reference.child(c.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                       if(dataSnapshot.child("InterestedPerson1").getValue(String.class)!=null){
                                           Log.e("Kex1","1");
                                           if(dataSnapshot.child("InterestedPerson1").getValue(String.class).equals(MyImageURL)){
                                               Log.e("Kex2","2");
                                               events_reference.child(c.getKey()).child("InterestedPerson1").removeValue();
                                           }
                                       }
                                        if(dataSnapshot.child("InterestedPerson2").getValue(String.class)!=null){
                                           Log.e("Kex3","1");
                                           if(dataSnapshot.child("InterestedPerson2").getValue(String.class).equals(MyImageURL)){
                                               Log.e("Kex4","1");
                                               events_reference.child(c.getKey()).child("InterestedPerson2").removeValue();
                                           }
                                       }

                                   }

                                   @Override
                                   public void onCancelled(@NonNull DatabaseError databaseError) {

                                   }
                               });
                               c.getInterestedPhoto().remove(0);
                           }
                           if(c.getInterestedPhoto().size()>1){
                               Log.e("Kex11","1");
                               if(c.getInterestedPhoto().get(1)!=null&&c.getInterestedPhoto().get(1).equals(MyImageURL)){
                                   Log.e("Kex12","1");
                                   events_reference.child(c.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                           if(dataSnapshot.child("InterestedPerson1").getValue(String.class)!=null){
                                               Log.e("Kex5","1");

                                               if(dataSnapshot.child("InterestedPerson1").getValue(String.class).equals(MyImageURL)){
                                                   Log.e("Kex6","1");
                                                   events_reference.child(c.getKey()).child("InterestedPerson1").removeValue();
                                               }

                                           }

                                            if(dataSnapshot.child("InterestedPerson2").getValue(String.class)!=null){
                                               Log.e("Kex7","1");
                                               if(dataSnapshot.child("InterestedPerson2").getValue(String.class).equals(MyImageURL)){
                                                   Log.e("Kex8","1");
                                                   events_reference.child(c.getKey()).child("InterestedPerson2").removeValue();
                                               }
                                           }


                                       }

                                       @Override
                                       public void onCancelled(@NonNull DatabaseError databaseError) {

                                       }
                                   });
                                   c.getInterestedPhoto().remove(1);
                               }
                           }

                          /*  events_reference.child(c.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if(dataSnapshot.child("InterestedPerson1").getValue(String.class).equals(MyImageURL)){
                                        events_reference.child(c.getKey()).child("InterestedPerson1").removeValue();
                                        c.getInterestedPhoto().remove(0);

                                    }
                                    else  if(dataSnapshot.child("InterestedPerson2").getValue(String.class).equals(MyImageURL)){
                                        events_reference.child(c.getKey()).child("InterestedPerson2").removeValue();
                                        c.getInterestedPhoto().remove(1);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });*/

                        }
                        else{

                            events_reference.child(c.getKey()).child("Interested").child(user.getUid()).child("Like").setValue("false");

                        }



                    }

                    mAdapter.notifyDataSetChanged();

                }
            });

            final Resources r = getActivity().getResources();
            int px = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    7,
                    r.getDisplayMetrics()
            );



            int px1 = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    3,
                    r.getDisplayMetrics()
            );

            int px2 = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    10,
                    r.getDisplayMetrics()
            );


            if(c.getLike()==null||c.getLike().equals("false")){

                holder.LikeButton.setBackgroundResource(R.drawable.ic_heart_white);

            }
            else{

                holder.LikeButton.setBackgroundResource(R.drawable.ic_heart_red);

            }

            holder.ClubName.setText(c.getClubName());

            holder.ClubLocation.setText(c.getClubLocation());
            holder.EventName.setText(c.getEventName());
            holder.EventTypeX.setText("( "+c.getEventTypeX()+" )");
            holder.PriceSegment.setText(c.getPriceSegment());

            holder.Event_Dates.setText(c.getEvent_Dates());
            if(c.getEventType()!=null&&c.getEventType().equals("Online")){
                holder.EventLocation.setText("Online");
            }
            else{
                holder.EventLocation.setText(c.getEventLocation());
            }

            holder.ClubDomain.setText(c.getClubDomain());
            holder.parentCardView.setVisibility(View.VISIBLE);
            holder.Discuss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getActivity(),EventDiscussions.class);
                    intent.putExtra("EventKey",c.getKey());
                    intent.putExtra("EventName",c.getEventName());
                    startActivity(intent);

                }
            });

            holder.ClubName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String uid=c.getUploadedBy();
                    Intent intent = new Intent(context, ClubDetails.class);
                    intent.putExtra("ClubUID", uid);
                    context.startActivity(intent);
                }
            });

            Picasso.get()

                    .load(c.getClub_Image_Url())
                    .placeholder(R.drawable.club_placeholder)
                    .resize(800, 800)
                    .centerCrop()
                    .noFade()
                    .error(R.drawable.club_placeholder) // default image to load
                    .into(holder.ClubProfileImage, new Callback() {
                        @Override
                        public void onSuccess() {




                            holder.ClubProfileImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String uid=c.getUploadedBy();
                                    Intent intent = new Intent(context, ClubDetails.class);
                                    intent.putExtra("ClubUID", uid);
                                    context.startActivity(intent);

                                }
                            });




                            holder.Options.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if(user.getUid().equals(c.getUploadedBy()))
                                    {
                                        open_admin_menu(c.getKey(),position);
                                    }
                                    else{
                                        open_allusers_meanu(c.getKey(),position);

                                    }

                                }

                            });


                        }



                        @Override
                        public void onError(Exception e) {

                        }
                    });








            Transformation transformation = new Transformation() {

                @Override
                public Bitmap transform(Bitmap source) {
                    int targetWidth ;
                    int targetHeight;
                    int phoneHeight;

                    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                    Display display = wm.getDefaultDisplay();
                    DisplayMetrics metrics = new DisplayMetrics();
                    display.getMetrics(metrics);


                    targetWidth=metrics.widthPixels/2;



                    Log.e("Width",""+targetWidth);


                    double aspectRatio = (double) source.getHeight() / (double) source.getWidth();

                    int resultant_width=(int) (targetWidth * aspectRatio);
                    int px7 = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            160,
                            r.getDisplayMetrics()
                    );

                    Log.e("SourceHeight",""+source.getHeight());
                    Log.e("px",""+px7);
                    if(resultant_width<px7){

                        targetHeight=px7;

                    }
                    else{
                        targetHeight = (int) (targetWidth * aspectRatio);
                    }

                    Log.e("Height",""+targetHeight);



                        Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                        if (result != source) {
                            // Same bitmap is returned if sizes are the same
                            source.recycle();
                        }
                        return result;




                }

                @Override
                public String key() {
                    return "transformation" + " desiredWidth";
                }
            };



          /* Picasso.get()
                   .load(R.drawable.image_place_holder)
                  // .transform(transformation)
                   .into(holder.EventBanner, new Callback() {
                       @Override
                       public void onSuccess() {





                       }

                       @Override
                       public void onError(Exception e) {

                       }
                   });



*/




            Picasso.get()
                   // .load(R.drawable.image_place_holder)
                    .load(c.getEvent_Banner())
                    .error(R.drawable.blank_image2)
                    .transform(transformation)
                    .noFade()
                    .placeholder(R.drawable.blank_image2)
                    .into(holder.EventBanner, new Callback() {
                        @Override
                        public void onSuccess() {
                            swipeRefreshLayout.setRefreshing(false);

                          /*  holder.EventBanner.post(new Runnable() {
                                @Override
                                public void run() {
                                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.TextLayout.getLayoutParams();
                                    params.gravity = Gravity.CENTER;
                                    holder.TextLayout.setLayoutParams(params);
                                    holder.TextLayout.setGravity(Gravity.CENTER_HORIZONTAL);
                                }
                            });*/



                        }

                        @Override
                        public void onError(Exception e) {


                            //holder.EventBanner.setImageResource(R.drawable.blank_image);

                            //Toast.makeText(context, "Failed to load new posts", Toast.LENGTH_SHORT).show();

                        }


                    });




        }
        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {



            return posts.size();
        }

        public  int getCount(){

            return  posts.size();

        }



    }





//    public  void getInterestedPhotos(final FirebaseCallBackX2 firebaseCallBackX2,String KeyX){
//
//        final ArrayList<String> InterestedPhotoS=new ArrayList<>();
//
//
//
//
//
//
//
//
//        final Query query=events_reference.child(KeyX).child("Interested").getRef().limitToLast(1);
//
//
//       /* query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//
//                InterestedPhotoS.add(dataSnapshot.child("ImageUrl").getValue(String.class));
//
//                Log.e("ChildListener1",""+InterestedPhotoS.get(0));
//
//                firebaseCallBackX2.onCallBack(InterestedPhotoS);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//*/
//
//        query.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//
//                InterestedPhotoS.add(dataSnapshot.child("ImageUrl").getValue(String.class));
//
//                Log.e("ChildListener1",""+InterestedPhotoS.get(0));
//
//                firebaseCallBackX2.onCallBack(InterestedPhotoS);
//
//
//
//
//                /*if(InterestedPhotoS.get(1)!=null){
//                    Log.e("ChildListener2",""+InterestedPhotoS.get(1));
//                    firebaseCallBackX2.onCallBack(InterestedPhotoS);
//                }*/
//
//
//
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//}











    public void getProfileImage(final FirebaseCallbackX firebaseCallbackX){

        final String[] MyImageURLS = new String[1];

        parentReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user.getUid())){
                    MyImageURLS[0] =dataSnapshot.child(user.getUid()).child("ImageUrl").getValue(String.class);
                    Log.e("url2","hii"+MyImageURLS[0]);
                    firebaseCallbackX.onCallBack(MyImageURLS[0]);
                }
                else{

                    Log.e("url2","hii"+MyImageURLS[0]);

                    adminReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(user.getUid())){
                                MyImageURLS[0] =dataSnapshot.child(user.getUid()).child("ProfileImageUrl").getValue(String.class);
                                firebaseCallbackX.onCallBack(MyImageURLS[0]);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








    }

    public void shareEvent(String EventKey){


                final Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);


                String uri="http://hungryhunter96.github.io/share_event?Key="+EventKey;


                Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setLink(Uri.parse(uri))
                        .setDomainUriPrefix("https://ddxr.page.link")
                        .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())

                        .buildShortDynamicLink()
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<ShortDynamicLink>() {
                            @Override
                            public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                                if (task.isSuccessful()) {
                                    //  progress_edit_delete.setVisibility(View.GONE);

                                    // Toast.makeText(EventDetail.this,"Link Copied",Toast.LENGTH_SHORT).show();
                                    String thiseventurl;
                                    Uri shortLink = task.getResult().getShortLink();
                                    thiseventurl=shortLink.toString();
                                    sendIntent.putExtra(Intent.EXTRA_TEXT, shortLink.toString());
                                    sendIntent.setType("text/plain");
                                    startActivity(sendIntent);
                                        /*Uri flowchartLink = task.getResult().getPreviewLink();

                                        ClipboardManager clipboard = (ClipboardManager) EventDetail.this.getSystemService(Context.CLIPBOARD_SERVICE);
                                        ClipData clip = ClipData.newPlainText(null, shortLink.toString());
                                        if (clipboard == null) return;
                                        clipboard.setPrimaryClip(clip);*/

                                } else {

                                   // Toast.makeText(EventDetail.this,"Link Copy Failure",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

















    }


    @Override
    public void onResume() {
        super.onResume();
        myPrefs2 = getActivity().getSharedPreferences("myPrefs2", Context.MODE_PRIVATE);
        int prefEventKey = myPrefs2.getInt("EventPos", -2);
        String prefLike = myPrefs2.getString("Like", "hello");
        String prefBookmark= myPrefs2.getString("Bookmark", "hello");
        String hii=myPrefs2.getString("Hii","nullh");
        String prefInterestedCountInit = myPrefs2.getString("InterestedCountInit", "null");
        String prefInterestedCountFinal = myPrefs2.getString("InterestedCountFinal", "null");
        String prefMyImageURL = myPrefs2.getString("MyImageURL", "hello");
        int likeC=myPrefs2.getInt("LikeCount",0);
        int bookmarkC=myPrefs2.getInt("BookmarkCount",0);
        Log.e("hexx","WW"+hii);


        //Toast.makeText(getActivity(),"WW "+hii,Toast.LENGTH_SHORT).show();

        Log.e("PrefEventKey",""+prefEventKey);
        Log.e("PrefLike",""+prefLike);
        Log.e("PrefBookmark",""+prefBookmark);


        //int count=posts.get
        if(prefEventKey!=-2) {
            if (posts.size() > prefEventKey) {

                if (!prefLike.equals("hello")) {
                    posts.get(prefEventKey).setLike(prefLike);
                    //mAdapter.notifyDataSetChanged();
                }
                if (!prefBookmark.equals("hello")) {
                    posts.get(prefEventKey).setBookmark(prefBookmark);
                    // mAdapter.notifyDataSetChanged();
                }
                if (!prefInterestedCountFinal.equals("null")) {
                    posts.get(prefEventKey).setInterestedCount(Integer.parseInt(prefInterestedCountFinal));
                    // mAdapter.notifyDataSetChanged();
                }
                posts.get(prefEventKey).setLikeCount(likeC);
                posts.get(prefEventKey).setBookmarkCount(bookmarkC);
                if ((prefLike.equals("true") || prefBookmark.equals("true")) ) {

                    if(posts.get(prefEventKey).getInterestedPhoto().size()==0) {
                       // if (!posts.get(prefEventKey).getInterestedPhoto().get(0).equals(MyImageURL)) {
                            posts.get(prefEventKey).getInterestedPhoto().add(MyImageURL);
                       // }
                    }

                    if(posts.get(prefEventKey).getInterestedPhoto().size()>0) {
                        if (!posts.get(prefEventKey).getInterestedPhoto().get(0).equals(MyImageURL)) {
                            posts.get(prefEventKey).getInterestedPhoto().add(MyImageURL);
                        }
                    }
                    if(posts.get(prefEventKey).getInterestedPhoto().size()>1){
                               if (!posts.get(prefEventKey).getInterestedPhoto().get(1).equals(MyImageURL)){

                                   posts.get(prefEventKey).getInterestedPhoto().add(MyImageURL);

                               }

                           }

                }
                if ((!prefLike.equals("true")) && (!prefBookmark.equals("true")) ) {

                    if(posts.get(prefEventKey).getInterestedPhoto().size()>0) {
                        if (posts.get(prefEventKey).getInterestedPhoto().get(0).equals(MyImageURL)) {
                            posts.get(prefEventKey).getInterestedPhoto().remove(0);
                        }
                    }
                    if(posts.get(prefEventKey).getInterestedPhoto().size()>1){
                        if (posts.get(prefEventKey).getInterestedPhoto().get(1).equals(MyImageURL)){

                            posts.get(prefEventKey).getInterestedPhoto().remove(1);

                        }

                    }

                }
                mAdapter.notifyDataSetChanged();


            }

        }





       /* if(prefName!=-2){

        }*/

        // t.setText(prefName);
    }











}

