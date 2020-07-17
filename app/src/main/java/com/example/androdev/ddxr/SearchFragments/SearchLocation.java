package com.example.androdev.ddxr.SearchFragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.androdev.ddxr.R;
import com.example.androdev.ddxr.UserFragments.FragmentSearch;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchLocation extends Fragment implements TextWatcher {

    FirebaseDatabase ClubDatabase;
    DatabaseReference ClubReference;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;

    RecyclerView.Adapter mAdapter;
    ArrayList<SearchCollegeObject> newList;
    ArrayList<SearchCollegeObject> posts;
    LottieAnimationView animation;

    String ClubName;
    String CollegeName;
    String ClubFollowers;
    String ClubProfileImage;

    String k;

    ArrayList<String> A;
    Integer AdapterCount;
    String l;



    public SearchLocation() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        animation=(LottieAnimationView) view.findViewById(R.id.animation_view);
        AdapterCount=0;


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








        mRecyclerView.setNestedScrollingEnabled(false
        );
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        FragmentSearch.SearchEdittext.addTextChangedListener(this);


        ClubDatabase = FirebaseDatabase.getInstance("https://ddrx-172d3-4390f.firebaseio.com/");
        ClubReference = ClubDatabase.getReference();
        mAdapter = new CollegeAdapter(getActivity(), newList);




        mRecyclerView.setAdapter(mAdapter);


    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        animation.setVisibility(View.VISIBLE);
        AdapterCount = 0;
        k = FragmentSearch.getEdittext();

        posts.clear();
        newList.clear();
        mAdapter.notifyDataSetChanged();

        if (k.length() == 0) {
            animation.setVisibility(View.VISIBLE);

            posts.clear();
            newList.clear();
            mAdapter.notifyDataSetChanged();




        } else {



            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    ClubName= dataSnapshot.child("ClubName").getValue(String.class);
                    //  A.add(UserName);

                    ClubProfileImage = dataSnapshot.child("ClubImageUrl").getValue(String.class);
                    CollegeName=dataSnapshot.child("t").getValue(String.class);

                    ClubFollowers="14,589";



                    posts.add(new SearchCollegeObject(ClubProfileImage,ClubName,CollegeName, ClubFollowers));
                    newList = removeDuplicateDistricts(posts, posts.size());

                    Log.e("Change",String.valueOf(A));
                    mAdapter.notifyDataSetChanged();

                    CollegeAdapter adapter=new CollegeAdapter(getActivity(),posts);
                    AdapterCount= adapter.getCount();

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



            Query query = ClubReference.orderByChild("DistrictName").startAt(k).endAt(k + "\uf8ff");
            query.addChildEventListener(childEventListener);




            posts.clear();
            A.clear();

        }
    }




    @Override
    public void afterTextChanged(Editable editable) {


    }

  /*  public String solveCapitalCharater(String s){
        if(s.equals("a")){
            return "A";
        }
        if(s.equals("b")){
            return "B";
        }
        if(s.equals("c")){
            return "C";
        }
        if(s.equals("d")){
            return "D";
        }
        if(s.equals("e")){
            return "E";
        }
        if(s.equals("f")){
            return "F";
        }
        if(s.equals("g")){
            return "G";
        }
        if(s.equals("h")){
            return "H";
        }
        if(s.equals("i")){
            return "I";
        }
        if(s.equals("j")){
            return "J";
        }
        if(s.equals("k")){
            return "K";
        }
        if(s.equals("l")){
            return "L";
        }
        if(s.equals("m")){
            return "M";
        }
        if(s.equals("n")){
            return "N";
        }
        if(s.equals("o")){
            return "O";
        }
        if(s.equals("p")){
            return "P";
        }
        if(s.equals("q")){
            return "Q";
        }
        if(s.equals("r")){
            return "R";
        }
        if(s.equals("s")){
            return "S";
        }
        if(s.equals("t")){
            return "T";
        }
        if(s.equals("u")){
            return "U";
        }
        if(s.equals("v")){
            return "V";
        }
        if(s.equals("w")){
            return "W";
        }
        if(s.equals("x")){
            return "X";
        }
        if(s.equals("y")){
            return "Y";
        }
        if(s.equals("z")){
            return "Z";
        }
        else return s;


    }*/
  private ArrayList<SearchCollegeObject> removeDuplicateDistricts(ArrayList<SearchCollegeObject> list, int size) {


      for (SearchCollegeObject element : list) {

          // If this element is not present in newList
          // then add it
          String s = element.getClubName();

          //  Toast.makeText(getActivity(),"hello"+element.getClubName(),Toast.LENGTH_SHORT).show();
          if (!newList.contains(element)) {

              if (newList.size() > 0) {

                  int sizeS = newList.size();
                  for(int i=0; i<newList.size(); i++) {

                      if (newList.get(i).getClubName().equals(element.getClubName())) {

                       //   Toast.makeText(getActivity(), "hello" + element.getClubName(), Toast.LENGTH_LONG).show();
                          break;




                      }
                      else{
                          if(i==newList.size()-1){
                              newList.add(element);
                          }
                      }


                  }



              } else{


                  newList.add(element);
                //  Toast.makeText(getActivity(), "hii" + element.getClubName(), Toast.LENGTH_LONG).show();
              }


          }







      }
      return newList;


  }

}







