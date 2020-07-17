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

public class ParticipantsList extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    List<ParticipantsObject> participants;
    DatabaseReference blogReference;
    String key;
    DatabaseReference parentRef;
    DatabaseReference adminRef;
    Toolbar toolbar;
    int participant_count;
    String EventKeyM;
    String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants_list);

        state=getIntent().getStringExtra("State");
        key=getIntent().getStringExtra("Key");
        EventKeyM=getIntent().getStringExtra("EventKey");
        participant_count=getIntent().getIntExtra("Count",0);


      toolbar=(Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);
      getSupportActionBar().setTitle("Participants("+participant_count+")");

      toolbar.setTitleTextColor(ContextCompat.getColor(ParticipantsList.this,R.color.white));
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        if(EventKeyM==null){
            if(state!=null&&state.equals("Students")) {
                blogReference = FirebaseDatabase.getInstance("https://ddrx-172d3-2a5f7.firebaseio.com/").getReference();
            }
            if(state!=null&&state.equals("Organizers")) {
                blogReference = FirebaseDatabase.getInstance("https://organizersblog.firebaseio.com/").getReference();
            }
        }
        else{
            blogReference= FirebaseDatabase.getInstance("https://eventdiscussions.firebaseio.com/").getReference(EventKeyM);
        }

        parentRef=FirebaseDatabase.getInstance().getReference();
        adminRef=FirebaseDatabase.getInstance("https://ddrx-admin-172d3.firebaseio.com/").getReference();

        participants=new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mAdapter = new ParticipantsList.MyAdapter(ParticipantsList.this, participants);



        mLayoutManager = new LinearLayoutManager(ParticipantsList.this);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void fetch(){

        blogReference.child(key).child("Participated").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String X1=null;
                String X2=null;
                String X3=null;

                if( dataSnapshot.hasChild("1")){
                    X1=dataSnapshot.child("1").getValue(String.class);
                }
                if( dataSnapshot.hasChild("2")){
                    X2=dataSnapshot.child("2").getValue(String.class);
                }
                if( dataSnapshot.hasChild("3")){

                    X3=dataSnapshot.child("3").getValue(String.class);
                    Log.e("3",""+X3);
                }



               String w=null;
                if(X2==null&&X1!=null&&X3!=null){
                    Log.e("33",""+X3);
                    w=X1+" and "+X3;
                }
                if(X1==null&&X2!=null&&X3!=null){
                    w=X2+" and "+X3;
                }
                if(X3==null&&X1!=null&&X2!=null){
                    w=X1+" and "+X2;
                }

                if(X2==null&&X1==null){
                     w=X3;
                }
                if(X1==null&&X3==null){
                    w=X2;
                }
                if(X2==null&&X3==null){
                    w=X1;
                }
                if(X2!=null&&X3!=null&&X1!=null){
                    w=X1+","+X2+" and "+X3;
                }
                String userID=  dataSnapshot.getKey();
                getDetails(userID,w);



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

   public void getDetails(final String userID,final String w){
        Log.i("Keyc",""+userID);

        parentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(userID)){

                  String ImageUrl=   dataSnapshot.child(userID).child("ImageUrl").getValue(String.class);
                  String UserName=  dataSnapshot.child(userID).child("UserName").getValue(String.class);
                  String ClubLocation=dataSnapshot.child(userID).child("ClubLocation").getValue(String.class);

                  participants.add(new ParticipantsObject(UserName,ClubLocation,ImageUrl,w,userID,"Student"));
                  mAdapter.notifyDataSetChanged();
                }
                else{

                    adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String ImageUrl=   dataSnapshot.child(userID).child("ProfileImageUrl").getValue(String.class);
                            String UserName=  dataSnapshot.child(userID).child("ClubName").getValue(String.class);
                            String ClubLocation=dataSnapshot.child(userID).child("ClubLocation").getValue(String.class);

                            participants.add(new ParticipantsObject(UserName,ClubLocation,ImageUrl,w,userID,"Admin"));
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

   class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{


        private List<ParticipantsObject> participantsX;
        private Context context;

        public MyAdapter(Context context, List<ParticipantsObject> participantsX) {
            this.context=context;
            this.participantsX=participantsX;

        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.participants_model,parent,false);
            ParticipantsList.MyAdapter.MyViewHolder vh = new  ParticipantsList.MyAdapter.MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

          final  ParticipantsObject c= participantsX.get(i);

            holder.UserName.setText(c.getUserName());
            holder.UserClubLocation.setText(c.getUserClubLocation());
            holder.Activity.setText(c.getActivity());
            holder.LL_parent_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;

                    if(c.getCategory().equals("Student")){
                        intent=new Intent(ParticipantsList.this, PeopleClick.class);
                        intent.putExtra("UserUID",c.getUserUID());

                    }
                    else{
                        intent=new Intent(ParticipantsList.this, ClubDetails.class);
                        intent.putExtra("ClubUID", c.getUserUID());
                    }

                    startActivity(intent);


                }
            });
            holder.ProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent;

                    if(c.getCategory().equals("Student")){
                        intent=new Intent(ParticipantsList.this, PeopleClick.class);
                        intent.putExtra("UserUID",c.getUserUID());

                    }
                    else{
                        intent=new Intent(ParticipantsList.this, ClubDetails.class);
                        intent.putExtra("ClubUID", c.getUserUID());
                    }

                    startActivity(intent);

                }
            });

            Picasso.get()
                    .load(c.getProfileImage())
                    .into(holder.ProfileImage);

        }

        @Override
        public int getItemCount() {
            return participantsX.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{

            TextView  UserName;
            TextView  UserClubLocation;
            CircleImageView ProfileImage;
            TextView Activity;
            LinearLayout LL_parent_text;



            public MyViewHolder(@NonNull View itemView) {


                super(itemView);


                UserName=(TextView)itemView.findViewById(R.id.username);
                UserClubLocation=(TextView)itemView.findViewById(R.id.club_location);
                ProfileImage=(CircleImageView) itemView.findViewById(R.id.profile_image);
                Activity=(TextView)itemView.findViewById(R.id.activity);
                LL_parent_text=(LinearLayout)itemView.findViewById(R.id.LL_parent_Text);



            }
        }




    }
}
