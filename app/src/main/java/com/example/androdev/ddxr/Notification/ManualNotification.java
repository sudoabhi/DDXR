package com.example.androdev.ddxr.Notification;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androdev.ddxr.R;
import com.example.androdev.ddxr.UserFragments.FirebaseCallBackX2;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManualNotification extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    List<ManualNotificationModel> posts;

    Toolbar toolbar;
    ProgressBar progressBar;
    TextView tv_noresults;

    int countX=0;

    DatabaseReference manualreference;
    DatabaseReference parentReference;

    ArrayList<String> bookedTickets2;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_notification);

        user= FirebaseAuth.getInstance().getCurrentUser();

        parentReference=FirebaseDatabase.getInstance().getReference();

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        progressBar=findViewById(R.id.top_progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        tv_noresults=findViewById(R.id.tv_noresults);
        tv_noresults.setVisibility(View.INVISIBLE);

        manualreference= FirebaseDatabase.getInstance("https://manualnotifications.firebaseio.com/").getReference();



        posts = new ArrayList<>();
        mAdapter = new NewsFeedAdapter(ManualNotification.this, posts);


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);



        mLayoutManager = new LinearLayoutManager(ManualNotification.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);

        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);





        mRecyclerView.setAdapter(mAdapter);




        getBookedTickets(new FirebaseCallBackX2() {
            @Override
            public void onCallBack(ArrayList<String> string) {

                bookedTickets2 = (ArrayList<String>)string.clone();



                if(bookedTickets2.size()==0){
                    tv_noresults.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }



                posts.clear();
                fetch();

            }
        });




     //   fetch();



    }

    public void fetch(){

        manualreference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String Notification= dataSnapshot.child("Notification").getValue(String.class);
                String Time=  dataSnapshot.child("Time").getValue(String.class);
                String Date= dataSnapshot.child("Date").getValue(String.class);
                String NotificationTitle=dataSnapshot.child("NotificationTitle").getValue(String.class);
                String TicketName=dataSnapshot.child("TicketName").getValue(String.class);
                String EventName=dataSnapshot.child("EventName").getValue(String.class);
                String TicketKey=dataSnapshot.child("TicketKey").getValue(String.class);








                for(int i=0;i<bookedTickets2.size();i++){



                    if(bookedTickets2.get(i).equals(TicketKey)){
                        posts.add(new ManualNotificationModel(Notification,Date,Time,NotificationTitle,TicketName,EventName));
                        mAdapter.notifyDataSetChanged();

                    }

                    if(i==bookedTickets2.size()-1){
                      //  Toast.makeText(ManualNotification.this,"Hii",Toast.LENGTH_SHORT).show();
                        if(posts.size()==0){
                            tv_noresults.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);


                        }
                        else{
                            tv_noresults.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
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

        manualreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()==0){
                    tv_noresults.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        manualreference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(){
//                    tv_noresults.setVisibility(View.INVISIBLE);
//                } else {
//                    tv_noresults.setVisibility(View.VISIBLE);
//                    progressBar.setVisibility(View.INVISIBLE);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });





    }

    class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.MyViewHolder>  {

        private List<ManualNotificationModel> posts;
        private Context context;



        public NewsFeedAdapter(Context context, List<ManualNotificationModel> posts) {
            this.context=context;
            this.posts=posts;

        }

        public  class MyViewHolder extends RecyclerView.ViewHolder {

            TextView NotificationText;
            TextView NotificationTitle;
            TextView Date;
            TextView Time;
            TextView EventName;
            TextView TicketName;


            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                NotificationText=(TextView)itemView.findViewById(R.id.notification_text);
                Date=(TextView)itemView.findViewById(R.id.notification_date);
                Time=(TextView)itemView.findViewById(R.id.notification_time);
                NotificationTitle=(TextView)itemView.findViewById(R.id.notification_title);
                EventName=(TextView)itemView.findViewById(R.id.event_name);
                TicketName=(TextView)itemView.findViewById(R.id.ticket_name);
            }
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.manual_notification_model,viewGroup,false);
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

            final ManualNotificationModel c = posts.get(i);

            myViewHolder.NotificationText.setText(c.getNotificationText());
            myViewHolder.Date.setText(c.getDate());
            myViewHolder.Time.setText(c.getTime());
            myViewHolder.NotificationTitle.setText(c.getNotificationTitle());
            myViewHolder.EventName.setText("Event Name:- "+c.getEventName());
            myViewHolder.TicketName.setText("Ticket Name:- "+c.getTicketName());
            progressBar.setVisibility(View.INVISIBLE);


        }

        @Override
        public int getItemCount() {
            return posts.size();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void getBookedTickets(final FirebaseCallBackX2 firebaseCallBackX2){

      final  ArrayList<String> bookedTickets=new ArrayList<>();


      parentReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               final    long count=dataSnapshot.child("BookedTickets").getChildrenCount();


              dataSnapshot.child("BookedTickets").getRef().addChildEventListener(new ChildEventListener() {
                  @Override
                  public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {



                      countX++;

                      String ticketkey=dataSnapshot.child("TicketKey").getValue(String.class);
                      bookedTickets.add(ticketkey);

                      if(countX==count){
                          firebaseCallBackX2.onCallBack(bookedTickets);
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

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
      });







    }
}
