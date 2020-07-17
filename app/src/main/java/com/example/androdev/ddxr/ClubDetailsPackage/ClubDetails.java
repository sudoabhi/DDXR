package com.example.androdev.ddxr.ClubDetailsPackage;


import android.content.Intent;

import android.os.Bundle;

import android.support.annotation.NonNull;

import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.androdev.ddxr.ChatBox.FirstActivity;
import com.example.androdev.ddxr.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;



import com.squareup.picasso.Picasso;


public class ClubDetails extends AppCompatActivity {

    TextView num_post;
    TextView num_attendee;
    TextView num_event;
    TextView domain;
    TextView college;
    TextView club_name;
    TextView location;
    TextView formation_year;
    TextView club_type;
    TextView club_bio;
    ImageView profile_image;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    FirebaseUser user;
    DatabaseReference reference;
    FirebaseStorage storage;
    StorageReference storage_reference;

    ProgressBar progressBar;
    DatabaseReference parentReference;


    String collegeS;
    LinearLayout lay;
    LinearLayout posts;
    LinearLayout events;
    LinearLayout attendees;
    LinearLayout bio;


    FirebaseDatabase blogDb;
    DatabaseReference blogReference;

    int postCount=0;

    Toolbar toolbar;

    String ClubUID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_details);

        profile_image=(ImageView) findViewById(R.id.profile_image);
        num_attendee = (TextView) findViewById(R.id.num_attendees);
        num_event = (TextView) findViewById(R.id.num_events);
        num_post = (TextView) findViewById(R.id.num_posts);
        domain = (TextView) findViewById(R.id.domain_name);
        college= findViewById(R.id.college_name);
        club_name = (TextView) findViewById(R.id.club_name);
        location = (TextView) findViewById(R.id.club_location);
        formation_year=(TextView) findViewById(R.id.formation_year);
        club_type=(TextView) findViewById(R.id.type);
        club_bio=(TextView) findViewById(R.id.club_bio);

        lay=findViewById(R.id.llll);
        lay.setVisibility(View.GONE);
        posts=findViewById(R.id.posts);
        events=findViewById(R.id.events);
        attendees=findViewById(R.id.attendees);
        bio=findViewById(R.id.bio);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        progressBar=(ProgressBar) findViewById(R.id.progressBar);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://ddrx-admin-172d3.firebaseio.com/");
        parentReference=FirebaseDatabase.getInstance().getReference();
        reference = database.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storage_reference = storage.getReference();
        storage_reference=storage_reference.child("admin_app/");

        blogDb=FirebaseDatabase.getInstance("https://organizersblog.firebaseio.com/");
        blogReference=blogDb.getReference();


        Intent i = getIntent();
        ClubUID = i.getStringExtra("ClubUID");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                club_name.setText(dataSnapshot.child(ClubUID).child("ClubName").getValue(String.class));
                toolbar.setTitle(club_name.getText().toString());
                domain.setText(dataSnapshot.child(ClubUID).child("ClubDomain").getValue(String.class));
                collegeS = dataSnapshot.child(ClubUID).child("ClubCollegeName").getValue(String.class);
                formation_year.setText(dataSnapshot.child(ClubUID).child("ClubFormationYear").getValue(String.class));
                club_bio.setText(dataSnapshot.child(ClubUID).child("ClubBio").getValue(String.class));
                String club_typeS=dataSnapshot.child(ClubUID).child("ClubType").getValue(String.class);
                club_type.setText(club_typeS);

                int event_count=(int)dataSnapshot.child(ClubUID).child("Events").getChildrenCount();
                num_event.setText(""+event_count);


                if(club_typeS!=null){
                    if(club_typeS.equals("College Club")){
                        lay.setVisibility(View.VISIBLE);
                        college.setText(collegeS);
                        location.setText(dataSnapshot.child(ClubUID).child("ClubCollegeDistrict").getValue(String.class));
                    }
                    else{
                        location.setText(dataSnapshot.child(ClubUID).child("ClubAddress").getValue(String.class));
                    }
                }




                Picasso.get()
                        .load(dataSnapshot.child(ClubUID).child("ProfileImageUrl").getValue(String.class))
                        .resize(800, 800)
                        .centerCrop()
                        .noFade()
                        .error(R.drawable.addphoto) // default image to load
                        .into(profile_image);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        blogReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String uid=dataSnapshot.child("UploadedBy").getValue(String.class);
                if(ClubUID.equals(uid)){
                    postCount++;
                }
                num_post.setText(""+postCount);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        posts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClubDetails.this, ClubPostsList.class);
                intent.putExtra("ClubUID", ClubUID);
                startActivity(intent);


            }
        });

        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ClubDetails.this, ClubEventsList.class);
                intent.putExtra("ClubUID", ClubUID);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onBackPressed() {

            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_club_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_message) {


            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {


                    parentReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshotX) {

                            String ImageURl=dataSnapshot.child(ClubUID).child("ProfileImageUrl").getValue(String.class);
                            String MyImage=dataSnapshotX.child(user.getUid()).child("ImageUrl").getValue(String.class);
                            String MyName=dataSnapshotX.child(user.getUid()).child("UserName").getValue(String.class);

                            Intent intent = new Intent(ClubDetails.this, FirstActivity.class);
                            //Toast.makeText(PeopleClick.this, "" + SenderName, Toast.LENGTH_SHORT).show();
                            intent.putExtra("ReceiverName", club_name.getText().toString());
                            intent.putExtra("ReceiverUID", ClubUID);
                            intent.putExtra("ReceiverImageUrl", ImageURl);
                            intent.putExtra("SenderProfileImage", MyImage);
                            intent.putExtra("SenderName",MyName  );
                            intent.putExtra("ActivityName", "PeopleClick");
                            startActivity(intent);

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






            return true;

        }

        return super.onOptionsItemSelected(item);
    }









    }

