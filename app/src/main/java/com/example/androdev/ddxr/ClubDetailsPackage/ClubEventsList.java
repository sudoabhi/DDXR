package com.example.androdev.ddxr.ClubDetailsPackage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androdev.ddxr.UserActivities.MyPosts;
import com.example.androdev.ddxr.R;
import com.example.androdev.ddxr.UserFragments.EventDiscussions;
import com.example.androdev.ddxr.UserFragments.FirebaseCallbackX;
import com.example.androdev.ddxr.UserFragments.InterestedPeople;
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

public class ClubEventsList extends AppCompatActivity {

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



    SharedPreferences myPrefs2;




    List<MyPosts> posts;

    ArrayList<String> districts;

    String ClubName;
    String EventDescription;
    String EventName;

    String key;

    String EventBanner;
    String Club_Image_Url;


    String EventType;
    String UpdatedDay;
    String UploadedBy;

    String likeS;


    String Key;


    ImageView noresults_iv;
    TextView noresults_tv;
    ArrayList<String> newList;
   String ClubUID;
    Toolbar toolbar;


    ChildEventListener mchildeventlistener;
    LinearLayout no_event_happening;


    int likeCount=0;
    int bookmarkCount=0;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_events_list);


        FirebaseMessaging.getInstance().subscribeToTopic("all");

        Intent i=getIntent();
        ClubUID = i.getStringExtra("ClubUID");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        events_database = FirebaseDatabase.getInstance("https://ddrx-events-172d3.firebaseio.com/");
        events_reference = events_database.getReference();
        events_reference.keepSynced(true);

        parentReference=FirebaseDatabase.getInstance().getReference();
        adminReference=FirebaseDatabase.getInstance("https://ddrx-admin-172d3.firebaseio.com/").getReference();


        getProfileImage(new FirebaseCallbackX() {
            @Override
            public void onCallBack(String string) {

                MyImageURL=string;

            }


        } );






        posts = new ArrayList<>();

        districts=new ArrayList<>();
        newList = new ArrayList<>();
        mAdapter = new ClubEventsList.NewsFeedAdapter(this, posts);
        jadapter = new ArrayAdapter<String>(this,R.layout.listview,newList);

        noresults_iv=(ImageView)findViewById(R.id.noresults_iv);
        noresults_tv=(TextView) findViewById(R.id.noresults_tv);




        user = FirebaseAuth.getInstance().getCurrentUser();



        event_node_reference = events_reference.push();
        key = event_node_reference.getKey();


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);



        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);

        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);





        mRecyclerView.setAdapter(mAdapter);

        posts.clear();

        fetch();




    }




    private void fetch(){



        ChildEventListener childEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {

                if(snapshot.child("UploadedBy").getValue(String.class).equals(ClubUID)) {

                    int StartingPrice;
                    int EndingPrice;
                    String PriceSegment = null;


                    String end_date = snapshot.child("EndDate").getValue(String.class);

                    String start_date = snapshot.child("StartDate").getValue(String.class);

                    final String dates = start_date + " - " + end_date;

                    EventName = snapshot.child("EventName").getValue(String.class);

                    final String EventTypeX = snapshot.child("EventTypeX").getValue(String.class);
                    final String ClubDomain = snapshot.child("ClubDomain").getValue(String.class);
                    final String EventLocation = snapshot.child("EventLocation").getValue(String.class);
                    final long InterestedCount = snapshot.child("Interested").getChildrenCount();
                    EventBanner = snapshot.child("EventThumbnailURL").getValue(String.class);
                    if (snapshot.hasChild("StartingPrice")) {
                        StartingPrice = snapshot.child("StartingPrice").getValue(Integer.class);
                    } else {
                        StartingPrice = 0;
                    }
                    if (snapshot.hasChild("EndingPrice")) {
                        EndingPrice = snapshot.child("EndingPrice").getValue(Integer.class);
                    } else {
                        EndingPrice = 0;
                    }

                    if (StartingPrice == 0 && EndingPrice == 0) {

                        PriceSegment = "Free";

                    } else if (StartingPrice != 0 && StartingPrice != EndingPrice) {
                        PriceSegment = String.valueOf(StartingPrice) + " - " + String.valueOf(EndingPrice);
                    } else if (StartingPrice == 0 && EndingPrice != 0) {
                        PriceSegment = "Upto " + String.valueOf(EndingPrice);
                    } else if (StartingPrice == EndingPrice && StartingPrice != 0) {
                        PriceSegment = String.valueOf(StartingPrice);
                    }


                    if(snapshot.hasChild("LikeCount")){
                        likeCount= Integer.parseInt(snapshot.child("LikeCount").getValue(String.class));
                    }
                    if(snapshot.hasChild("BookmarkCount")){
                        bookmarkCount=Integer.parseInt(snapshot.child("BookmarkCount").getValue(String.class));
                    }

                    ClubName = snapshot.child("ClubName").getValue(String.class);
                    toolbar.setTitle(ClubName);


                    ClubLocation = snapshot.child("ClubLocation").getValue(String.class);

                    EventType = snapshot.child("EventType").getValue(String.class);
                    Club_Image_Url = snapshot.child("ClubImageURL").getValue(String.class);

                    UploadedBy = snapshot.child("UploadedBy").getValue(String.class);
                    Key = snapshot.child("EventKey").getValue(String.class);
                    likeS = snapshot.child("Interested").child(user.getUid()).child("Like").getValue(String.class);
                    final String Interested = snapshot.child("Interested").child(user.getUid()).child("Status").getValue(String.class);

                    final String bookmark = snapshot.child("Interested").child(user.getUid()).child("Bookmark").getValue(String.class);
                    final String InterestedPerson1 = snapshot.child("InterestedPerson1").getValue(String.class);
                    final String InterestedPerson2 = snapshot.child("InterestedPerson2").getValue(String.class);

                    final ArrayList<String> InterestedPhoto = new ArrayList<>();

                    if (InterestedPerson1 != null) {
                        InterestedPhoto.add(InterestedPerson1);

                    }
                    if (InterestedPerson2 != null) {
                        InterestedPhoto.add(InterestedPerson2);
                    }

                    if (InterestedPhoto.size() != InterestedCount && InterestedPhoto.size() < 2) {


                        Query query = events_reference.child(Key).child("Interested").limitToLast(2);

                        query.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                String InterestedPerson1X = InterestedPerson1;
                                String InterestedPerson2X = InterestedPerson2;
                                if (InterestedPerson1X == null) {
                                    InterestedPerson1X = dataSnapshot.child("ImageUrl").getValue(String.class);
                                    if (InterestedPhoto.get(0) != null && InterestedPerson1X != null) {
                                        if (!InterestedPerson1X.equals(InterestedPhoto.get(0))) {
                                            events_reference.child(Key).child("InterestedPerson1").setValue(InterestedPerson1X);
                                            InterestedPhoto.add(InterestedPerson1X);
                                        }
                                    }


                                }
                                if (InterestedPerson2X == null) {
                                    InterestedPerson2X = dataSnapshot.child("ImageUrl").getValue(String.class);
                                    if (InterestedPhoto.get(0) != null && InterestedPerson2X != null) {
                                        if (!InterestedPerson2X.equals(InterestedPhoto.get(0))) {

                                            events_reference.child(Key).child("InterestedPerson2").setValue(InterestedPerson2X);
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

                    posts.add(new MyPosts(ClubName, ClubLocation, EventDescription, dates, EventName, Club_Image_Url,
                            EventBanner, UpdatedDay, UploadedBy, likeS, Key, bookmark, EventTypeX, ClubDomain,
                            EventLocation, EventType, Interested, InterestedCount, InterestedPhoto, PriceSegment,
                            likeCount,bookmarkCount));



                    mAdapter.notifyDataSetChanged();

                    if (posts.size() == 0) {

                        noresults_iv.setVisibility(View.VISIBLE);
                        noresults_tv.setVisibility(View.VISIBLE);

                        //   Toast.makeText(getActivity(),"No content available",Toast.LENGTH_SHORT).show();
                    } else {


                        noresults_iv.setVisibility(View.INVISIBLE);
                        noresults_tv.setVisibility(View.INVISIBLE);

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
        };
        events_reference.addChildEventListener(childEventListener);




    }










    private void open_allusers_meanu(final String key,final int position){

        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.newsfeed_allusers_options, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);


        alertDialogBuilder.setView(promptsView);


        final AlertDialog alertDialog2 = alertDialogBuilder.create();


        alertDialog2.show();

        TextView report=(TextView)promptsView.findViewById(R.id.report);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(),"This feature is not working currently",Toast.LENGTH_LONG).show();

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

    public void shareEvent(String EventKey){


        final Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);


        String uri="http://hungryhunter96.github.io/share_event?Key="+EventKey;


        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(uri))
                .setDomainUriPrefix("https://ddxr.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())

                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
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





    class NewsFeedAdapter extends RecyclerView.Adapter<ClubEventsList.NewsFeedAdapter.MyViewHolder>   {

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

                EventName=(TextView)v.findViewById(R.id.event_name);
                Event_Dates=(TextView)v.findViewById(R.id.event_dates);
                InterestedPhoto2=(CircleImageView)v.findViewById(R.id.interested_photo2);
                InterestedPhoto1=(CircleImageView)v.findViewById(R.id.interested_photo1);


            }

        }

        // Provide a suitable constructor (depends on the kind of dataset)

        public NewsFeedAdapter(Context context,List<MyPosts> posts) {
            this.context=context;
            this.posts=posts;

        }

        // Create new views (invoked by the layout manager)
        @Override
        public ClubEventsList.NewsFeedAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                                                        int viewType) {
            // create a new view
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.demo2,parent,false);
            ClubEventsList.NewsFeedAdapter.MyViewHolder vh = new ClubEventsList.NewsFeedAdapter.MyViewHolder(v);
            return vh;
        }


        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(@NonNull final ClubEventsList.NewsFeedAdapter.MyViewHolder holder, final int position) {

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
                    Intent intent=new Intent(ClubEventsList.this, InterestedPeople.class);
                    intent.putExtra("NewsFeedKey",c.getKey());
                    // intent.putExtra("InterestedCountX",5);
                    Log.e("MMS",""+c.getInterestedCount());
                    startActivity(intent);
                }
            });
            holder.InterestedCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(ClubEventsList.this,InterestedPeople.class);
                    intent.putExtra("NewsFeedKey",c.getKey());
                    // intent.putExtra("InterestedCountX",5);
                    Log.e("MMS",""+c.getInterestedCount());
                    startActivity(intent);

                }
            });

            holder.InterestedTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(ClubEventsList.this,InterestedPeople.class);
                    intent.putExtra("NewsFeedKey",c.getKey());
                    // intent.putExtra("InterestedCountX",5);
                    Log.e("MMS",""+c.getInterestedCount());
                    startActivity(intent);

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
                            .into(holder.InterestedPhoto1);


                }


            }


            if(c.getInterestedCount()>1){

                holder.InterestedPhotosFL.setVisibility(View.VISIBLE);

                holder.InterestedPhoto2.setVisibility(View.VISIBLE);
                if(c.getInterestedPhoto().size()>0&&c.getInterestedPhoto().get(0)!=null){
                    Picasso.get()
                            .load(Uri.parse(c.getInterestedPhoto().get(0)))
                            .into(holder.InterestedPhoto1);

                    if(c.getInterestedPhoto().size()>1&&c.getInterestedPhoto().get(1)!=null){
                        Picasso.get()
                                .load(Uri.parse(c.getInterestedPhoto().get(1)))
                                .into(holder.InterestedPhoto2);

                    }

                }
            }


            holder.GetDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(ClubEventsList.this, ClubEventDetails.class);
                    intent.putExtra("Key",c.getKey());
                    intent.putExtra("position",position);
                    startActivity(intent);
                }
            });




            final Resources r = getResources();
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
                    Intent intent=new Intent(ClubEventsList.this, EventDiscussions.class);
                    intent.putExtra("EventKey",c.getKey());
                    intent.putExtra("EventName",c.getEventName());
                    startActivity(intent);

                }
            });



            Picasso.get()

                    .load(c.getClub_Image_Url())
                    .placeholder(R.drawable.club_placeholder)
                    .resize(800, 800)
                    .centerCrop()
                    .noFade()
                    .error(R.drawable.add_photo3) // default image to load
                    .into(holder.ClubProfileImage, new Callback() {
                        @Override
                        public void onSuccess() {


                            holder.Options.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    open_allusers_meanu(c.getKey(),position);

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


                    double aspectRatio = (double) source.getHeight() / (double) source.getWidth();

                    int resultant_width=(int) (targetWidth * aspectRatio);
                    int px7 = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            160,
                            r.getDisplayMetrics()
                    );

                    if(resultant_width<px7){

                        targetHeight=px7;

                    }
                    else{
                        targetHeight = (int) (targetWidth * aspectRatio);
                    }





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
                    .placeholder(R.drawable.load)
                    .error(android.R.drawable.stat_notify_error)
                    .transform(transformation)
                    .into(holder.EventBanner, new Callback() {
                        @Override
                        public void onSuccess() {




                        }

                        @Override
                        public void onError(Exception e) {

                            Toast.makeText(context, "Failed to load new posts", Toast.LENGTH_SHORT).show();

                        }


                    });



        }


        @Override
        public int getItemCount() {
            return posts.size();
        }

        public  int getCount(){

            return  posts.size();

        }



    }






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






}


