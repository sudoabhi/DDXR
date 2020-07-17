package com.example.androdev.ddxr.UserFragments;


import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ablanco.zoomy.Zoomy;
import com.example.androdev.ddxr.ChatBox.HomeMessage;
import com.example.androdev.ddxr.CommonPackage.AboutActivity;
import com.example.androdev.ddxr.CommonPackage.AppFAQsActivity;
import com.example.androdev.ddxr.CommonPackage.ContactUsActivity;
import com.example.androdev.ddxr.CommonPackage.FeedbackActivity;
import com.example.androdev.ddxr.Notification.ManualNotification;
import com.example.androdev.ddxr.PaytmPAYMENT.TicketHistory;
import com.example.androdev.ddxr.R;
import com.example.androdev.ddxr.IntroPackage.ScreenActivity;
import com.example.androdev.ddxr.SignupPackage.PhotoSelector;
import com.example.androdev.ddxr.UserActivities.CustomViewPager2;
import com.example.androdev.ddxr.UserProfilePackage.PeopleClick;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProfile extends Fragment {

    FirebaseDatabase database;
    FirebaseDatabase blog_database;
    DatabaseReference blog_reference;
    ArrayList<BlogsObject> posts;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    DatabaseReference parentReference;
    CircleImageView profile_image;
    TextView user_name;
    TextView user_course;
    TextView college;
    TextView district;
    TextView tv_no_club;
    TextView state;
    NestedScrollView scrollView;


    FirebaseAuth.AuthStateListener mAuthListener;
    AuthUI mAuthUI;
    FirebaseUser user;
    TextView groupAdmin;
    String groupAdminS;
    ImageView edit_profile_image;

    View host_club_view;

    Bitmap original;
    CircleImageView club_profile_image;
    ProgressBar progressBar;
    Bitmap decoded;
    Uri profile_image_url;
    AlertDialog.Builder builder;
    FirebaseStorage storage;
    StorageReference storage_reference;
    ImageView host_setting;
    Boolean club_exists;


    String UserName;
    String key;
    String todays_date;
    String todays_time;
    String monthS;
    String yearS;
    String dayS;
    String am_pm;

    String ImageUrl;
    String UserSkill;
    String  ProfileImage ;
    String  BlogText;
    String  updated_day;
    String  updated_time;
    String uploadedBy;

    FloatingActionButton msg_button;

    String likeS;
    String dislikeS;
    String Key;
    int NumberOfDislikes;
    int NumberOfLikes;
    ProgressBar progressBar2;
    TextView nothing;
    Button follow_bt;
    TextView FollowersCount;
    TextView FollowingCount;
    ProgressBar progressBarX;
    FloatingActionButton share_profile;

    LinearLayout LL_following;
    LinearLayout LL_followers;



    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 7;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 9;


    public FragmentProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_fragment_profile2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LL_followers=(LinearLayout) view.findViewById(R.id.LL_followers);
        LL_following=(LinearLayout) view.findViewById(R.id.LL_following);



        progressBar = (ProgressBar) view.findViewById(R.id.top_progress_bar);
        progressBarX=(ProgressBar) view.findViewById(R.id.progress_barX);
        progressBarX.setVisibility(View.GONE);
        share_profile=(FloatingActionButton) view.findViewById(R.id.share_profile);
        progressBar2=(ProgressBar) view.findViewById(R.id.progress_bar);
        nothing=(TextView) view.findViewById(R.id.nothing);
        follow_bt=(Button)view.findViewById(R.id.follow_bt);
        FollowersCount=(TextView)view.findViewById(R.id.followers_count);
        FollowingCount=(TextView)view.findViewById(R.id.following_count);
        host_setting=(ImageView) view.findViewById(R.id.host_setting);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user==null){
            Toast.makeText(getActivity(),"There was an error. Please log in again.",Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getActivity(), ScreenActivity.class));
            getActivity().finish();
        }
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user==null){
                    Toast.makeText(getActivity(),"There was an error. Please log in again.",Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getActivity(), ScreenActivity.class));
                    getActivity().finish();
                }

            }
        };



        storage = FirebaseStorage.getInstance();
        storage_reference = storage.getReference();
        scrollView=view.findViewById(R.id.scrollView);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        tv_no_club=(TextView)view.findViewById(R.id.tv_no_club);
        host_club_view=(LinearLayout)view.findViewById(R.id.host_club_view);
        msg_button=(FloatingActionButton) view.findViewById(R.id.message);

        database = FirebaseDatabase.getInstance();
        parentReference = database.getReference();


        blog_database = FirebaseDatabase.getInstance("https://ddrx-172d3-2a5f7.firebaseio.com/");
        blog_reference = blog_database.getReference();
      //  user = FirebaseAuth.getInstance().getCurrentUser();
        edit_profile_image = (ImageView) view.findViewById(R.id.edit_profile_image);
        club_profile_image=(CircleImageView) view.findViewById(R.id.club_profile_image);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Profile");
        posts = new ArrayList<>();

        mAdapter = new FragmentProfile.BlogAdapter(getActivity(), posts);

        mLayoutManager = new LinearLayoutManager(getActivity());
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



        getTodaysTime();
        fetch();
        scrollView.scrollTo(0,0);











    user_name = (TextView) view.findViewById(R.id.user_name);
        user_course = (TextView) view.findViewById(R.id.user_course);
        college = (TextView) view.findViewById(R.id.user_college);
        district = (TextView) view.findViewById(R.id.user_district);
        state = (TextView) view.findViewById(R.id.user_state);
        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        groupAdmin = (TextView) view.findViewById(R.id.user_club);



        share_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri="http://hungryhunter96.github.io/profile?Key="+user.getUid();


                Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setLink(Uri.parse(uri))
                        .setDomainUriPrefix("https://ddxr.page.link")
                        .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())

                        .buildShortDynamicLink()
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<ShortDynamicLink>() {
                            @Override
                            public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                                if (task.isSuccessful()) {
                                 //   progress_edit_delete.setVisibility(View.GONE);

                                    Toast.makeText(getActivity(),"Link Copied",Toast.LENGTH_SHORT).show();

                                    Uri shortLink = task.getResult().getShortLink();
                                    Uri flowchartLink = task.getResult().getPreviewLink();

                                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData clip = ClipData.newPlainText(null, shortLink.toString());
                                    if (clipboard == null) return;
                                    clipboard.setPrimaryClip(clip);

                                } else {

                                    Toast.makeText(getActivity(),"Profile link copied",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });





            }
        });

        LL_followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Integer.parseInt(FollowersCount.getText().toString())!=0){

                    Intent intent=new Intent(getActivity(), InterestedPeople.class);
                    intent.putExtra("UserUID",user.getUid());
                    intent.putExtra("Purpose","Followers");
                    startActivity(intent);

                }

            }
        });

        LL_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Integer.parseInt(FollowingCount.getText().toString())!=0){

                    Intent intent=new Intent(getActivity(), InterestedPeople.class);
                    intent.putExtra("UserUID",user.getUid());
                    intent.putExtra("Purpose","Following");
                    startActivity(intent);

                }

            }
        });


        parentReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                user_name.setText(dataSnapshot.child(user.getUid()).child("UserName").getValue(String.class));
                String course = dataSnapshot.child(user.getUid()).child("Course").getValue(String.class);
                String admission_year = dataSnapshot.child(user.getUid()).child("AdmissionYear").getValue(String.class);
                String passing_year = dataSnapshot.child(user.getUid()).child("PassingYear").getValue(String.class);
                String course_details = "" + course + " " + "(" + "" + admission_year + "-" + passing_year + ")";
                user_course.setText(course_details);
                club_exists=false;
                college.setText(dataSnapshot.child(user.getUid()).child("CollegeName").getValue(String.class));
                district.setText(dataSnapshot.child(user.getUid()).child("DistrictName").getValue(String.class));
                state.setText(dataSnapshot.child(user.getUid()).child("StateName").getValue(String.class));
                key=dataSnapshot.child(user.getUid()).child("UploadedBy").getValue(String.class);
                FollowersCount.setText(""+dataSnapshot.child(user.getUid()).child("Followers").getChildrenCount());
                FollowingCount.setText(""+dataSnapshot.child(user.getUid()).child("Following").getChildrenCount());




                if (dataSnapshot.child(user.getUid()).hasChild("ClubName")) {
                    club_exists=true;
                    host_club_view.setVisibility(View.VISIBLE);
                    tv_no_club.setVisibility(View.INVISIBLE);

                    // Toast.makeText(getActivity(),"Hii",Toast.LENGTH_SHORT).show();

                    groupAdminS = dataSnapshot.child(user.getUid()).child("ClubName").getValue(String.class);
                    String s = " " + groupAdminS ;


                    groupAdmin.setText(s);

                   // dataSnapshot.child(user.getUid()).child("ClubProfileImage").getValue(String.class);
                    groupAdmin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Intent intent = new Intent(getActivity(), ClubMainActivity.class);
                            //startActivity(intent);
                        }
                    });

                } else {
                    tv_no_club.setVisibility(View.VISIBLE);
                    host_club_view.setVisibility(View.INVISIBLE);

                    groupAdmin.setOnClickListener(null);
                }


                Picasso.get()
                        .load(dataSnapshot.child(user.getUid()).child("ImageUrl").getValue(String.class))
                        .resize(800, 800)
                        .centerCrop()
                        .noFade()
                        .error(R.drawable.add_photo3) // default image to load
                        .into(profile_image);

                Picasso.get()
                        .load(dataSnapshot.child(user.getUid()).child("ClubImageUrl").getValue(String.class))
                        .resize(800, 800)
                        .centerCrop()
                        .noFade()
                        .error(R.drawable.add_photo3) // default image to load
                        .into(club_profile_image);

                parentReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                       /* if(!club_exists) {
                            if (!key.equals(user.getUid())) {
                                host_setting.setVisibility(View.INVISIBLE);
                            } else {
                                host_setting.setVisibility(View.VISIBLE);
                            }
                        }*/

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


        msg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getActivity(), HomeMessage.class);
                startActivity(intent);

            }
        });




        edit_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditProfileImage();
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditProfileImage();
            }
        });

        follow_bt.setText("Profile Viewers");
        follow_bt.setTextSize(16);
        follow_bt.setPadding(50,10,50,10);

        follow_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(),ProfileViewers.class);
                startActivity(intent);
            }
        });

       /* follow_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(follow_bt.getText().equals("Follow")){
                    follow_bt.setText("Following");


                }
                else{
                    follow_bt.setText("Follow");
                }
            }
        });*/



        //Implementing Listeners


        host_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(club_exists) {

                    if (key.equals(user.getUid())) {
                        open_hostclub_admin();
                    } else {


                        open_host_menu_allusers();

                    }

                }
                else{
                    if (key.equals(user.getUid())) {

                    }
                }








            }
        });




    }

   public void open_hostclub_admin() {

       LayoutInflater li = LayoutInflater.from(getActivity());
       final View promptsView = li.inflate(R.layout.profile_hostclub_admin, null);

       AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
               getActivity());

       // set prompts.xml to alertdialog builder
       alertDialogBuilder.setView(promptsView);


       final AlertDialog alertDialog2 = alertDialogBuilder.create();

       // show it
       alertDialog2.show();

       TextView delete = (TextView) promptsView.findViewById(R.id.delete);
       delete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               deleteclub(key);

               alertDialog2.cancel();

           }

       });

           TextView share = (TextView) promptsView.findViewById(R.id.share);
       share.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               alertDialog2.cancel();

           }

       });




   }

   public void deleteclub(final String key){

       AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
               getActivity());
       final AlertDialog alertDialog = alertDialogBuilder.create();

       // show it
       alertDialog.show();
       alertDialogBuilder.setTitle("Attention !")
               .setMessage("Are you sure you want to delete this post?")
               .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int which) {

                       FirebaseDatabase clubDatabase=FirebaseDatabase.getInstance("https://ddrx-172d3-4390f.firebaseio.com/");
                       DatabaseReference clubReference=clubDatabase.getReference();

                       clubReference.child(key).removeValue();
                       tv_no_club.setVisibility(View.VISIBLE);
                       club_exists=false;
                       host_club_view.setVisibility(View.INVISIBLE);

                       parentReference.child(key).child("ClubName").removeValue();
                       parentReference.child(key).child("ClubImageUrl").removeValue();


                       Toast.makeText(getActivity(),"Club Deleted",Toast.LENGTH_LONG).show();


                       alertDialog.cancel();

                   }
               })










               .setNegativeButton("Cancel",
                       new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {
                               alertDialog.cancel();
                               // dialog.dismiss();
                               //   dialog.cancel();
                               //  dialog.cancel();
                           }
                       })

               // create alert dialog
               .setIcon(android.R.drawable.ic_dialog_alert)
               .show();

   }

    public void open_allusers_menu(){

        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.newsfeed_allusers_options, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

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

    private void open_admin_menu(final String key,final String BlogText,final int position){
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.newsfeed_admin_options, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);


        final AlertDialog alertDialog2 = alertDialogBuilder.create();

        // show it
        alertDialog2.show();

        TextView delete=(TextView)promptsView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteblog(key,position);
                alertDialog2.cancel();


            }
        });

        TextView edit=(TextView)promptsView.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editposts(key,BlogText,position);
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

    private void editposts(final String key, final String blogtext, final int position){
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.edit_posts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText subject = (EditText) promptsView
                .findViewById(R.id.subject);

        subject.setText(blogtext);
        subject.setSelection(subject.getText().length());



        //  final EditText link=(EditText) promptsView.findViewById(R.id.link);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text

                                InputMethodManager imm = (InputMethodManager)getActivity(). getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(subject.getWindowToken(), 0);


                                blog_reference.child(key).child("BlogText").setValue(subject.getText().toString());
                                Map<String, Object> newAchievements = new HashMap<>();
                                posts.get(position).setBlogText(subject.getText().toString());



                                mAdapter.notifyDataSetChanged();
                                Toast.makeText(getActivity(), "Post Edited", Toast.LENGTH_SHORT).show();

















                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                InputMethodManager imm = (InputMethodManager)getActivity(). getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(subject.getWindowToken(), 0);
                                dialog.cancel();



                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

       /* mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v,
                                       int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    mRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerView.smoothScrollToPosition(
                                    position);
                        }
                    }, 100);
                }
            }
        });*/


        // show it
        alertDialog.show();
        subject.requestFocus();
        InputMethodManager imm = (InputMethodManager)getActivity(). getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);







    }

    private void deleteblog(final String key,final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setTitle("Attention !")
                .setMessage("Are you sure you want to delete this post?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        blog_reference.child(key).removeValue();
                        posts.remove(position);
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(),"Post Deleted",Toast.LENGTH_LONG).show();

                    }


                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        })

                // create alert dialog
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }


    /*public void open_hostclub_admin_noclub(){

        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.profile_hostclub_admin_noclub, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);


        final AlertDialog alertDialog2 = alertDialogBuilder.create();

        // show it
        alertDialog2.show();

        TextView add_new_club=(TextView)promptsView.findViewById(R.id.add_new_club);
        add_new_club.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent=new Intent(getActivity(),AddClubActivity.class);
                startActivity(intent);
                alertDialog2.cancel();
            }

            });

    }*/

    public void open_host_menu_allusers(){


        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.profile_hostclub_users, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

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



    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {


            case R.id.logoutbtn:
                Logout();
                return true;

            case R.id.tickets:
                showTicketsActivity();
                return true;

            case R.id.about:
                startActivity(new Intent(getContext(), AboutActivity.class));
                return true;

            case R.id.contact:
                startActivity(new Intent(getContext(), ContactUsActivity.class));
                return true;

            case R.id.feedback:
                startActivity(new Intent(getContext(), FeedbackActivity.class));
                return true;

            case R.id.faq:
                startActivity(new Intent(getContext(), AppFAQsActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }




    public void Logout() {
        mAuthUI = AuthUI.getInstance();

        mAuthUI.signOut(getActivity())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(getActivity(), ScreenActivity.class));
                            getActivity().finish();

                        }
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
                likeS=snapshot.child("LikeStatus").child(user.getUid()).getValue(String.class);
                dislikeS=snapshot.child("DislikeStatus").child(user.getUid()).getValue(String.class);
                Key=snapshot.child("Key").getValue(String.class);
                if(snapshot.hasChild("NumberLikes")){
                    NumberOfLikes=snapshot.child("NumberLikes").getValue(Integer.class);
                }
                 if(snapshot.hasChild("NumberDislikes")){
                     NumberOfDislikes=snapshot.child("NumberDislikes").getValue(Integer.class);
                 }
              //  NumberOfLikes=snapshot.child("NumberLikes").getValue(Integer.class);
             //   NumberOfDislikes=snapshot.child("NumberDislikes").getValue(Integer.class);
                String BlogImage=snapshot.child("BlogPic").getValue(String.class);
                String ClubLocation=snapshot.child("ClubLocation").getValue(String.class);
                String Edited=snapshot.child("Edited").getValue(String.class);
                int comments_count=0;
                if(snapshot.hasChild("CommentsCount")){
                    comments_count= snapshot.child("CommentsCount").getValue(Integer.class);
                }
                if (uploadedBy.equals(user.getUid())) {
                    progressBar2.setVisibility(View.GONE);

                    posts.add(new BlogsObject(UserName,ClubLocation,BlogImage, ProfileImage, BlogText, uploadedBy, updated_time, updated_day, likeS, dislikeS, Key, NumberOfLikes, NumberOfDislikes,comments_count,Edited));
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
                if(posts.size()==0){
                  //  progressBar2.setVisibility(View.GONE);
                    nothing.setVisibility(View.VISIBLE);
                  //  Toast.makeText(getActivity(),"No posts found",Toast.LENGTH_SHORT).show();

                }
                progressBar2.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }




    public void EditProfileImage() {


        Intent intent=new Intent(getContext(), PhotoSelector.class);
        intent.putExtra("Activity","FragmentProfile");
        startActivityForResult(intent,1);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String resultUriS = data.getStringExtra("FragmentProfileStringUri");
                Uri resultUri=Uri.parse(resultUriS);

                if(resultUriS!=null){
                    profile_image.setImageURI(resultUri);
                    new FragmentProfile.CompressionAsyncTask(resultUri,resultUriS).execute();
                }
            }
        }
    }



    /*public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

                != PackageManager.PERMISSION_GRANTED) {

            Log.d("Fragment 13", "" + MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);


            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Toast.makeText(getActivity(), "This is very necessary", Toast.LENGTH_SHORT).show();

            } else {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);


            }
        }
        else{
            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
            return Uri.parse(path);
        }
        return null;


    }*/


    public void uploadimageinbytes(Bitmap bitmap, int quality, Uri uri) {
        final StorageReference mountainsRef = storage_reference.child(user.getUid()).child("profile_images");
        // final StorageReference mountainsRef = storage_reference.child(user.getUid()).child("profile_images/"+uri.getLastPathSegment());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressBar.setVisibility(View.INVISIBLE);

                Toast.makeText(getActivity(), "Successful", Toast.LENGTH_SHORT).show();

            }
        });


        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }


                return mountainsRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    final String downloadUriS = downloadUri.toString();
                    profile_image_url = downloadUri;
                    parentReference.child(user.getUid()).child("ImageUrl").setValue(downloadUriS);
                    FirebaseDatabase.getInstance("https://ddrx-172d3-2a5f7.firebaseio.com/").getReference()
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                      if(snapshot.child("UploadedBy").getValue(String.class).equals(user.getUid())){
                                          String key=snapshot.child("Key").getValue(String.class);
                                          FirebaseDatabase.getInstance("https://ddrx-172d3-2a5f7.firebaseio.com/").getReference().child(key).child("ProfileImage").setValue(downloadUriS);

                                      }
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                    Toast.makeText(getActivity(), "" + downloadUriS, Toast.LENGTH_SHORT).show();
                    Log.d("Fragment 7", "" + downloadUriS);
                } else {

                }
            }

        });


    }


    private class CompressionAsyncTask extends AsyncTask<Uri, Integer, Void> {

        Uri selectedImage;
        String selectedImageS;

        public CompressionAsyncTask(Uri selectedImage, String selectedImageS) {

            this.selectedImage = selectedImage;
            this.selectedImageS = selectedImageS;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected Void doInBackground(Uri... params) {
            try {
                original = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
            } catch (IOException e) {
            }
            uploadimageinbytes(original, 30, selectedImage);


            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            /*Picasso.get()
                    .load(Uri.parse(selectedImageS)) // mCategory.icon is a string
                    .resize(800, 800)
                    .centerCrop()
                    .noFade()
                    .error(R.drawable.add_photo3) // default image to load
                    .into(profile_image);*/


        }


    }


    class BlogAdapter extends RecyclerView.Adapter<com.example.androdev.ddxr.UserFragments.FragmentProfile.BlogAdapter.MyViewHolder>   {

        private List<BlogsObject> posts;
        private Context context;




        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public  class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
           /* View parentView;
            TextView UserName;
            TextView UserSkill;
            CircleImageView ProfileImage;
            TextView ClubLocation;

            TextView BlogText;

            TextView update_day;
            TextView updated_time;
            Button likeButton;
            Button dislikeButton;


            ProgressBar progressBar;*/

            TextView UserName;
            TextView UserSkill;
            CircleImageView ProfileImage;
            ImageView BlogImage;
            TextView ClubLocation;

            TextView BlogText;

            LinearLayout likeButton;
            LinearLayout dislikeButton;
            TextView like_count;
            TextView dislike_count;
            ImageView like_icon;
            ImageView dislike_icon;
            FrameLayout parentView;
            LinearLayout views;
            TextView comments_count;
            TextView time;
            TextView edited_check;


            ProgressBar progressBar;
            public MyViewHolder(View v) {
                super(v);
               /* parentView=(View)v.findViewById(R.id.parent_view) ;
                UserName = (TextView)v.findViewById(R.id.user_name);
                likeButton=(Button) v.findViewById(R.id.like_button);
                dislikeButton=(Button)v.findViewById(R.id.dislike_button);

                BlogText=(TextView)v.findViewById(R.id.blog_text);
                UserSkill=( TextView) v.findViewById(R.id.user_skill);
                ProfileImage=(CircleImageView) v.findViewById(R.id.profile_image);
                update_day=(TextView)v.findViewById(R.id.date);

                updated_time=(TextView)v.findViewById(R.id.time);
                ClubLocation=(TextView) v.findViewById(R.id.skill_location);*/

                parentView = (FrameLayout) v.findViewById(R.id.parent_view);
                edited_check=(TextView)v.findViewById(R.id.edited_check);


                UserName = (TextView) v.findViewById(R.id.user_name);
                likeButton = v.findViewById(R.id.like_button);
                dislikeButton =v.findViewById(R.id.dislike_button);
                BlogImage = (ImageView) v.findViewById(R.id.blog_image);
                comments_count = (TextView) v.findViewById(R.id.comment_count);

                BlogText = (TextView) v.findViewById(R.id.blog_text);
                views = (LinearLayout) v.findViewById(R.id.views);
                UserSkill = (TextView) v.findViewById(R.id.user_skill);
                ProfileImage = (CircleImageView) v.findViewById(R.id.profile_image);

                ClubLocation = (TextView) v.findViewById(R.id.skill_location);
                time=(TextView)v.findViewById(R.id.time);
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
        public com.example.androdev.ddxr.UserFragments.FragmentProfile.BlogAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                                                   int viewType) {
            // create a new view
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.blog2,parent,false);
            com.example.androdev.ddxr.UserFragments.FragmentProfile.BlogAdapter.MyViewHolder vh = new com.example.androdev.ddxr.UserFragments.FragmentProfile.BlogAdapter.MyViewHolder(v);
            return vh;
        }


        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(@NonNull final com.example.androdev.ddxr.UserFragments.FragmentProfile.BlogAdapter.MyViewHolder holder, final int position) {
            final BlogsObject c = posts.get(position);
            holder.parentView.setVisibility(View.INVISIBLE);


            holder.ClubLocation.setText(c.getClubLocation());
            holder.parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), BlogActivity.class);
                    intent.putExtra("State","Students");
                    intent.putExtra("Key", c.getKey());
                    intent.putExtra("DeletePosition", position);
                    startActivity(intent);
                }
            });


            holder.UserName.setText(c.getUserName());
            if(c.getEdited()!=null&&c.getEdited().equals("true")){
                holder.edited_check.setVisibility(View.VISIBLE);
            }
            else{
                holder.edited_check.setVisibility(View.GONE);
            }
            holder.UserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //   Intent intent=new Intent
                }
            });
            //  Toast.makeText(getActivity(),""+posts.getNumberOfLikes(),Toast.LENGTH_SHORT).show();
            holder.like_count.setText(""+c.getNumberOfLikes());
            holder.dislike_count.setText(""+c.getNumberOfDislikes());
            holder.comments_count.setText("" + c.getComments_count());

            Transformation transformation = new Transformation() {

                @Override
                public Bitmap transform(Bitmap source) {
                    int targetWidth;
                    int targetHeight;
                    int phoneHeight;

                    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                    Display display = wm.getDefaultDisplay();
                    DisplayMetrics metrics = new DisplayMetrics();
                    display.getMetrics(metrics);

                    targetWidth = metrics.widthPixels / 2;
                    // phoneHeight=metrics.heightPixels;
                    // targetWidth = holder.EventBanner.getWidth();
                    // if(targetWidth==0){

                    // }
                    Log.e("Width", "" + targetWidth);


                    double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                    targetHeight = (int) (targetWidth * aspectRatio);
                    Log.e("Height", "" + targetHeight);


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

            if (c.getBlogImage() == null) {


                Resources r = getActivity().getResources();
                int px = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        15,
                        r.getDisplayMetrics()
                );

                int px2 = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        10,
                        r.getDisplayMetrics()
                );

                int px3 = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        5,
                        r.getDisplayMetrics()
                );

                holder.parentView.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.views.getLayoutParams();
                params.setMargins(-px3, px3, -px3, -px2);
                holder.views.setLayoutParams(params);
            } else {

                Picasso.get()
                        .load(c.getBlogImage())
                        .error(android.R.drawable.stat_notify_error)

                        .transform(transformation)
                        .into(holder.BlogImage, new Callback() {
                            @Override
                            public void onSuccess() {

                                Resources r = getActivity().getResources();
                                int px = (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP,
                                        15,
                                        r.getDisplayMetrics()
                                );

                                int px2 = (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP,
                                        10,
                                        r.getDisplayMetrics()
                                );

                                int px3 = (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP,
                                        5,
                                        r.getDisplayMetrics()
                                );

                                holder.parentView.setVisibility(View.VISIBLE);
                                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.views.getLayoutParams();
                                params.setMargins(-px3, px2, -px3, -px2);
                                holder.views.setLayoutParams(params);

                                //  holder.parentView.setVisibility(View.VISIBLE);

                            }

                            @Override
                            public void onError(Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                            }
                        });


            }


            Zoomy.Builder builder = new Zoomy.Builder(getActivity()).target(holder.BlogImage);
            builder.register();

            holder.dislikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    int dislikes = c.getNumberOfDislikes();

                    if (c.getDislikeS() == null || c.getDislikeS().equals("false")) {


                        c.setDislikeS("true");


                        dislikes = dislikes + 1;
                        c.setNumberOfDislikes(dislikes);
                        mAdapter.notifyDataSetChanged();

                        blog_reference.child(c.getKey()).child("DislikeStatus").child(user.getUid()).setValue("true");
                        blog_reference.child(c.getKey()).child("Participated").child(user.getUid()).child("2").setValue("Disliked");
                        blog_reference.child(c.getKey()).child("NumberDislikes").setValue(dislikes);


                    } else {
                        c.setDislikeS("false");

                        dislikes = dislikes - 1;
                        c.setNumberOfDislikes(dislikes);
                        mAdapter.notifyDataSetChanged();
                        blog_reference.child(c.getKey()).child("NumberDislikes").setValue(dislikes);
                        blog_reference.child(c.getKey()).child("Participated").child(user.getUid()).child("2").removeValue();


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

                        c.setLikeS("true");
                        c.setNumberOfLikes(likes);
                        mAdapter.notifyDataSetChanged();


                        blog_reference.child(c.getKey()).child("LikeStatus").child(user.getUid()).setValue("true");
                        blog_reference.child(c.getKey()).child("Participated").child(user.getUid()).child("2").setValue("Liked");
                     /*   blog_reference.child(c.getKey()).child("Participated").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               if(dataSnapshot.child(user.getUid()).getValue(String.class).equals("Seen")) {

                                   dataSnapshot.child(user.getUid()).getRef().setValue("Seen and Liked");
                               }
                               else if(dataSnapshot.child(user.getUid()).getValue(String.class).equals("Seen and Commented")) {

                                    dataSnapshot.child(user.getUid()).getRef().setValue("Seen,Liked and Commented");
                                }
                                else{
                                    dataSnapshot.child(user.getUid()).getRef().setValue("Liked");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });*/


                        blog_reference.child(c.getKey()).child("NumberLikes").setValue(likes);


                    } else {


                        likes = likes - 1;
                        c.setLikeS("false");
                        c.setNumberOfLikes(likes);


                        mAdapter.notifyDataSetChanged();
                        blog_reference.child(c.getKey()).child("LikeStatus").child(user.getUid()).setValue("false");
                        blog_reference.child(c.getKey()).child("Participated").child(user.getUid()).child("2").removeValue();
                     /*   blog_reference.child(c.getKey()).child("Participated").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.child(user.getUid()).getValue(String.class).equals("Liked")) {

                                    dataSnapshot.child(user.getUid()).getRef().removeValue();
                                }
                                else if(dataSnapshot.child(user.getUid()).getValue(String.class).equals("Seen,Liked and Commented")) {

                                    dataSnapshot.child(user.getUid()).getRef().setValue("Seen and Commented");
                                }
                                else if(dataSnapshot.child(user.getUid()).getValue(String.class).equals("Seen and Liked")) {

                                    dataSnapshot.child(user.getUid()).getRef().setValue("Seen");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });*/


                        blog_reference.child(c.getKey()).child("NumberLikes").setValue(likes);


                    }

                }
            });

            if (c.getLikeS() != null && c.getLikeS().equals("true")) {
                holder.dislikeButton.setClickable(false);
                int tintColor = ContextCompat.getColor(getActivity(), android.R.color.holo_blue_dark);
                holder.like_icon.setBackgroundTintList(ColorStateList.valueOf(tintColor));
                holder.dislike_icon.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), android.R.color.darker_gray)));

            } else {
                int tintColor = ContextCompat.getColor(getActivity(), android.R.color.darker_gray);
                holder.dislikeButton.setClickable(true);
                //holder.like_icon.setBackgroundTintList(ColorStateList.valueOf(tintColor));


            }
            if (c.getDislikeS() != null && c.getDislikeS().equals("true")) {
                holder.likeButton.setClickable(false);
                int tintColor = ContextCompat.getColor(getActivity(), android.R.color.holo_red_dark);
                holder.dislike_icon.setBackgroundTintList(ColorStateList.valueOf(tintColor));
                holder.like_icon.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), android.R.color.darker_gray)));


            } else {
                int tintColor = ContextCompat.getColor(getActivity(), android.R.color.darker_gray);
                holder.likeButton.setClickable(true);
                //holder.dislike_icon.setBackgroundTintList(ColorStateList.valueOf(tintColor));

            }


            //  setOnItemLongClickLister(mListener);

            holder.parentView.setOnLongClickListener(new View.OnLongClickListener() {
                                                         @Override
                                                         public boolean onLongClick(View view) {

                                                             //blog_et.setFocusable(false);
                                                             if (user.getUid().equals(c.getUploadedBy())) {
                                                                 open_admin_menu(c.getKey(), c.getBlogText(), position);


                                                             } else {
                                                                 open_allusers_menu();

                                                             }

                                        /*                     menu=new PopupMenu(getActivity(),holder.UserSkill);
                                                             //  menu.inflate(R.menu.blog_holdclick);
                                                             if(user.getUid().equals(c.getUploadedBy())){

                                                                 menu.getMenu().add(Menu.NONE, MENU_ITEM_EDIT, Menu.NONE, "EDIT").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                                                     @Override
                                                                     public boolean onMenuItemClick(MenuItem menuItem) {
                                                                         // Toast.makeText(getActivity(), " Hii"+posts.getKey(), Toast.LENGTH_SHORT).show();
                                                                         String blogtext=c.getBlogText();




                                                                         editposts(c.getKey(),blogtext,position);


                                                                         *//*InputMethodManager imm = (InputMethodManager)getActivity(). getSystemService(Context.INPUT_METHOD_SERVICE);
                                                                         imm.hideSoftInputFromWindow(blog_et.getWindowToken(), 0);*//*

                                                                        // mLayoutManager.scrollToPositionWithOffset(position,10);









                                                                         return true;
                                                                     }
                                                                 });
                                                                 menu.getMenu().add(Menu.NONE, MENU_ITEM_DELETE, Menu.NONE, "DELETE").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                                                     @Override
                                                                     public boolean onMenuItemClick(MenuItem menuItem) {
                                                                         deleteblog(c.getKey(),position);
                                                                         return true;
                                                                     }
                                                                 });
                                                             }
                                                             else{
                                                                 menu.getMenu().add(Menu.NONE, MENU_ITEM_REPORT, Menu.NONE, "REPORT");
                                                                 //   menu.getMenu().add("Hide");
                                                             }

                                                             menu.show();*/
                                                             //blog_et.setFocusable(true);
                                                             return true;


                                                         }


                                                     }


            );


            String time=c.getUpdatedTime();
            String date=c.getUpdatedDay();
            if(todays_date.equals(date)){
                if(todays_time.equals(c.getUpdatedTime())){
                    holder.time.setText("Right Now");
                }
                else{
                    holder.time.setText(time);
                }

            }
            else{
                holder.time.setText(date);
            }




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

                            Toast.makeText(getActivity(), "Failed to load new posts", Toast.LENGTH_SHORT).show();

                        }


                    });



        }

