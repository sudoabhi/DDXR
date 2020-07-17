package com.example.androdev.ddxr.ClubDetailsPackage;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androdev.ddxr.R;
import com.example.androdev.ddxr.UserFragments.BlogActivity;
import com.example.androdev.ddxr.UserFragments.BlogsObject;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClubPostsList extends AppCompatActivity {


    RecyclerView mRecyclerView;
    FirebaseDatabase blog_database;
    DatabaseReference blog_reference;
    LinearLayoutManager mLayoutManager;
    String UserName;
    FirebaseStorage storage;
    StorageReference storage_reference;
    DatabaseReference newblogreference;

    String  ProfileImage;
    FirebaseUser user;
  /*  FirebaseDatabase parentDatabase;
    DatabaseReference parentReference;*/
  FirebaseDatabase adminDatabase;
  DatabaseReference adminReference;
    String ImageUrl;
    String UserSkill;
    private RecyclerView.Adapter mAdapter;

    ProgressBar progressBar;


    String  BlogText;
    String  updated_day;
    String  updated_time;
    String uploadedBy;
    String ClubUID;

    AlertDialog.Builder builder;


    String likeS;
    String dislikeS;
    String Key;
    ArrayList<BlogsObject> posts;
    int NumberOfDislikes;
    int NumberOfLikes;
    Handler mHandler;
    CardView EdiTextCardView;
    ImageView call_filters;
    TextView tv_noresults;



    int m;
    int spinner_position;
    String sort_category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_posts_list);

        Intent i=getIntent();
        ClubUID = i.getStringExtra("ClubUID");

    mHandler = new Handler();
        storage= FirebaseStorage.getInstance();
        storage_reference = storage.getReference();

    tv_noresults=(TextView)findViewById(R.id.tv_noresults) ;

    EdiTextCardView=(CardView) findViewById(R.id.post_blog);
    mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
    user= FirebaseAuth.getInstance().getCurrentUser();

    blog_database = FirebaseDatabase.getInstance("https://organizersblog.firebaseio.com/");
    adminDatabase= FirebaseDatabase.getInstance("https://ddrx-admin-172d3.firebaseio.com/");
    adminReference=adminDatabase.getReference();
    blog_reference = blog_database.getReference();

    newblogreference = blog_reference.push();
    //UploadedBy=user.getUid();
        progressBar=(ProgressBar)findViewById(R.id.top_progress_bar);

   /* parentDatabase=FirebaseDatabase.getInstance();
    parentReference=parentDatabase.getReference();*/


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });






    //  posts = new ArrayList<>();
    //   mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
    posts = new ArrayList<>();

    mAdapter = new BlogAdapter(ClubPostsList.this, posts);

    mLayoutManager = new LinearLayoutManager(ClubPostsList.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    //  mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false
                );
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);

        mRecyclerView.setAdapter(mAdapter);


        fetch();



}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.fragment_blogs_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.filter:
                call_filters();
                return true;

        }

        return true;
    }


    public void call_filters(){


        final Dialog dialog = new Dialog(new ContextThemeWrapper(ClubPostsList.this, R.style.DialogSlideAnim));
        dialog.setContentView(R.layout.call_filters);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.windowAnimations = R.style.DialogSlideAnim;
        dialog.getWindow().setAttributes(lp);
        Spinner spinner = (Spinner) dialog.findViewById(R.id.sort_spinner);
        Button ApplySort = (Button) dialog.findViewById(R.id.sort_apply);
        ApplySort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner_position = m;

                posts.clear();
                mAdapter.notifyDataSetChanged();
                fetch_filter(sort_category);

                dialog.cancel();
            }
        });
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ClubPostsList.this,
                R.array.blogs_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setSelection(spinner_position);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //   ((TextView) view).setTextColor(Color.RED);


                sort_category = adapterView.getItemAtPosition(i).toString();
                m = i;
                //  Toast.makeText(getActivity(), "" + sort_category, Toast.LENGTH_SHORT).show();


                //   final NewsFeedAdapter jadapter=new NewsFeedAdapter(getActivity(),posts);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });

        // show it
        dialog.show();
    }



    public void open_allusers_menu(){

        LayoutInflater li = LayoutInflater.from(ClubPostsList.this);
        View promptsView = li.inflate(R.layout.newsfeed_allusers_options, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ClubPostsList.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);


        final AlertDialog alertDialog2 = alertDialogBuilder.create();

        // show it
        alertDialog2.show();

        TextView report=(TextView)promptsView.findViewById(R.id.report);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog2.cancel();


            }
        });

        TextView share=(TextView)promptsView.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog2.cancel();
            }
        });


    }




    private void fetch(){

        ChildEventListener childEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {

                UserName= snapshot.child("UserName").getValue(String.class);
                UserSkill = snapshot.child("Skill").getValue(String.class);
                ProfileImage = snapshot.child("ProfileImage").getValue(String.class);
                BlogText=snapshot.child("BlogText").getValue(String.class);
                updated_day=snapshot.child("UpdatedDay").getValue(String.class);
                updated_time=snapshot.child("UpdatedTime").getValue(String.class);
                uploadedBy=snapshot.child("UploadedBy").getValue(String.class);

                likeS = snapshot.child("LikeStatus").child(user.getUid()).getValue(String.class);

                dislikeS = snapshot.child("DislikeStatus").child(user.getUid()).getValue(String.class);

                Key=snapshot.child("Key").getValue(String.class);
                NumberOfLikes=snapshot.child("NumberLikes").getValue(Integer.class);
                NumberOfDislikes=snapshot.child("NumberDislikes").getValue(Integer.class);
                String ClubLocation=snapshot.child("ClubLocation").getValue(String.class);

             if(ClubUID.equals(uploadedBy)){
                 posts.add(new BlogsObject(UserName,ClubLocation,null, ProfileImage, BlogText, uploadedBy,
                         updated_time,updated_day,likeS,dislikeS,Key,NumberOfLikes,NumberOfDislikes,
                         0,null));
                 mAdapter.notifyDataSetChanged();
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
        blog_reference.addChildEventListener(childEventListener);


        blog_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.INVISIBLE);

                if(posts.size()==0){

                    tv_noresults.setVisibility(View.VISIBLE);


                    //   Toast.makeText(getActivity(),"No content available",Toast.LENGTH_SHORT).show();
                }
                else{

                    tv_noresults.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }







   /* @Override
    public boolean onLongClick(View view) {
        return false;
    }*/

class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.MyViewHolder>   {

    private List<BlogsObject> posts;
    Context context;




    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public  class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        FrameLayout parentView;
        TextView UserName;
    //    TextView UserSkill;
        //ImageView BlogImage;
        CircleImageView ProfileImage;
        TextView ClubLocation;

        TextView BlogText;
        TextView update_day;
        TextView updated_time;
        LinearLayout likeButton;
        LinearLayout dislikeButton;
        TextView like_count;
        TextView dislike_count;
        ImageView like_icon;
        ImageView dislike_icon;


        ProgressBar progressBar;
        public MyViewHolder(View v) {
            super(v);
            parentView=(FrameLayout) v.findViewById(R.id.parent_view) ;
            UserName = (TextView)v.findViewById(R.id.user_name);
            likeButton=v.findViewById(R.id.like_button);
            dislikeButton=v.findViewById(R.id.dislike_button);
            //BlogImage=(ImageView)v.findViewById(R.id.blog_image);
            ClubLocation=(TextView) v.findViewById(R.id.skill_location);
            //  parentView.setVisibility(View.INVISIBLE);
            //  likeButton=(LikeButton) v.findViewById(R.id.star_button);
            //   progressBar=(ProgressBar) v.findViewById(R.id.post_progress_bar);
            BlogText=(TextView)v.findViewById(R.id.blog_text);
          //  UserSkill=( TextView) v.findViewById(R.id.skill);
            ProfileImage=(CircleImageView) v.findViewById(R.id.profile_image);
            update_day=(TextView)v.findViewById(R.id.date);
            //  update_date=(TextView)v.findViewById(R.id.update_day);
            updated_time=(TextView)v.findViewById(R.id.time);
            like_count=v.findViewById(R.id.like_count);
            dislike_count=v.findViewById(R.id.dislike_count);
            like_icon=v.findViewById(R.id.like_icon);
            dislike_icon=v.findViewById(R.id.dislike_icon);


        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)

    public BlogAdapter(Context context,List<BlogsObject> posts) {
        this.context=context;
        this.posts=posts;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public BlogAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.blog3,parent,false);
        BlogAdapter.MyViewHolder vh = new BlogAdapter.MyViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull final BlogAdapter.MyViewHolder holder, final int position) {

        final BlogsObject c = posts.get(position);
      //   holder.parentView.setVisibility(View.INVISIBLE);
        holder.ClubLocation.setText(c.getClubLocation());

        holder.UserName.setText(c.getUserName());

        //  Toast.makeText(getActivity(),""+posts.getNumberOfLikes(),Toast.LENGTH_SHORT).show();
        holder.like_count.setText(""+c.getNumberOfLikes());
        holder.dislike_count.setText(""+c.getNumberOfDislikes());



            holder.dislikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    int dislikes = c.getNumberOfDislikes();

                    if (c.getDislikeS() == null || c.getDislikeS().equals("false")) {


                        c.setDislikeS("true");
                        c.setLikeS("false");
                        dislikes = dislikes + 1;
                        c.setNumberOfDislikes(dislikes);
                        mAdapter.notifyDataSetChanged();
                        blog_reference.child(c.getKey()).child("LikeStatus").child(user.getUid()).setValue("false");
                        blog_reference.child(c.getKey()).child("DislikeStatus").child(user.getUid()).setValue("true");
                        blog_reference.child(c.getKey()).child("NumberDislikes").setValue(dislikes);


                    } else {
                        c.setDislikeS("false");
                        c.setLikeS("false");
                        dislikes = dislikes - 1;
                        c.setNumberOfDislikes(dislikes);
                        mAdapter.notifyDataSetChanged();
                        blog_reference.child(c.getKey()).child("NumberDislikes").setValue(dislikes);
                        blog_reference.child(c.getKey()).child("LikeStatus").child(user.getUid()).setValue("false");


                        blog_reference.child(c.getKey()).child("DislikeStatus").child(user.getUid()).setValue("false");

                    }

                }
            });


            holder.likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int likes = c.getNumberOfLikes();

                    if (c.getLikeS() == null || c.getLikeS().equals("false")) {


                        likes = likes + 1;
                        c.setDislikeS("false");
                        c.setLikeS("true");
                        c.setNumberOfLikes(likes);
                        mAdapter.notifyDataSetChanged();


                        blog_reference.child(c.getKey()).child("LikeStatus").child(user.getUid()).setValue("true");
                        blog_reference.child(c.getKey()).child("DislikeStatus").child(user.getUid()).setValue("false");


                        blog_reference.child(c.getKey()).child("NumberLikes").setValue(likes);


                    } else {


                        likes = likes - 1;
                        c.setLikeS("false");
                        c.setNumberOfLikes(likes);
                        c.setDislikeS("false");

                        mAdapter.notifyDataSetChanged();
                        blog_reference.child(c.getKey()).child("LikeStatus").child(user.getUid()).setValue("false");
                        blog_reference.child(c.getKey()).child("DislikeStatus").child(user.getUid()).setValue("false");


                        blog_reference.child(c.getKey()).child("NumberLikes").setValue(likes);


                    }

                }
            });

        if (c.getLikeS() != null && c.getLikeS().equals("true")) {
            holder.dislikeButton.setClickable(false);
            int tintColor = ContextCompat.getColor(ClubPostsList.this, android.R.color.holo_blue_dark);
            holder.like_icon.setBackgroundTintList(ColorStateList.valueOf(tintColor));
            holder.dislike_icon.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(ClubPostsList.this, android.R.color.darker_gray)));

        } if (c.getLikeS() != null && c.getLikeS().equals("false") && c.getDislikeS().equals("true")) {
            int tintColor = ContextCompat.getColor(ClubPostsList.this, android.R.color.darker_gray);
            holder.dislikeButton.setClickable(true);
            holder.likeButton.setClickable(false);
            holder.like_icon.setBackgroundTintList(ColorStateList.valueOf(tintColor));

        }



        if (c.getDislikeS() != null && c.getDislikeS().equals("true")) {
            holder.likeButton.setClickable(false);
            int tintColor = ContextCompat.getColor(ClubPostsList.this, android.R.color.holo_red_dark);
            holder.dislike_icon.setBackgroundTintList(ColorStateList.valueOf(tintColor));
            holder.like_icon.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(ClubPostsList.this, android.R.color.darker_gray)));


        }if (c.getDislikeS() != null && c.getDislikeS().equals("false") && c.getLikeS().equals("true")) {
            int tintColor = ContextCompat.getColor(ClubPostsList.this, android.R.color.darker_gray);
            holder.likeButton.setClickable(true);
            holder.dislikeButton.setClickable(false);
            holder.dislike_icon.setBackgroundTintList(ColorStateList.valueOf(tintColor));

        }


            //  setOnItemLongClickLister(mListener);

            holder.parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ClubPostsList.this, BlogActivity.class);
                    intent.putExtra("State","Organizers");
                    intent.putExtra("Key", c.getKey());
                    intent.putExtra("DeletePosition", position);
                    startActivity(intent);
                }
            });

            holder.parentView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View view) {
                    open_allusers_menu();
                    return true;

                }

            });


            // holder.UserSkill.setText(c.getSkill());
            holder.update_day.setText(c.getUpdatedDay());
            // holder.ClubProfileImage.setImageResource(c.getClubProfileImage());
            holder.updated_time.setText(c.getUpdatedTime());
            holder.BlogText.setText(c.getBlogText());


            Picasso.get()
                    .load(c.getImageUrl())
                    .error(android.R.drawable.stat_notify_error)
                    .resize(400, 400)
                    .centerCrop()
                    // .transform(transformation)
                    .into(holder.ProfileImage, new Callback() {
                        @Override
                        public void onSuccess() {


                        }

                        @Override
                        public void onError(Exception e) {

                            Toast.makeText(ClubPostsList.this, "Failed to load new posts", Toast.LENGTH_SHORT).show();

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



    private void fetch_filter(final String sort_category2) {


        posts.clear();



        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {


                UserName = snapshot.child("UserName").getValue(String.class);
                UserSkill = snapshot.child("Skill").getValue(String.class);
                ProfileImage = snapshot.child("ProfileImage").getValue(String.class);
                BlogText = snapshot.child("BlogText").getValue(String.class);
                updated_day = snapshot.child("UpdatedDay").getValue(String.class);
                updated_time = snapshot.child("UpdatedTime").getValue(String.class);
                uploadedBy = snapshot.child("UploadedBy").getValue(String.class);
                likeS = snapshot.child("LikeStatus").child(user.getUid()).getValue(String.class);

                dislikeS = snapshot.child("DislikeStatus").child(user.getUid()).getValue(String.class);

                Key = snapshot.child("Key").getValue(String.class);
                NumberOfLikes = snapshot.child("NumberLikes").getValue(Integer.class);
                NumberOfDislikes = snapshot.child("NumberDislikes").getValue(Integer.class);
                String Edited=snapshot.child("Edited").getValue(String.class);
                int comments_count = 0;
                if (snapshot.hasChild("CommentsCount")) {
                    comments_count = snapshot.child("CommentsCount").getValue(Integer.class);
                }

                String ClubLocation = snapshot.child("ClubLocation").getValue(String.class);

                //    posts.add(new BlogsObject(UserName, UserSkill, ProfileImage, BlogText, uploadedBy, updated_time,updated_day,likeS,dislikeS,Key,NumberOfLikes,NumberOfDislikes));
                //   mAdapter.notifyDataSetChanged();
                if (BlogText != null) {
                    if (sort_category2.equals("All")) {
                        ((AppCompatActivity) ClubPostsList.this).getSupportActionBar().setTitle("All Posts");
                        posts.add(new BlogsObject(UserName, ClubLocation, null, ProfileImage, BlogText, uploadedBy, updated_time, updated_day, likeS, dislikeS, Key, NumberOfLikes, NumberOfDislikes, comments_count,Edited));


                        mAdapter.notifyDataSetChanged();

                    }
                    if (sort_category2.equals("Liked")) {
                        ((AppCompatActivity) ClubPostsList.this).getSupportActionBar().setTitle("Filter: Liked Topics");
                        if (likeS != null && likeS.equals("true")) {
                            posts.add(new BlogsObject(UserName, ClubLocation, null, ProfileImage, BlogText, uploadedBy, updated_time, updated_day, likeS, dislikeS, Key, NumberOfLikes, NumberOfDislikes, comments_count,Edited));

                            mAdapter.notifyDataSetChanged();

                        } else {


                            mAdapter.notifyDataSetChanged();
                        }


                    }
                    if (sort_category2.equals("Added by me")) {
                        ((AppCompatActivity) ClubPostsList.this).getSupportActionBar().setTitle("Filter: My Feed");
                        if (uploadedBy.equals(user.getUid())) {
                            posts.add(new BlogsObject(UserName, ClubLocation, null, ProfileImage, BlogText, uploadedBy, updated_time, updated_day, likeS, dislikeS, Key, NumberOfLikes, NumberOfDislikes, comments_count,Edited));

                            mAdapter.notifyDataSetChanged();

                        } else {


                            mAdapter.notifyDataSetChanged();
                        }


                    }
                    if (sort_category2.equals("Disliked")) {
                        ((AppCompatActivity) ClubPostsList.this).getSupportActionBar().setTitle("Filter: Disliked Topics");

                        if (dislikeS != null && dislikeS.equals("true")) {
                            posts.add(new BlogsObject(UserName, ClubLocation, null, ProfileImage, BlogText, uploadedBy, updated_time, updated_day, likeS, dislikeS, Key, NumberOfLikes, NumberOfDislikes, comments_count,Edited));

                            mAdapter.notifyDataSetChanged();

                        } else {


                            mAdapter.notifyDataSetChanged();
                        }


                    }
                    if (sort_category2.equals("Not reacted")) {
                        ((AppCompatActivity) ClubPostsList.this).getSupportActionBar().setTitle("Filter: Ignored Topics");

                        if ((dislikeS == null || dislikeS.equals("false")) && (likeS == null || likeS.equals("false"))) {
                            posts.add(new BlogsObject(UserName, ClubLocation, null, ProfileImage, BlogText, uploadedBy, updated_time, updated_day, likeS, dislikeS, Key, NumberOfLikes, NumberOfDislikes, comments_count,Edited));

                            mAdapter.notifyDataSetChanged();

                        } else {


                            mAdapter.notifyDataSetChanged();
                        }


                    }

                }
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
        blog_reference.addChildEventListener(childEventListener);

        //it is added to wait for childeventlistener to do its work
        blog_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Toast.makeText(ClubPostsList.this,""+posts.size(),Toast.LENGTH_LONG).show();
                if (posts.size() == 0) {
                    tv_noresults.setVisibility(View.VISIBLE);
                } else {
                    tv_noresults.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}





