package com.example.androdev.ddxr.SearchFragments;


import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.androdev.ddxr.R;
import com.example.androdev.ddxr.UserFragments.BlogsObject;
import com.example.androdev.ddxr.UserFragments.FragmentSearch;
import com.example.androdev.ddxr.UserFragments.MainUserActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchPeople extends Fragment implements TextWatcher {

    FirebaseDatabase parentDatabase;
    DatabaseReference parentReference;
    String UserUID;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    FirebaseRecyclerAdapter adapter2;
    RecyclerView.Adapter mAdapter;
    ArrayList<SearchPeopleObject> posts;
    ArrayList<SearchPeopleObject> newList;
    LottieAnimationView animation;

    String UserName;
    String UserSkill;
    String UserCollege;
    String ProfileImage;
    FirebaseUser user;
    String k;
    String l;
    ArrayList<String> A;
    Integer AdapterCount;


    public SearchPeople() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_search_people, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        animation=(LottieAnimationView) view.findViewById(R.id.animation_view);
        AdapterCount=0;

        //animation.cancelAnimation();
       // animation.addAnimatorListener();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);
        A = new ArrayList<>();
        posts = new ArrayList<>();
        posts.clear();
        newList = new ArrayList<>();
        newList.clear();
        user= FirebaseAuth.getInstance().getCurrentUser();









        mRecyclerView.setNestedScrollingEnabled(false
        );
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        FragmentSearch.SearchEdittext.addTextChangedListener(this);


        parentDatabase = FirebaseDatabase.getInstance("https://ddrx-172d3.firebaseio.com/");
        parentReference = parentDatabase.getReference();
        mAdapter = new PeopleAdapter(getActivity(), newList);
       // Toast.makeText(getActivity(),""+mAdapter.getItemCount(),Toast.LENGTH_SHORT).show();



        mRecyclerView.setAdapter(mAdapter);


    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

       animation.setVisibility(View.VISIBLE);
        AdapterCount=0;
        k = FragmentSearch.getEdittext();
         posts.clear();
         newList.clear();
         mAdapter.notifyDataSetChanged();

        if (k.length() == 0) {
            animation.setVisibility(View.VISIBLE);

            posts.clear();
            newList.clear();
            mAdapter.notifyDataSetChanged();


            // fetch(k);
            //  adapter2.stopListening();

        } else {
            //   fetch(k);
            //    adapter2.startListening();
          //  Toast.makeText(getActivity(),""+mAdapter.getItemCount(),Toast.LENGTH_SHORT).show();


            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    UserName= dataSnapshot.child("UserName").getValue(String.class);
                    A.add(UserName);

                    ProfileImage = dataSnapshot.child("ImageUrl").getValue(String.class);
                    UserCollege=dataSnapshot.child("ClubLocation").getValue(String.class);
                    UserUID=dataSnapshot.child("UserUID").getValue(String.class);
                    UserSkill=dataSnapshot.child("UserSkill").getValue(String.class);




                   if(!user.getUid().equals(UserUID)){
                       posts.add(new SearchPeopleObject(ProfileImage,UserName, UserSkill, UserCollege,UserUID));
                   }

                    newList = removeDuplicateDistricts(posts, posts.size());
                   // AdapterCount=AdapterCount+1;
                    Log.e("Change",String.valueOf(A));
                    mAdapter.notifyDataSetChanged();

                    PeopleAdapter adapter=new PeopleAdapter(getActivity(),posts);
                    AdapterCount= adapter.getCount();
                 //  Toast.makeText(getActivity(),""+AdapterCount,Toast.LENGTH_SHORT).show();
                    if(AdapterCount==0){
                        animation.setVisibility(View.VISIBLE);
                    }
                    else animation.setVisibility(View.GONE);

                    if (k.length() == 0) {
                        animation.setVisibility(View.VISIBLE);

                        posts.clear();
                        newList.clear();
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



            Query query = parentReference.orderByChild("UserName").startAt(k).endAt(k + "\uf8ff");
            query.addChildEventListener(childEventListener);


            posts.clear();


          //  animation.setVisibility(View.VISIBLE);


            A.clear();
            //  Query query=  parentReference.orderByChild("UserName").startAt(k);
         /*   query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                    while (iterator.hasNext()) {
                        DataSnapshot next = (DataSnapshot) iterator.next();

                        UserName= next.child("UserName").getValue(String.class);

                        ProfileImage = next.child("ImageUrl").getValue(String.class);
                        UserCollege=next.child("CollegeName").getValue(String.class);

                        posts.clear();

                        posts.add(new SearchPeopleObject(ProfileImage,UserName, UserSkill, UserCollege));


                        mAdapter.notifyDataSetChanged();


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }*/
        }
    }




    @Override
    public void afterTextChanged(Editable editable) {

        k = editable.toString();

        if (k.length() > 0) {

            }
            //   fetch(k);
            //    adapter2.startListening();


          /*  Query query=  parentReference.orderByChild("UserName").startAt(k).endAt(k + "\uf8ff");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot shot:dataSnapshot.getChildren()){

                        UserName= shot.child("UserName").getValue(String.class);

                        ProfileImage = shot.child("ImageUrl").getValue(String.class);
                        UserCollege=shot.child("CollegeName").getValue(String.class);

                        posts.add(new SearchPeopleObject(ProfileImage,UserName, UserSkill, UserCollege));


                        mAdapter.notifyDataSetChanged();


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }


    }*/

        }

    private ArrayList<SearchPeopleObject> removeDuplicateDistricts(ArrayList<SearchPeopleObject> list, int size) {


        for (SearchPeopleObject element : list) {

            // If this element is not present in newList
            // then add it
           // String s = element.getClubName();

            //  Toast.makeText(getActivity(),"hello"+element.getClubName(),Toast.LENGTH_SHORT).show();
            if (!newList.contains(element)) {

                if (newList.size() > 0) {

                    int sizeS = newList.size();
                    for(int i=0; i<newList.size(); i++) {

                        //Yaha thoda or differentiae kro using userskill
                        if(newList.get(i)!=null&&(newList.get(i).getUserUID().equals(element.getUserUID()))){
                          //  Toast.makeText(getActivity(), "hello" + element.getUserName(), Toast.LENGTH_LONG).show();
                            break;

                        }

                     /*   if ((newList.get(i).getUserName().equals(element.getUserName()))&&(newList.get(i).getUserCollege().equals(element.getUserCollege()))&&(newList.get(i).getUserName().equals(element.getUserName()))) {

                            Toast.makeText(getActivity(), "hello" + element.getUserName(), Toast.LENGTH_LONG).show();
                            break;*/




                   //     }
                        else{
                            if(i==newList.size()-1){
                                newList.add(element);
                            }
                        }


                    }



                } else{


                    newList.add(element);
                  //  Toast.makeText(getActivity(), "hii" + element.getUserName(), Toast.LENGTH_LONG).show();
                }


            }







        }
        return newList;


    }

    }



   /* @Override
    public void onDestroy() {
        super.onDestroy();
        adapter2.stopListening();
    }*/

  /*  private void fetch(String k){



        Query query = parentReference.orderByChild("UserName").startAt(k).endAt(k + "\uf8ff").limitToFirst(10);

        FirebaseRecyclerOptions<SearchPeopleObject> options =
                new FirebaseRecyclerOptions.Builder<SearchPeopleObject>()
                        .setQuery(query, new SnapshotParser<SearchPeopleObject>() {
                            @NonNull
                            @Override
                            public SearchPeopleObject parseSnapshot(@NonNull DataSnapshot snapshot) {

                                for (DataSnapshot post : snapshot.getChildren()) {
                                    // Iterate through all posts with the same author
                                    UserName = post.child("UserName").getValue(String.class);
                                    A.add(UserName);


                                }
                                Log.e("Checking",""+String.valueOf(A));



                              UserSkill="Android Development";


                                UserName= snapshot.child("UserName").getValue(String.class);

                                ProfileImage = snapshot.child("ImageUrl").getValue(String.class);
                                UserCollege=snapshot.child("CollegeName").getValue(String.class);



                                return new SearchPeopleObject(ProfileImage,UserName, UserSkill, UserCollege);
                            }
                        })
                        .build();

        adapter2 = new FirebaseRecyclerAdapter<SearchPeopleObject,com.example.androdev.ddxr.SearchFragments.PeopleViewHolder>(options) {


            @Override
            public com.example.androdev.ddxr.SearchFragments.PeopleViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                             int viewType) {

                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_people_model,parent,false);
                com.example.androdev.ddxr.SearchFragments.PeopleViewHolder vh = new com.example.androdev.ddxr.SearchFragments.PeopleViewHolder(v);
                return vh;
            }

            // Replace the contents of a view (invoked by the layout manager)
            @Override
            protected void onBindViewHolder(@NonNull final com.example.androdev.ddxr.SearchFragments.PeopleViewHolder holder, int position, @NonNull final SearchPeopleObject posts) {


                holder.UserName.setText(posts.getUserName());
                holder.UserName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

              holder.UserCollege.setText(posts.getUserCollege());





                Picasso.get()
                        .load(posts.getImageUrl())
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







            }


        };

        mRecyclerView.setAdapter(adapter2);

    }














}











class PeopleViewHolder extends RecyclerView.ViewHolder {
    // each data item is just a string in this case
   CircleImageView ProfileImage;
   TextView UserName;
    TextView UserSkill;
    TextView UserCollege;


    ProgressBar progressBar;


    public PeopleViewHolder(View v) {
        super(v);

        UserName = (TextView)v.findViewById(R.id.user_name);
        UserCollege=(TextView) v.findViewById(R.id.user_college);
        UserSkill=( TextView) v.findViewById(R.id.skill);
        ProfileImage=(CircleImageView) v.findViewById(R.id.profile_image);


    }


}*/






































