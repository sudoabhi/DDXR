package com.example.androdev.ddxr.ClubDetailsPackage;


import android.content.Intent;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androdev.ddxr.EventDetails.EventDetailsFAQsActivity;

import com.example.androdev.ddxr.EventDetails.EventDetailsOrganisersActivity;

import com.example.androdev.ddxr.EventDetails.EventDetailsPostersActivity;
import com.example.androdev.ddxr.EventDetails.EventDetailsTicketsActivity;

import com.example.androdev.ddxr.R;
import com.example.androdev.ddxr.UserFragments.FirebaseCallbackXXXX;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class ClubEventDetails extends AppCompatActivity {

    KenBurnsView EventBannerView;

    String EventKey;
    TextView EventName;
    TextView EventDetail;
    String EventBannerURL;
    TextView Location;
    ProgressBar progressBar2;
    ProgressBar progress_bar;
    TextView start_date_and_time;
    TextView end_date_and_time;
    //TextView InterestedCountT;
    String EventBannerURLX;
    TextView PriceSegmentTV;
    //CircleImageView InterestedPhoto1;
    //CircleImageView InterestedPhoto2;
    ImageView FAQButton;
    DatabaseReference parentReference;
    DatabaseReference eventReference;
    DatabaseReference adminReference;
    ImageView TicketsButton;
    String thiseventurl;
    ImageView OrganiserButton;

    FirebaseUser user;


    RelativeLayout parent_view;


    ImageView share;


    ScrollView scrollView;


    String[] MyImageURL;
    String URLChecker;

    int position=-2;
    TextView LastDate;
    String EventNameX;

    CardView cv_ticket;
    CardView cv_faq;
    CardView cv_organiser;




    String EventNameS;
    String EventDetailS;
    String start_date;

    String start_time;

    String end_date;
    String last_date;
    String end_time;


    SharedPreferences myPrefs2;
    SharedPreferences.Editor prefsEditor2;




    FrameLayout event_banner_container;
    Toolbar toolbar;
    Button interestedB;
    TextView interestedT;
    ImageView interestedIV;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_event_details);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        EventKey = i.getStringExtra("Key");
        position=i.getIntExtra("position",-2);

        parentReference = FirebaseDatabase.getInstance().getReference();
        eventReference = FirebaseDatabase.getInstance("https://ddrx-events-172d3.firebaseio.com/").getReference();
        adminReference = FirebaseDatabase.getInstance("https://ddrx-admin-172d3.firebaseio.com/").getReference();

        event_banner_container=findViewById(R.id.event_banner_container);
        interestedB =findViewById(R.id.interested);
        scrollView = findViewById(R.id.scrollView);
        EventBannerView = findViewById(R.id.event_banner);

        RandomTransitionGenerator generator = new RandomTransitionGenerator(5000,new AccelerateDecelerateInterpolator());
        EventBannerView.setTransitionGenerator(generator);


        parent_view=(RelativeLayout) findViewById(R.id.parent_view);

        progressBar2=(ProgressBar) findViewById(R.id.progress_bar2);
        PriceSegmentTV=(TextView) findViewById(R.id.price_segment);


        user = FirebaseAuth.getInstance().getCurrentUser();
        EventDetail = (TextView) findViewById(R.id.event_details);
        EventName = (TextView) findViewById(R.id.event_name);
        Location = (TextView) findViewById(R.id.location);
        start_date_and_time = (TextView) findViewById(R.id.start_date_and_time);
        end_date_and_time = (TextView) findViewById(R.id.end_date_and_time);
        //InterestedCountT = (TextView) findViewById(R.id.interested_count);
        //InterestedPhoto1 = (CircleImageView) findViewById(R.id.interested_photo1);
        //InterestedPhoto2 = (CircleImageView) findViewById(R.id.interested_photo2);
        LastDate=(TextView) findViewById(R.id.last_date_to_register);
        FAQButton = (ImageView) findViewById(R.id.faq_button);
        TicketsButton = (ImageView) findViewById(R.id.tickets_button);
        OrganiserButton = (ImageView) findViewById(R.id.organisers_button);
        cv_faq=findViewById(R.id.faq_cardview);
        cv_ticket=findViewById(R.id.tickets_card_view);
        cv_organiser=findViewById(R.id.org_cardview);

        progress_bar=(ProgressBar) findViewById(R.id.progress_bar);


        interestedIV=findViewById(R.id.interestedIV);
        interestedT=findViewById(R.id.interestedT);
        getInterestedDetails();
        interestedB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //interestedB.setBackgroundColor(Color.parseColor("#E3E3E3"));
                Toast.makeText(ClubEventDetails.this,"Missing feature",Toast.LENGTH_SHORT).show();
                interestedB.setVisibility(View.GONE);
                interestedIV.setVisibility(View.VISIBLE);
                interestedT.setText("I am interested");
            }
        });



        event_banner_container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ClubEventDetails.this,EventDetailsPostersActivity.class);
                i.putExtra("EventKey",EventKey);
                i.putExtra("EventName",EventNameS);
                startActivity(i);
            }
        });
        EventBannerView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ClubEventDetails.this, EventDetailsPostersActivity.class);
                i.putExtra("EventKey",EventKey);
                i.putExtra("EventName",EventNameS);
                startActivity(i);
            }
        });


        share = (ImageView) findViewById(R.id.share_event);


        //InterestedTextView=(TextView)findViewById(R.id.interested_tv);
        //InterestedPhotosFL=(FrameLayout)findViewById(R.id.interested_photos);





        MyImageURL = new String[1];














        Log.e("EventKey",""+EventKey);


        URLChecker="false";
        position=i.getIntExtra("position",-2);
        //getEventURL();
        fetch();



        share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);


                String uri="http://hungryhunter96.github.io/share_event?Key="+EventKey;


                Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setLink(Uri.parse(uri))
                        //.setDomainUriPrefix("https://ddxradmin.page.link")
                        .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())

                        .buildShortDynamicLink()
                        .addOnCompleteListener(ClubEventDetails.this, new OnCompleteListener<ShortDynamicLink>() {
                            @Override
                            public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                                if (task.isSuccessful()) {
                                    //  progress_edit_delete.setVisibility(View.GONE);

                                    // Toast.makeText(ClubEventDetails.this,"Link Copied",Toast.LENGTH_SHORT).show();

                                    Uri shortLink = task.getResult().getShortLink();
                                    thiseventurl=shortLink.toString();
                                    sendIntent.putExtra(Intent.EXTRA_TEXT, shortLink.toString());
                                    sendIntent.setType("text/plain");
                                    startActivity(sendIntent);
                                        /*Uri flowchartLink = task.getResult().getPreviewLink();

                                        ClipboardManager clipboard = (ClipboardManager) ClubEventDetails.this.getSystemService(Context.CLIPBOARD_SERVICE);
                                        ClipData clip = ClipData.newPlainText(null, shortLink.toString());
                                        if (clipboard == null) return;
                                        clipboard.setPrimaryClip(clip);*/

                                } else {

                                    Toast.makeText(ClubEventDetails.this,"Link Copy Failure",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });








            }
        });









        FAQButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ClubEventDetails.this, EventDetailsFAQsActivity.class);
                i.putExtra("EventKey",EventKey);
                i.putExtra("EventName",EventNameX);
                startActivity(i);
            }
        });

        cv_faq.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ClubEventDetails.this,EventDetailsFAQsActivity.class);
                i.putExtra("EventKey",EventKey);
                i.putExtra("EventName",EventNameX);
                startActivity(i);

            }
        });


        OrganiserButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ClubEventDetails.this, EventDetailsOrganisersActivity.class);
                i.putExtra("EventKey",EventKey);
                i.putExtra("EventName",EventNameX);
                startActivity(i);
            }
        });

        cv_organiser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ClubEventDetails.this,EventDetailsOrganisersActivity.class);
                i.putExtra("EventKey",EventKey);
                i.putExtra("EventName",EventNameX);
                startActivity(i);

            }
        });

        TicketsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ClubEventDetails.this, EventDetailsTicketsActivity.class);
                i.putExtra("EventKey",EventKey);
                i.putExtra("EventName",EventNameX);
                startActivity(i);
            }
        });

        cv_ticket.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ClubEventDetails.this,EventDetailsTicketsActivity.class);
                i.putExtra("EventKey",EventKey);
                i.putExtra("EventName",EventNameX);
                startActivity(i);

            }
        });


        /*// ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();*/
    }






    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_event, menu);
        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            Intent i=new Intent(EventDetails.this,EditEventDetails.class);
            i.putExtra("EventKey",EventKey);
            i.putExtra("EventName",EventNameS);
            i.putExtra("EventBannerURL",EventBannerURL);
            i.putExtra("EventDescription",EventDetailS);
            i.putExtra("StartDate",start_date);
            i.putExtra("StartTime",start_time);
            i.putExtra("EndDate",end_date);
            i.putExtra("EndTime",end_time);
            i.putExtra("LastDate",last_date);
            startActivity(i);
            return true;

        } else if (id == R.id.action_edit_faq) {

            //startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            return true;
        } else if (id == R.id.action_edit_org) {

            return true;
        } else if (id == R.id.action_edit_ticket) {

            return true;
        } else if (id == R.id.action_bug) {
            Toast.makeText(EventDetails.this, "Stay Tuned. Coming Soon",
                    Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/





    public String DateFormat(String olddate){
        String[] parts=olddate.split("/");

        String s=null;
        switch(parts[1]){

            case "01":
                s="January";
                break;
            case "02":
                s="Febraury";
                break;
            case "03":
                s="March";
                break;
            case "04":
                s="April";
                break;
            case "05":
                s="May";
                break;
            case "06":
                s="June";
                break;
            case "07":
                s="July";
                break;
            case "08":
                s="August";
                break;
            case "09":
                s="September";
                break;
            case "10":
                s="October";
                break;
            case "11":
                s="November";
                break;
            case "12":
                s="December";
                break;




        }

        String result=parts[0]+" "+s+", 20"+parts[2];
        return result;

    }

    public void getInterestedDetails(){

        eventReference.child(EventKey).child("Interested").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String bookmarkS=dataSnapshot.child(user.getUid()).child("Bookmark").getValue(String.class);
                String likeS=dataSnapshot.child(user.getUid()).child("Like").getValue(String.class);
                String statusS=dataSnapshot.child(user.getUid()).child("Status").getValue(String.class);

                if(Objects.equals(likeS, "true") || Objects.equals(bookmarkS, "true") || Objects.equals(statusS, "true")){
                    interestedB.setVisibility(View.GONE);
                    interestedT.setText("I am interested");
                    interestedIV.setVisibility(View.VISIBLE);

                }else{
                    interestedB.setVisibility(View.VISIBLE);
                    interestedT.setText("Are you interested?");
                    interestedIV.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

/*
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








    }*/


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void fetch(){

        getEventImageURL(new FirebaseCallbackXXXX() {
            @Override
            public void onCallBack1(String string) {
                EventBannerURLX=string;


            }

            @Override
            public void onCallBack2(String string) {
                EventNameX=string;

            }


        });

        /*getProfileImage(new FirebaseCallbackX() {
            @Override
            public void onCallBack(String string) {

                MyImageURL[0] = string;
                Log.e("url", "" + MyImageURL[0]);

            }


        });

        getInterestedDetails(new FirebaseCallbackXXXXX() {
            @Override
            public void onCallBack1(String string) {

                Bookmark = string;


            }

            @Override
            public void onCallBack2(String string) {

                Like = string;

            }

            @Override
            public void onCallBack3(long string) {

                InterestedCount[0] = string;
                InterestedCountInit=String.valueOf(string);

            }
        });*/

        eventReference.child(EventKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                EventBannerURL = dataSnapshot.child("EventThumbnailURL").getValue(String.class);
                EventNameS = dataSnapshot.child("EventName").getValue(String.class);
                EventDetailS = dataSnapshot.child("EventDescription").getValue(String.class);
                start_date = dataSnapshot.child("StartDate").getValue(String.class);
                String new_start_date = DateFormat(start_date);
                start_time = dataSnapshot.child("StartTime").getValue(String.class);
                String InterestedPhoto1S = dataSnapshot.child("InterestedPerson1").getValue(String.class);
                String InterestedPhoto2S = dataSnapshot.child("InterestedPerson2").getValue(String.class);
                end_date = dataSnapshot.child("EndDate").getValue(String.class);
                String new_end_date = DateFormat(end_date);
                last_date=dataSnapshot.child("LAST_DATE").getValue(String.class);
                end_time = dataSnapshot.child("EndTime").getValue(String.class);
                long InterestedCount = dataSnapshot.child("Interested").getChildrenCount();
                int StartingPrice;
                int EndingPrice;
                String PriceSegment=null;
                if(dataSnapshot.hasChild("StartingPrice")){
                    StartingPrice=dataSnapshot.child("StartingPrice").getValue(Integer.class);
                }
                else{
                    StartingPrice=0;
                }
                if(dataSnapshot.hasChild("EndingPrice")){
                    EndingPrice=dataSnapshot.child("EndingPrice").getValue(Integer.class);
                }
                else{
                    EndingPrice=0;
                }

                if(StartingPrice==0&&EndingPrice==0){

                    PriceSegment="Free";

                }
                else if(StartingPrice!=0&&StartingPrice!=EndingPrice){
                    PriceSegment="₹ "+String.valueOf(StartingPrice)+" - "+String.valueOf(EndingPrice);
                }
                else if(StartingPrice==0&&EndingPrice!=0){
                    PriceSegment="Upto ₹ "+String.valueOf(EndingPrice);
                }
                else if(StartingPrice==EndingPrice&&StartingPrice!=0){
                    PriceSegment="₹ "+String.valueOf(StartingPrice);
                }

                PriceSegmentTV.setText(PriceSegment);






                LastDate.setText(last_date);

                if (dataSnapshot.child("EventType").getValue(String.class).equals("Online")) {
                    String LocationS = dataSnapshot.child("EventWebsiteURL").getValue(String.class);
                    Location.setText(LocationS);
                } else {
                    String LocationS = dataSnapshot.child("EventAddress").getValue(String.class);
                    Location.setText(LocationS);
                }
                start_date_and_time.setText(new_start_date + " at " + start_time);
                end_date_and_time.setText(new_end_date + " at " + end_time);


                EventDetail.setText(EventDetailS);
                //InterestedCountT.setText("" + InterestedCount);
                EventName.setText(EventNameS);



                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        // Stuff that updates the UI
                        Picasso.get()
                                .load(Uri.parse(EventBannerURL))
                                //.transform(transformation)
                                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                                .into(EventBannerView, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        progress_bar.setVisibility(View.GONE);
                                        parent_view.setVisibility(View.VISIBLE);
                                        share.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onError(Exception e) {

                                    }
                                });

                    }
                });

                /*if (InterestedPhoto1S != null) {
                    InterestedPhoto1.setVisibility(View.VISIBLE);
                    Picasso.get()
                            .load(Uri.parse(InterestedPhoto1S))

                            .into(InterestedPhoto1);
                } else {
                    InterestedPhoto1.setVisibility(View.GONE);
                    InterestedPhoto2.setVisibility(View.GONE);
                }
                if (InterestedPhoto2S != null) {
                    InterestedPhoto1.setVisibility(View.VISIBLE);
                    InterestedPhoto2.setVisibility(View.VISIBLE);
                    Picasso.get()
                            .load(Uri.parse(InterestedPhoto2S))

                            .into(InterestedPhoto2);
                } else {
                    InterestedPhoto2.setVisibility(View.GONE);
                }*/


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });







    }







    public void getEventImageURL(final FirebaseCallbackXXXX firebaseCallbackX){

        adminReference.child(user.getUid()).child("Events").child(EventKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String EventBannerURL=dataSnapshot.child("EventThumbnailURL").getValue(String.class);
                String EventName=dataSnapshot.child("EventName").getValue(String.class);
                firebaseCallbackX.onCallBack1(EventBannerURL);
                firebaseCallbackX.onCallBack2(EventName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    /*  @Override
      public boolean onOptionsItemSelected(MenuItem item) {
          switch (item.getItemId()) {
              case android.R.id.home:
                  onBackPressed();
                  break;
          }
          return true;
      }
  */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }














}
