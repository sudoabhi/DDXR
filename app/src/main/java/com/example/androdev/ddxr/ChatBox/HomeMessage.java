package com.example.androdev.ddxr.ChatBox;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.androdev.ddxr.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeMessage extends AppCompatActivity {


    RecyclerView recyclerView;
    HomeMessageAdapter homeMessageAdapter;
    LinearLayoutManager mLayoutManager;
    ArrayList<HomeMessageObject> homeMessageObjects;
    FirebaseUser user;
    String todays_date;
    ProgressBar progressBar;
    TextView noMessages;


    ArrayList<HomeMessageObject> RecentChats=new ArrayList<>();
    String time;
    String date;
    String profile_image_url;
    String user_name;
    Boolean work;
    int count;
    String userUID;
    String message;
    ArrayList<String> DuplicateClearance=new ArrayList<>();
    FirebaseDatabase messageDatabase;
    DatabaseReference messageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_message);
        user = FirebaseAuth.getInstance().getCurrentUser();
        work=true;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        progressBar=(ProgressBar) findViewById(R.id.progress_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Messages");



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        homeMessageObjects = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(HomeMessage.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);


        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        noMessages=(TextView) findViewById(R.id.no_messages);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);

        homeMessageAdapter = new HomeMessageAdapter(HomeMessage.this, homeMessageObjects);
        recyclerView.setAdapter(homeMessageAdapter);
     /*   setUpItemTouchHelper();
        setUpAnimationDecoratorHelper();*/

        messageDatabase = FirebaseDatabase.getInstance("https://messagelist-6b6d2.firebaseio.com/");
        messageReference = messageDatabase.getReference();

        RecentChats.clear();
         count = 0;




      final Query recentChats= messageReference.child(user.getUid()).orderByKey().limitToLast(200);
    //    final Query recentChats= messageReference.child(user.getUid()).orderByKey();
        recentChats.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


               count = 0;

               final String receiver = dataSnapshot.child("OtherParty").getValue(String.class);
               String date = dataSnapshot.child("Date").getValue(String.class);
               final String otherpartyname = dataSnapshot.child("OtherPartyName").getValue(String.class);
               final String otherpartyprofileiamge = dataSnapshot.child("OtherPartyProfileImage").getValue(String.class);
               final String text = dataSnapshot.child("Text").getValue(String.class);
               String texttype = dataSnapshot.child("TextType").getValue(String.class);
               final String time = dataSnapshot.child("Time").getValue(String.class);
               final String sendername = dataSnapshot.child("MyName").getValue(String.class);
               getTodaysDate();


               final String user_image_url = dataSnapshot.child("UserImageUrl").getValue(String.class);


               Log.e("receiver0", "" + receiver);
               Log.e("otherpartyname0", "" + otherpartyname);
               Log.e("otherpartyprofileimage0", "" + otherpartyprofileiamge);
               Log.e("text0", "" + text);
               Log.e("time0", "" + time);
               Log.e("sendername0", "" + sendername);
               Log.e("userimageurl0", "" + user_image_url);
               Log.e("userUID0", "" + userUID);



               if (receiver == null && otherpartyname == null && otherpartyprofileiamge == null && text == null && time == null && sendername == null && user_image_url == null) {
                   userUID = null;
               } else {
                   userUID = user.getUid();
                   Log.e("userUID", "" +userUID);}

                   if(userUID!=null){

                   int n = RecentChats.size();

                   if (RecentChats.size() > 0) {


                       for (int i = 0; i < n; i++) {

                           Log.e("hello", "mummy");
                           Log.e("check", "" + dataSnapshot.child("OtherParty").getValue(String.class));

                           if (RecentChats.get(i).other_party != null) {


                               if (!RecentChats.get(i).other_party.equals(receiver)) {


                                   count = count + 1;
                                   if (count == RecentChats.size()) {


                                       Log.e("hello", "mummy" + RecentChats.size());

                                       if(date.equals(todays_date)){

                                           RecentChats.add(new HomeMessageObject(sendername, text, time, otherpartyprofileiamge, receiver, user.getUid(), otherpartyname, user_image_url));


                                       }
                                       else{
                                           RecentChats.add(new HomeMessageObject(sendername, text, date, otherpartyprofileiamge, receiver, user.getUid(), otherpartyname, user_image_url));

                                       }




                                       homeMessageObjects.add(new HomeMessageObject(RecentChats.get(RecentChats.size() - 1).receiver_name, RecentChats.get(RecentChats.size() - 1).recent_text, RecentChats.get(RecentChats.size() - 1).time, RecentChats.get(RecentChats.size() - 1).image_url, RecentChats.get(RecentChats.size() - 1).other_party, RecentChats.get(RecentChats.size() - 1).sender_uid, RecentChats.get(RecentChats.size() - 1).sender_name, RecentChats.get(RecentChats.size() - 1).sender_image_url));
                                       homeMessageAdapter.notifyDataSetChanged();


                                   } else {
                                       Log.e("hiisam", "" + i);
                                  /* RecentChats.remove(i);
                                   RecentChats.add(receiver);
                                   homeMessageObjects.add(new HomeMessageObject(RecentChats.get(RecentChats.size()-1), "hii", "hii", "hii"));
                                   homeMessageAdapter.notifyDataSetChanged();
*/


                                   }
                               }
                               if (RecentChats.get(i).other_party.equals(receiver)) {

                                  /* Log.e("T be delted", "" + RecentChats.get(i).other_party);
                                   Log.e("otherpartyname", otherpartyname);
                                   Log.e("time", time);
                                   Log.e("text", text);
                                   Log.e("otherpartyprofileimage", otherpartyprofileiamge);
                                   Log.e("receiver", receiver);
                                   Log.e("user", user.getUid());
                                   Log.e("sendername", sendername);
                                   Log.e("userimageurl", user_image_url);*/
                                   // Log.e("otherpartyname",otherpartyname);

                                   if (RecentChats.size() - 1 != i) {

                                       RecentChats.remove(i);
                                       if(date.equals(todays_date)){

                                           RecentChats.add(new HomeMessageObject(sendername, text, time, otherpartyprofileiamge, receiver, user.getUid(), otherpartyname, user_image_url));


                                       }
                                       else{
                                           RecentChats.add(new HomeMessageObject(sendername, text, date, otherpartyprofileiamge, receiver, user.getUid(), otherpartyname, user_image_url));

                                       }

                                       homeMessageObjects.add(new HomeMessageObject(RecentChats.get(RecentChats.size() - 1).receiver_name, RecentChats.get(RecentChats.size() - 1).recent_text, RecentChats.get(RecentChats.size() - 1).time, RecentChats.get(RecentChats.size() - 1).image_url, RecentChats.get(RecentChats.size() - 1).other_party, RecentChats.get(RecentChats.size() - 1).sender_uid, RecentChats.get(RecentChats.size() - 1).sender_name, RecentChats.get(RecentChats.size() - 1).sender_image_url));
                                       homeMessageObjects.remove(i);

                                       homeMessageAdapter.notifyDataSetChanged();

                                   } else {

                                       RecentChats.remove(i);
                                       if(date.equals(todays_date)){

                                           RecentChats.add(new HomeMessageObject(sendername, text, time, otherpartyprofileiamge, receiver, user.getUid(), otherpartyname, user_image_url));


                                       }
                                       else{
                                           RecentChats.add(new HomeMessageObject(sendername, text, date, otherpartyprofileiamge, receiver, user.getUid(), otherpartyname, user_image_url));

                                       }

                                       homeMessageObjects.add(new HomeMessageObject(RecentChats.get(RecentChats.size() - 1).receiver_name, RecentChats.get(RecentChats.size() - 1).recent_text, RecentChats.get(RecentChats.size() - 1).time, RecentChats.get(RecentChats.size() - 1).image_url, RecentChats.get(RecentChats.size() - 1).other_party, RecentChats.get(RecentChats.size() - 1).sender_uid, RecentChats.get(RecentChats.size() - 1).sender_name, RecentChats.get(RecentChats.size() - 1).sender_image_url));
                                       homeMessageObjects.remove(i);

                                       homeMessageAdapter.notifyDataSetChanged();
                                   }


                               }

                           }


                       }


                   }

                   if (RecentChats.size() == 0) {


                       if(date.equals(todays_date)){

                           RecentChats.add(new HomeMessageObject(sendername, text, time, otherpartyprofileiamge, receiver, user.getUid(), otherpartyname, user_image_url));


                       }
                       else{
                           RecentChats.add(new HomeMessageObject(sendername, text, date, otherpartyprofileiamge, receiver, user.getUid(), otherpartyname, user_image_url));

                       }

                       homeMessageObjects.add(new HomeMessageObject(RecentChats.get(RecentChats.size() - 1).receiver_name, RecentChats.get(RecentChats.size() - 1).recent_text, RecentChats.get(RecentChats.size() - 1).time, RecentChats.get(RecentChats.size() - 1).image_url, RecentChats.get(RecentChats.size() - 1).other_party, RecentChats.get(RecentChats.size() - 1).sender_uid, RecentChats.get(RecentChats.size() - 1).sender_name, RecentChats.get(RecentChats.size() - 1).sender_image_url));


                       Log.e("xero", "xero");
                       //  homeMessageObjects.add(new HomeMessageObject(RecentChats.get(RecentChats.size()-1).sender_name,RecentChats.get(RecentChats.size()-1).recent_text,RecentChats.get(RecentChats.size()-1).time,RecentChats.get(RecentChats.size()-1).image_url,RecentChats.get(RecentChats.size()-1).other_party));
                       homeMessageAdapter.notifyDataSetChanged();


                   }
               }


              /* if (receiver != null) {
*/




         /*  }



          else{

                   Log.e("receiver1",""+receiver);
                   Log.e("otherpartyname1",""+otherpartyname);
                   Log.e("otherpartyprofileimage1",""+otherpartyprofileiamge);
                   Log.e("text1",""+text);
                   Log.e("time1",""+time);
                   Log.e("sendername1",""+sendername);
                   Log.e("userimageurl1",""+user_image_url);
               }*/












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


        recentChats.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                progressBar.setVisibility(View.INVISIBLE);
                if(RecentChats.size()==0){
                    noMessages.setVisibility(View.VISIBLE);
                }
                else{
                    noMessages.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        count=0;












      }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return true;
    }

    private void getTodaysDate(){

        Calendar calendar = Calendar.getInstance();
        String am_pm = null;
        String monthS=null;


        int hourS=calendar.get(Calendar.HOUR_OF_DAY);
      //  year=
        String yearS = Integer.toString(calendar.get(Calendar.YEAR)) ;
        int monthofYear = calendar.get(Calendar.MONTH);
       // day=calendar.get(Calendar.DAY_OF_MONTH);
        String dayS = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        int minuteS=calendar.get(Calendar.MINUTE);
        //    Toast.makeText(getActivity(),hourS+minutesS,Toast.LENGTH_SHORT).show();

        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hourS);
        datetime.set(Calendar.MINUTE, minuteS);

        String minute;
        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
            am_pm= "AM";
        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";

        if(datetime.get(Calendar.MINUTE)<10){

            minute= "0"+datetime.get(Calendar.MINUTE);
        }
        else minute=""+datetime.get(Calendar.MINUTE);

        String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ? "12" : datetime.get(Calendar.HOUR) + "";
       // todays_time=strHrsToShow + ":" + minute + " " + am_pm;



        switch(monthofYear+1) {
            case 1:
                monthS = "Jan";
                break;
            case 2:
                monthS = "Feb";
                break;
            case 3:
                monthS= "March";
                break;
            case 4:
                monthS= "April";
                break;
            case 5:
                monthS = "May";
                break;
            case 6:
                monthS = "June";
                break;
            case 7:
                monthS = "July";
                break;
            case 8:
                monthS = "August";
                break;
            case 9:
                monthS = "Sept";
                break;
            case 10:
                monthS = "Oct";
                break;
            case 11:
                monthS = "Nov";
                break;
            case 12:
                monthS = "Dec";
                break;
        }

        todays_date=""+dayS+" "+monthS+", "+yearS;





    }

    /*private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                xMark = ContextCompat.getDrawable(HomeMessage.this, R.drawable.ic_clear_24dp);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) HomeMessage.this.getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;
            }

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                HomeMessageAdapter homeMessageAdapter = (HomeMessageAdapter) recyclerView.getAdapter();
                if (homeMessageAdapter.isUndoOn() && homeMessageAdapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                HomeMessageAdapter adapter = (HomeMessageAdapter) recyclerView.getAdapter();
                boolean undoOn = adapter.isUndoOn();
                if (undoOn) {
                    adapter.pendingRemoval(swipedPosition);
                } else {
                    adapter.remove(swipedPosition);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }*/

    /**
     * We're gonna setup another ItemDecorator that will draw the red background in the empty space while the items are animating to thier new positions
     * after an item is removed.
     */
   /* private void setUpAnimationDecoratorHelper() {
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            // we want to cache this and not allocate anything repeatedly in the onDraw method
            Drawable background;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                initiated = true;
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                if (!initiated) {
                    init();
                }

                // only if animation is in progress
                if (parent.getItemAnimator().isRunning()) {

                    // some items might be animating down and some items might be animating up to close the gap left by the removed item
                    // this is not exclusive, both movement can be happening at the same time
                    // to reproduce this leave just enough items so the first one and the last one would be just a little off screen
                    // then remove one from the middle

                    // find first child with translationY > 0
                    // and last one with translationY < 0
                    // we're after a rect that is not covered in recycler-view views at this point in time
                    View lastViewComingDown = null;
                    View firstViewComingUp = null;

                    // this is fixed
                    int left = 0;
                    int right = parent.getWidth();

                    // this we need to find out
                    int top = 0;
                    int bottom = 0;

                    // find relevant translating views
                    int childCount = parent.getLayoutManager().getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getLayoutManager().getChildAt(i);
                        if (child.getTranslationY() < 0) {
                            // view is coming down
                            lastViewComingDown = child;
                        } else if (child.getTranslationY() > 0) {
                            // view is coming up
                            if (firstViewComingUp == null) {
                                firstViewComingUp = child;
                            }
                        }
                    }

                    if (lastViewComingDown != null && firstViewComingUp != null) {
                        // views are coming down AND going up to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    } else if (lastViewComingDown != null) {
                        // views are going down to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = lastViewComingDown.getBottom();
                    } else if (firstViewComingUp != null) {
                        // views are coming up to fill the void
                        top = firstViewComingUp.getTop();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    }

                    background.setBounds(left, top, right, bottom);
                    background.draw(c);

                }
                super.onDraw(c, parent, state);
            }

        });
    }*/

    /**
     * RecyclerView adapter enabling undo on a swiped away item.
     */














    }













