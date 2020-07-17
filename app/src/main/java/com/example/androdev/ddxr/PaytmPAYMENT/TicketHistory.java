package com.example.androdev.ddxr.PaytmPAYMENT;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.androdev.ddxr.R;
import com.example.androdev.ddxr.UserFragments.InterestedPeople;
import com.example.androdev.ddxr.UserFragments.ParticipantsObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TicketHistory extends AppCompatActivity {

    RecyclerView recyclerView;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    List<TicketHistoryObject> participants;
    DatabaseReference eventsReference;
    DatabaseReference parentReference;
    Toolbar toolbar;
    FirebaseUser user;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_history);

        recyclerView=(RecyclerView)findViewById(R.id.my_recycler_view);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Purchase History");

        toolbar.setTitleTextColor(ContextCompat.getColor(TicketHistory.this,R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        eventsReference= FirebaseDatabase.getInstance("https://ddrx-events-172d3.firebaseio.com/").getReference();
        parentReference=FirebaseDatabase.getInstance().getReference();
        user= FirebaseAuth.getInstance().getCurrentUser();

        participants=new ArrayList<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mAdapter = new MyAdapter(TicketHistory.this, participants);



        mLayoutManager = new LinearLayoutManager(TicketHistory.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);

        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);



        mRecyclerView.setAdapter(mAdapter);

        fetch();
    }

    public void fetch(){


        parentReference.child(user.getUid()).child("BookedTickets").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
              String EventKey=  dataSnapshot.child("EventKey").getValue(String.class);
              String TicketKey=dataSnapshot.child("TicketKey").getValue(String.class);
              String EventName=dataSnapshot.child("EventName").getValue(String.class);
              String TicketName=dataSnapshot.child("TicketName").getValue(String.class);
              String EventBannerURL=dataSnapshot.child("EventBannerURL").getValue(String.class);
              String EntryKey=dataSnapshot.child("Key").getValue(String.class);
              String TeamName=dataSnapshot.child("TeamName").getValue(String.class);

                participants.add(new TicketHistoryObject(EventName,TicketName,EventBannerURL,EventKey,TicketKey,EntryKey,TeamName));
                mAdapter.notifyDataSetChanged();
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

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{


        private List<TicketHistoryObject> participantsX=new ArrayList<>();
        private Context context;

        public MyAdapter(Context context, List<TicketHistoryObject> participantsX) {
            this.context=context;
            this.participantsX=participantsX;

        }


        @NonNull
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.likeparticipantsmodel,parent,false);
            MyAdapter.MyViewHolder vh = new  MyAdapter.MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, final int i) {

            final TicketHistoryObject c= participantsX.get(i);
//            holder.follow.setVisibility(View.GONE);

            holder.UserName.setText(c.getUserName());
            holder.UserClubLocation.setText(c.getUserClubLocation());
            holder.parent_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(TicketHistory.this,TicketActivity.class);


                    intent.putExtra("EventKey",c.getEventKey());
                    intent.putExtra("TicketKey",c.getTicketKey());
                    intent.putExtra("UserUID",user.getUid());
                    intent.putExtra("Key",c.getEntryKey());
                    intent.putExtra("TeamName",c.getTeamName());

                    startActivity(intent);
                }
            });
            //  holder.Activity.setText(c.getActivity());
            Picasso.get()
                    .load(c.getProfileImage())
                    .into(holder.ProfileImage);

        }

        @Override
        public int getItemCount() {
            return participantsX.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{

            TextView UserName;
            RelativeLayout parent_layout;
            TextView  UserClubLocation;
            CircleImageView ProfileImage;
            ImageView follow;
            // TextView Activity;


            public MyViewHolder(@NonNull View itemView) {


                super(itemView);


                UserName=(TextView)itemView.findViewById(R.id.username);
                follow=(ImageView)itemView.findViewById(R.id.follow);
                parent_layout=(RelativeLayout) itemView.findViewById(R.id.parent_layout);
                UserClubLocation=(TextView)itemView.findViewById(R.id.club_location);
                ProfileImage=(CircleImageView) itemView.findViewById(R.id.profile_image);
                //  Activity=(TextView)itemView.findViewById(R.id.activity);



            }
        }



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}