/*            final BlogsObject c = posts.get(position);
            holder.ClubLocation.setText(c.getClubLocation());

            holder.UserName.setText(c.getUserName());
            holder.UserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //   Intent intent=new Intent
                }
            });
            //  Toast.makeText(getActivity(),""+posts.getNumberOfLikes(),Toast.LENGTH_SHORT).show();
            holder.dislikeButton.setHint("("+c.getNumberOfDislikes()+")");
            holder.likeButton.setHint("("+c.getNumberOfLikes()+")");

            holder.dislikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    int dislikes= c.getNumberOfDislikes();

                    if( c.getDislikeS()==null||c.getDislikeS().equals("false")){


                        c.setDislikeS("true");


                        dislikes=dislikes+1;
                        c.setNumberOfDislikes(dislikes);
                        mAdapter.notifyDataSetChanged();

                        blog_reference.child(c.getKey()).child("DislikeStatus").child(user.getUid()).setValue("true");
                        blog_reference.child(c.getKey()).child("NumberDislikes").setValue(dislikes);








                    }
                    else{
                        c.setDislikeS("false");

                        dislikes=dislikes-1;
                        c.setNumberOfDislikes(dislikes);
                        mAdapter.notifyDataSetChanged();
                        blog_reference.child(c.getKey()).child("NumberDislikes").setValue(dislikes);


                        blog_reference.child(c.getKey()).child("DislikeStatus").child(user.getUid()).setValue("false");





                    }

                }
            });



            holder.likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int likes=c.getNumberOfLikes();

                    if(c.getLikeS()==null||c.getLikeS().equals("false")){


                        likes=likes+1;

                        c.setLikeS("true");
                        c.setNumberOfLikes(likes);
                        mAdapter.notifyDataSetChanged();


                        blog_reference.child(c.getKey()).child("LikeStatus").child(user.getUid()).setValue("true");






                        blog_reference.child(c.getKey()).child("NumberLikes").setValue(likes);








                    }
                    else{


                        likes=likes-1;
                        c.setLikeS("false");
                        c.setNumberOfLikes(likes);


                        mAdapter.notifyDataSetChanged();
                        blog_reference.child(c.getKey()).child("LikeStatus").child(user.getUid()).setValue("false");


                        blog_reference.child(c.getKey()).child("NumberLikes").setValue(likes);









                    }

                }
            });

            if(c.getLikeS()!=null&&c.getLikeS().equals("true")){
                holder.dislikeButton.setClickable(false);
                int tintColor = ContextCompat.getColor(getActivity(), android.R.color.holo_blue_dark);
                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.ic_thumb_up_black_24dp);
                drawable = DrawableCompat.wrap(drawable);
                DrawableCompat.setTint(drawable.mutate(), tintColor);

                drawable.setBounds( 0, 0, 90, 90);

                holder.likeButton.setCompoundDrawables(drawable, null, null, null);

            }
            else{
                int tintColor = ContextCompat.getColor(getActivity(), android.R.color.darker_gray);
                holder.dislikeButton.setClickable(true);
                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.ic_thumb_up_black_24dp);
                drawable = DrawableCompat.wrap(drawable);
                DrawableCompat.setTint(drawable.mutate(), tintColor);

                drawable.setBounds( 0, 0, 75, 75);

                holder.likeButton.setCompoundDrawables(drawable, null, null, null);


            }
            if(c.getDislikeS()!=null&&c.getDislikeS().equals("true")){
                holder.likeButton.setClickable(false);
                int tintColor = ContextCompat.getColor(getActivity(), android.R.color.holo_blue_dark);
                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.ic_thumb_down_black_24dp);
                drawable = DrawableCompat.wrap(drawable);
                DrawableCompat.setTint(drawable.mutate(), tintColor);

                drawable.setBounds( 0, 0, 90, 90);

                holder.dislikeButton.setCompoundDrawables(drawable, null, null, null);
            }
            else{
                int tintColor = ContextCompat.getColor(getActivity(), android.R.color.darker_gray);
                holder.likeButton.setClickable(true);
                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.ic_thumb_down_black_24dp);
                drawable = DrawableCompat.wrap(drawable);
                DrawableCompat.setTint(drawable.mutate(), tintColor);

                drawable.setBounds( 0, 0, 75, 75);

                holder.dislikeButton.setCompoundDrawables(drawable, null, null, null);


            }


            //  setOnItemLongClickLister(mListener);

            holder.parentView.setOnLongClickListener(new View.OnLongClickListener() {
                                                         @Override
                                                         public boolean onLongClick(View view)
                                                         {

                                                           //  blog_et.setFocusable(false);
                                                             if(user.getUid().equals(c.getUploadedBy())) {
                                                                 open_admin_menu(c.getKey(),c.getBlogText(),position);


                                                             }else{
                                                                 open_allusers_menu();

                                                             }

                                        *//*                     menu=new PopupMenu(getActivity(),holder.UserSkill);
                                                             //  menu.inflate(R.menu.blog_holdclick);
                                                             if(user.getUid().equals(c.getUploadedBy())){

                                                                 menu.getMenu().add(Menu.NONE, MENU_ITEM_EDIT, Menu.NONE, "EDIT").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                                                     @Override
                                                                     public boolean onMenuItemClick(MenuItem menuItem) {
                                                                         // Toast.makeText(getActivity(), " Hii"+posts.getKey(), Toast.LENGTH_SHORT).show();
                                                                         String blogtext=c.getBlogText();




                                                                         editposts(c.getKey(),blogtext,position);


                                                                         *//**//*InputMethodManager imm = (InputMethodManager)getActivity(). getSystemService(Context.INPUT_METHOD_SERVICE);
                                                                         imm.hideSoftInputFromWindow(blog_et.getWindowToken(), 0);*//**//*

                                                                        // mLayoutManager.scrollToPositionWithOffset(position,10);









                                                                         return true;
                                                                     }
                                                                 });
                                                                 menu.getMenu().add(Menu.NONE, MENU_ITEM_DELETE, Menu.NONE, "DELETE").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                                                     @Override
                                                                     public boolean onMenuItemClick(MenuItem menuItem) {
                                                                         deleteblog(c.getKey(),position);
                                                                         return true;
                                                                     }
                                                                 });
                                                             }
                                                             else{
                                                                 menu.getMenu().add(Menu.NONE, MENU_ITEM_REPORT, Menu.NONE, "REPORT");
                                                                 //   menu.getMenu().add("Hide");
                                                             }

                                                             menu.show();*//*
                                                            // blog_et.setFocusable(true);
                                                             return true;


                                                         }




                                                     }








            );

















         if(holder.update_day!=null){
             holder.update_day.setText(c.getUpdatedDay());
         }

         if(holder.updated_time!=null){
             holder.updated_time.setText(c.getUpdatedTime());
         }




            holder.BlogText.setText(c.getBlogText());




            Picasso.get()
                    .load(c.getImageUrl())
                    .error(android.R.drawable.stat_notify_error)
                    .resize(400,400)
                    .centerCrop()
                    // .transform(transformation)
                    .into(holder.ProfileImage, new Callback() {
                        @Override
                        public void onSuccess() {




                        }

                        @Override
                        public void onError(Exception e) {

                            Toast.makeText(getActivity(), "Failed to load new posts", Toast.LENGTH_SHORT).show();

                        }


                    });







        }*/


        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {



            return posts.size();
        }

        public  int getCount(){

            return  posts.size();

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


    public void showTicketsActivity(){

        Intent intent=new Intent(getActivity(), TicketHistory.class);
        startActivity(intent);







    }



}


























