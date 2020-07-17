package com.example.androdev.ddxr.SignupPackage;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androdev.ddxr.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class SignUpActivity3 extends AppCompatActivity {


    Toolbar mytoolbar;
    ArrayAdapter adapter;
    ArrayAdapter adapter2;
    ArrayAdapter adapter3;
    ArrayList<String> states;
    ArrayList<String> districts;
    ArrayList<String> colleges;
    String state_name;
    String district_name;
    String college_name;
    String rollnumS;
    String phoneS;
    EditText rollnumT;
    EditText phoneT;
    Button next;
    TextView progresstv;
    ProgressBar progress;

    LinearLayout layout;
    TextInputLayout input;
    TextInputLayout input2;


    ProgressBar LayoutprogressBar;
    FrameLayout content;


    FirebaseDatabase database;
    FirebaseDatabase parentDatabase;
    DatabaseReference parentdatabaseReference;
    DatabaseReference databaseReference;
    FirebaseUser user;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment3);


        database = FirebaseDatabase.getInstance("https://ddrx-college-list.firebaseio.com/");
        parentDatabase=FirebaseDatabase.getInstance();
        parentdatabaseReference=parentDatabase.getReference();
        databaseReference = database.getReference();
        user= FirebaseAuth.getInstance().getCurrentUser();

        progress=findViewById(R.id.myProgress);
        progresstv=findViewById(R.id.myTextProgress);

        phoneT=findViewById(R.id.phone);
        rollnumT=findViewById(R.id.rollnum);

        layout=findViewById(R.id.lay1);
        input=findViewById(R.id.input);
        input2=findViewById(R.id.input2);

        next =findViewById(R.id.next);
        next.setEnabled(false);

        LayoutprogressBar=(ProgressBar) findViewById(R.id.progressBar);
        LayoutprogressBar.setVisibility(View.INVISIBLE);
        content=findViewById(R.id.content);

        states=new ArrayList<>();
        districts=new ArrayList<>();
        colleges=new ArrayList<>();


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rollnumS=rollnumT.getText().toString();
                phoneS=phoneT.getText().toString();
                if(college_name.equals(" Select")|| TextUtils.isEmpty(rollnumS)||TextUtils.isEmpty(phoneS)){
                    next.setClickable(true);
                    LayoutprogressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(SignUpActivity3.this, "Please input the required details.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    next.setClickable(false);
                    LayoutprogressBar.setVisibility(View.VISIBLE);
                    content.setForeground(new ColorDrawable(Color.parseColor("#60000000")));
                    input.setVisibility(View.GONE);
                    input2.setVisibility(View.GONE);
                    layout.setVisibility(View.GONE);
                    next.setVisibility(View.GONE);
                    progress.setVisibility(View.VISIBLE);
                    progresstv.setVisibility(View.VISIBLE);
                    sendData();
                    //Toast.makeText(getContext(), "Registration Successful", Toast.LENGTH_SHORT).show();




                }

            }
        });




        Spinner show_states=(Spinner)findViewById(R.id.state);
        Spinner show_districts=(Spinner)findViewById(R.id.district);
        Spinner show_colleges=(Spinner)findViewById(R.id.college);


        states.add("Loading");
        create_states_list();
        adapter = new ArrayAdapter(SignUpActivity3.this,android.R.layout.simple_list_item_1 ,states);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        show_states.setAdapter(adapter);


        show_states.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                adapter3.clear();
                adapter2.clear();
                state_name=adapterView.getItemAtPosition(i).toString();

                create_district_list();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        districts.add("Loading");
        adapter2=new ArrayAdapter(SignUpActivity3.this,android.R.layout.simple_list_item_1,districts);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        show_districts.setAdapter(adapter2);


        show_districts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                adapter3.clear();
                district_name=adapterView.getItemAtPosition(i).toString();

                create_college_list();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        colleges.add("Loading");
        adapter3=new ArrayAdapter(SignUpActivity3.this,android.R.layout.simple_list_item_1,colleges);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        show_colleges.setAdapter(adapter3);

        show_colleges.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                college_name=adapterView.getItemAtPosition(i).toString();
                //Toast.makeText(getActivity(),""+college_name,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });







    }

    public void create_states_list(){

        states.clear();
        databaseReference.child("State").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                    String statesS;

                    statesS = uniqueKeySnapshot.getKey();
                    if(statesS!=null){
                        states.add(statesS);
                    }


                }
                Collections.sort(states);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






    }

    public void create_district_list(){

        districts.clear();
        databaseReference.child("State").child(""+state_name).child("District").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                districts.clear();

                for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()){

                    String districtsS;

                    districtsS=uniqueKeySnapshot.getKey();
                    if(districtsS!=null){
                        districts.add(districtsS);
                    }




                }
                Collections.sort(districts);
                adapter2.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    public void create_college_list(){

        colleges.clear();
        databaseReference.child("State").child(""+state_name).child("District").child(""+district_name).child("CllgName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                colleges.clear();

                if(dataSnapshot.getChildrenCount()<2){

                    String collegeS;
                    collegeS=dataSnapshot.getValue(String.class);
                    if(collegeS!=null){
                        colleges.add(collegeS);
                    }

                }
                else{

                    for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()){

                        String collegesS;

                        collegesS=uniqueKeySnapshot.getValue(String.class);
                        if(collegesS!=null){
                            colleges.add(collegesS);
                        }



                    }

                }

                next.setEnabled(true);
                Collections.sort(colleges);
                adapter3.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void sendData()
    {




        parentdatabaseReference.child(user.getUid()).child("ClubLocation").setValue(college_name + "," + district_name)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        next.setClickable(true);
                        LayoutprogressBar.setVisibility(View.INVISIBLE);
                        content.setForeground(null);
                        Toast.makeText(SignUpActivity3.this, "An error occurred, with error message:  "+e.toString(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                });

        parentdatabaseReference.child(user.getUid()).child("CollegeName").setValue(college_name);
        parentdatabaseReference.child(user.getUid()).child("DistrictName").setValue(district_name);
        parentdatabaseReference.child(user.getUid()).child("StateName").setValue(state_name);
        parentdatabaseReference.child(user.getUid()).child("UserUID").setValue(user.getUid());
        parentdatabaseReference.child(user.getUid()).child("Registered").setValue("True");
        parentdatabaseReference.child(user.getUid()).child("UserType").setValue("Student");
        parentdatabaseReference.child(user.getUid()).child("Phone").setValue(phoneS);
        parentdatabaseReference.child(user.getUid()).child("RollNum").setValue(rollnumS)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        next.setClickable(true);
                        LayoutprogressBar.setVisibility(View.INVISIBLE);
                        content.setForeground(null);
                        //Toast.makeText(SignUpActivity3.this, "Registering...", Toast.LENGTH_SHORT).show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                startActivity(new Intent(SignUpActivity3.this, WelcomeActivity.class));
                            }
                        }, 3000);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        next.setClickable(true);
                        LayoutprogressBar.setVisibility(View.INVISIBLE);
                        content.setForeground(null);
                        Toast.makeText(SignUpActivity3.this, "An error occurred, with error message:  "+e.toString(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                });




    }

}
