package com.example.androdev.ddxr.UserFragments;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.androdev.ddxr.ClubDetailsPackage.ClubDetails;
import com.example.androdev.ddxr.R;
import com.example.androdev.ddxr.UserProfilePackage.PeopleClick;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class InterestedPeople extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    List<ParticipantsObject> participants;
    DatabaseReference eventsReference;
    String NewsFeedKey;
    String CommentKey;
    DatabaseReference parentRef;
    DatabaseReference adminRef;
    Toolbar toolbar;
    int participant_count;
    String InterestedCount;
    String PeopleClickId;
    String Purpose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interested_people);

        Intent i=getIntent();

        PeopleClickId=i.getStringExtra("UserUID");
        Purpose=i.getStringExtra("Purpose");

        NewsFeedKey=i.getStringExtra("NewsFeedKey");
       // InterestedCount=i.getStringExtra("InterestedCountX");
      //  Log.e("MMR",""+InterestedCount);


        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Interested People");

        if(PeopleClickId!=null){
            getSupportActionBar().setTitle(Purpose);
        }

        toolbar.setTitleTextColor(ContextCompat.getColor(InterestedPeople.this,R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        eventsReference= FirebaseDatabase.getInstance("https://ddrx-events-172d3.firebaseio.com/").getReference();
        parentRef=FirebaseDatabase.getInstance().getReference();
        adminRef=FirebaseDatabase.getInstance("https://ddrx-admin-172d3.firebaseio.com/").getReference();

        participants=new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mAdapter = new InterestedPeople.MyAdapter(InterestedPeople.this, participants);



        mLayoutManager = new LinearLayoutManager(InterestedPeople.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);

        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);



        mRecyclerView.setAdapter(mAdapter);


        if(PeopleClickId!=null){
            fetchX();
        }
        else{
            fetch();
        }

      //  fetch();

    }

    public void fetchX(){

        parentRef.child(PeopleClickId).child(Purpose).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String ClubLocation=dataSnapshot.child("ClubLocation").getValue(String.class);
                String ImageUrl=dataSnapshot.child("ImageUrl").getValue(String.class);
                String UserName=dataSnapshot.child("UserName").getValue(String.class);

                participants.add(new ParticipantsObject(UserName,ClubLocation,ImageUrl,null,dataSnapshot.getKey(),"Student"));
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

    public void fetch(){

        eventsReference.child(NewsFeedKey).child("Interested").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                String userID=  dataSnapshot.getKey();
                getDetails(userID);



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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void getDetails(final String userID){
        Log.i("Keyc",""+userID);

        parentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(userID)){

                    String ImageUrl=   dataSnapshot.child(userID).child("ImageUrl").getValue(String.class);
                    String UserName=  dataSnapshot.child(userID).child("UserName").getValue(String.class);
                    String ClubLocation=dataSnapshot.child(userID).child("ClubLocation").getValue(String.class);

                    participants.add(new ParticipantsObject(UserName,ClubLocation,ImageUrl,null,userID,"Student"));
                    mAdapter.notifyDataSetChanged();
                }
                else{

                    adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String ImageUrl=   dataSnapshot.child(userID).child("ProfileImageUrl").getValue(String.class);
                            String UserName=  dataSnapshot.child(userID).child("ClubName").getValue(String.class);
                            String ClubLocation=dataSnapshot.child(userID).child("ClubLocation").getValue(String.class);

                            participants.add(new ParticipantsObject(UserName,ClubLocation,ImageUrl,null,userID,"Admin"));
                            mAdapter.notifyDataSetChanged();

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

    class MyAdapter extends RecyclerView.Adapter<InterestedPeople.MyAdapter.MyViewHolder>{


        private List<ParticipantsObject> participantsX;
        private Context context;

        public MyAdapter(Context context, List<ParticipantsObject> participantsX) {
            this.context=context;
            this.participantsX=participantsX;

        }


        @NonNull
        @Override
        public InterestedPeople.MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.likeparticipantsmodel,parent,false);
            InterestedPeople.MyAdapter.MyViewHolder vh = new  InterestedPeople.MyAdapter.MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull InterestedPeople.MyAdapter.MyViewHolder holder, int i) {

           final ParticipantsObject c= participantsX.get(i);

            holder.UserName.setText(c.getUserName());
            holder.UserClubLocation.setText(c.getUserClubLocation());
            holder.ProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;

                    if(c.getCategory().equals("Student")){
                        intent=new Intent(InterestedPeople.this, PeopleClick.class);
                        intent.putExtra("UserUID",c.getUserUID());

                    }
                    else{
                        intent=new Intent(InterestedPeople.this, ClubDetails.class);
                        intent.putExtra("ClubUID", c.getUserUID());
                    }

                    startActivity(intent);


                }
            });
            holder.LL_parent_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;

                    if(c.getCategory().equals("Student")){
                        intent=new Intent(InterestedPeople.this, PeopleClick.class);
                        intent.putExtra("UserUID",c.getUserUID());

                    }
                    else{
                        intent=new Intent(InterestedPeople.this, ClubDetails.class);
                        intent.putExtra("ClubUID", c.getUserUID());
                    }

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
            TextView  UserClubLocation;
            CircleImageView ProfileImage;
            LinearLayout LL_parent_text;

            // TextView Activity;


            public MyViewHolder(@NonNull View itemView) {


                super(itemView);


                UserName=(TextView)itemView.findViewById(R.id.username);
                UserClubLocation=(TextView)itemView.findViewById(R.id.club_location);
                ProfileImage=(CircleImageView) itemView.findViewById(R.id.profile_image);
                LL_parent_text=(LinearLayout)itemView.findViewById(R.id.LL_parent_text);
                //  Activity=(TextView)itemView.findViewById(R.id.activity);



            }
        }



    }
}
