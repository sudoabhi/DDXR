package com.example.androdev.ddxr.UserFragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.androdev.ddxr.R;
import com.example.androdev.ddxr.UserProfilePackage.PeopleClick;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileViewers extends AppCompatActivity {


    Spinner ViewersSpinner;
    Button viewB;
    ArrayList<String> options=new ArrayList<>();

    ArrayAdapter adapter;
    String filter_name;
    Toolbar toolbar;

    String todays_date;
    String todays_time;
    String monthS;
    String yearS;
    String dayS;
    String am_pm;
    TextView nothing_to_show;


    ProgressBar pbar;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    List<ViewersObject> participants;
    DatabaseReference parentRef;
    DatabaseReference adminRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_viewers);

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        pbar=(ProgressBar) findViewById(R.id.progress_bar);
        nothing_to_show=(TextView) findViewById(R.id.nothing_to_show);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Views");

        toolbar.setTitleTextColor(ContextCompat.getColor(ProfileViewers.this,R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);






        parentRef=FirebaseDatabase.getInstance().getReference();
        adminRef=FirebaseDatabase.getInstance("https://ddrx-admin-172d3.firebaseio.com/").getReference();

        participants=new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mAdapter = new ProfileViewers.MyAdapter(ProfileViewers.this, participants);



        mLayoutManager = new LinearLayoutManager(ProfileViewers.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);

        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);



        mRecyclerView.setAdapter(mAdapter);


        ViewersSpinner=(Spinner)findViewById(R.id.viewers_spinner);
        viewB=(Button)findViewById(R.id.viewB);

        options.add("Today");
        options.add("All Time");


        adapter = new ArrayAdapter(ProfileViewers.this,android.R.layout.simple_list_item_1 ,options);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ViewersSpinner.setAdapter(adapter);

        getTodaysTime();



        fetch("Today");

        ViewersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {




                ((TextView) view).setTextColor(Color.RED);
                participants.clear();


                 filter_name=adapterView.getItemAtPosition(i).toString();




            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        viewB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pbar.setVisibility(View.VISIBLE);
                participants.clear();
                mAdapter.notifyDataSetChanged();
                fetch(filter_name);
            }
        });



    }

    public void fetch(final String filter_nameX){
        participants.clear();
        mAdapter.notifyDataSetChanged();

        FirebaseUser userIDX= FirebaseAuth.getInstance().getCurrentUser();
        final String userID=userIDX.getUid();

        parentRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if( dataSnapshot.hasChild("ProfileViewers")){
                   if(dataSnapshot.child("ProfileViewers").getChildrenCount()!=0){

                       dataSnapshot.child("ProfileViewers").getRef().addChildEventListener(new ChildEventListener() {
                           @Override
                           public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                               String ImageUrl=   dataSnapshot.child("ProfileImage").getValue(String.class);
                               String UserName=  dataSnapshot.child("Name").getValue(String.class);
                               String ClubLocation=dataSnapshot.child("ClubLocation").getValue(String.class);
                               String SeenDay=dataSnapshot.child("Date").getValue(String.class);
                               String SeenTime=dataSnapshot.child("Time").getValue(String.class);
                               String UserUID=dataSnapshot.getKey();

                               if(filter_nameX.equals("Today")){
                                   Log.e(""+SeenDay,"Seen1");
                                   Log.e(""+todays_date,"Seen2");
                                   if(SeenDay.equals(todays_date)){
                                       pbar.setVisibility(View.INVISIBLE);

                                       participants.add(new ViewersObject(UserName,ClubLocation,ImageUrl,null,SeenTime,SeenDay,UserUID));
                                       mAdapter.notifyDataSetChanged();
                                   }

                               }
                               else{
                                   pbar.setVisibility(View.INVISIBLE);

                                   participants.add(new ViewersObject(UserName,ClubLocation,ImageUrl,null,SeenTime,SeenDay,UserUID));
                                   mAdapter.notifyDataSetChanged();
                               }

                               dataSnapshot.getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                       if(participants.size()==0){
                                           pbar.setVisibility(View.INVISIBLE);
                                           nothing_to_show.setVisibility(View.VISIBLE);
                                           mRecyclerView.setVisibility(View.INVISIBLE);
                                       }
                                       else{
                                           pbar.setVisibility(View.INVISIBLE);
                                           nothing_to_show.setVisibility(View.INVISIBLE);
                                           mRecyclerView.setVisibility(View.VISIBLE);
                                       }

                                   }

                                   @Override
                                   public void onCancelled(@NonNull DatabaseError databaseError) {

                                   }
                               });

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
                   else{
                       pbar.setVisibility(View.INVISIBLE);
                       nothing_to_show.setVisibility(View.VISIBLE);
                       mRecyclerView.setVisibility(View.INVISIBLE);
                   }
               }
               else{

                   pbar.setVisibility(View.INVISIBLE);
                   nothing_to_show.setVisibility(View.VISIBLE);
                   mRecyclerView.setVisibility(View.INVISIBLE);
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       // parentRef.child(userID).child("ProfileViewers").addListenerForSingleValueEvent(new ChildEventListener());





      /*  parentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(userID)){



                    String ImageUrl=   dataSnapshot.child(userID).child("ImageUrl").getValue(String.class);
                    String UserName=  dataSnapshot.child(userID).child("UserName").getValue(String.class);
                    String ClubLocation=dataSnapshot.child(userID).child("ClubLocation").getValue(String.class);
                    String SeenDay=dataSnapshot.child(userID).child("ProfileViewers")

                    participants.add(new ParticipantsObject(UserName,ClubLocation,ImageUrl,null));
                    mAdapter.notifyDataSetChanged();
                }
                else{

                    adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String ImageUrl=   dataSnapshot.child(userID).child("ProfileImageUrl").getValue(String.class);
                            String UserName=  dataSnapshot.child(userID).child("ClubName").getValue(String.class);
                            String ClubLocation=dataSnapshot.child(userID).child("ClubLocation").getValue(String.class);

                            participants.add(new ParticipantsObject(UserName,ClubLocation,ImageUrl,null));
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
        });*/







    }

    class MyAdapter extends RecyclerView.Adapter<ProfileViewers.MyAdapter.MyViewHolder>{


        private List<ViewersObject> participantsX;
        private Context context;

        public MyAdapter(Context context, List<ViewersObject> participantsX) {
            this.context=context;
            this.participantsX=participantsX;

        }


        @NonNull
        @Override
        public ProfileViewers.MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewersmodel,parent,false);
            ProfileViewers.MyAdapter.MyViewHolder vh = new  ProfileViewers.MyAdapter.MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull ProfileViewers.MyAdapter.MyViewHolder holder, int i) {

          final  ViewersObject c= participantsX.get(i);

            holder.UserName.setText(c.getUserName());
            holder.UserClubLocation.setText(c.getUserClubLocation());
            holder.ViewDate.setText("Viewed on "+c.getDate());
            holder.ViewTime.setText(" at "+c.getTime());

            holder.LL_parent_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(ProfileViewers.this,PeopleClick.class);
                    intent.putExtra("UserUID",c.getUserUID());
                    startActivity(intent);

                }
            });

            holder.ProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(ProfileViewers.this,PeopleClick.class);
                    intent.putExtra("UserUID",c.getUserUID());
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
            TextView ViewDate;
            TextView ViewTime;
            LinearLayout LL_parent_text;
            // TextView Activity;


            public MyViewHolder(@NonNull View itemView) {


                super(itemView);


                UserName=(TextView)itemView.findViewById(R.id.username);
                UserClubLocation=(TextView)itemView.findViewById(R.id.club_location);
                ProfileImage=(CircleImageView) itemView.findViewById(R.id.profile_image);
                ViewDate=(TextView)itemView.findViewById(R.id.seen_date);
                ViewTime=(TextView)itemView.findViewById(R.id.seen_time);
                LL_parent_text=(LinearLayout)itemView.findViewById(R.id.LL_parent_text);
                //  Activity=(TextView)itemView.findViewById(R.id.activity);



            }
        }



    }

    private void getTodaysTime() {

        Calendar calendar = Calendar.getInstance();


        int hourS = calendar.get(Calendar.HOUR_OF_DAY);
        yearS = Integer.toString(calendar.get(Calendar.YEAR));
        int monthofYear = calendar.get(Calendar.MONTH);
        dayS = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        int minuteS = calendar.get(Calendar.MINUTE);
        //    Toast.makeText(getActivity(),hourS+minutesS,Toast.LENGTH_SHORT).show();

        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hourS);
        datetime.set(Calendar.MINUTE, minuteS);

        String minute;
        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "AM";
        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";

        if (datetime.get(Calendar.MINUTE) < 10) {

            minute = "0" + datetime.get(Calendar.MINUTE);
        } else minute = "" + datetime.get(Calendar.MINUTE);

        String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ? "12" : datetime.get(Calendar.HOUR) + "";
        todays_time = strHrsToShow + ":" + minute + " " + am_pm;

        switch (monthofYear + 1) {
            case 1:
                monthS = "Jan";
                break;
            case 2:
                monthS = "Feb";
                break;
            case 3:
                monthS = "March";
                break;
            case 4:
                monthS = "April";
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

        todays_date = "" + dayS + " " + monthS + ", " + yearS;


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
