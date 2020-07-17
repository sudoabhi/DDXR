package com.example.androdev.ddxr.SignupPackage;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.androdev.ddxr.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity4 extends AppCompatActivity {

    Toolbar mytoolbar;
    ArrayAdapter adapter;
    ArrayAdapter adapter2;
    ArrayAdapter adapter3;
    String course;
    String ad_year;
    String pass_year;
    String branchS;
    String course_name[];
    String admission_year[];
    String passing_year[];
    TextInputEditText branch_name;
    Button next_fragment;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseUser currentUser;

    ProgressBar LayoutprogressBar;
    FrameLayout content;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment4);


        database=FirebaseDatabase.getInstance();
        reference=database.getReference();
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        branch_name=findViewById(R.id.branch_n);
        next_fragment=(Button)findViewById(R.id.next_frag);

        LayoutprogressBar=(ProgressBar) findViewById(R.id.progressBar);
        LayoutprogressBar.setVisibility(View.INVISIBLE);
        content=findViewById(R.id.content);

        Spinner course_names = (Spinner) findViewById(R.id.spn1);
        Spinner admission_years = (Spinner) findViewById(R.id.spn2);
        Spinner passing_years = (Spinner) findViewById(R.id.spn3);

        course_name = new String[]{"B.E", "B.Tech", "M.E.", "M.Tech", "PH.D."};
        admission_year = new String[]{"2013", "2014", "2015", "2016", "2017", "2018", "2019"};
        passing_year = new String[]{"2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024"};

        adapter = new ArrayAdapter(SignUpActivity4.this, android.R.layout.simple_list_item_1, course_name);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        course_names.setAdapter(adapter);

        adapter2 = new ArrayAdapter(SignUpActivity4.this, android.R.layout.simple_list_item_1, admission_year);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        admission_years.setAdapter(adapter2);

        adapter3 = new ArrayAdapter(SignUpActivity4.this, android.R.layout.simple_list_item_1, passing_year);

        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        passing_years.setAdapter(adapter3);

        admission_years.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                ad_year=adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        passing_years.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                pass_year=adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        course_names.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                course=adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        next_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                branchS= branch_name.getText().toString();
                if(TextUtils.isEmpty(branchS)) {
                    next_fragment.setClickable(true);
                    LayoutprogressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(SignUpActivity4.this, "Please Enter your branch", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{

                    next_fragment.setClickable(false);
                    LayoutprogressBar.setVisibility(View.VISIBLE);
                    content.setForeground(new ColorDrawable(Color.parseColor("#60000000")));
                    sendData();




               /*reference.child(currentUser.getUid()).child("Course").setValue(course);
               reference.child(currentUser.getUid()).child("AdmissionYear").setValue(ad_year);
               reference.child(currentUser.getUid()).child("PassingYear").setValue(pass_year);
               reference.child(currentUser.getUid()).child("Branch").setValue(Branch);*/
                }



            }
        });

    }

    private void sendData()
    {

        reference.child(currentUser.getUid()).child("AdmissionYear").setValue(ad_year)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        next_fragment.setClickable(true);
                        LayoutprogressBar.setVisibility(View.INVISIBLE);
                        content.setForeground(null);
                        Toast.makeText(SignUpActivity4.this, "An error occurred, with error message:  "+e.toString(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                });

        reference.child(currentUser.getUid()).child("PassingYear").setValue(pass_year)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        next_fragment.setClickable(true);
                        LayoutprogressBar.setVisibility(View.INVISIBLE);
                        content.setForeground(null);
                        Toast.makeText(SignUpActivity4.this, "An error occurred, with error message:  "+e.toString(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                });

        reference.child(currentUser.getUid()).child("Branch").setValue(branchS)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        next_fragment.setClickable(true);
                        LayoutprogressBar.setVisibility(View.INVISIBLE);
                        content.setForeground(null);
                        Toast.makeText(SignUpActivity4.this, "An error occurred, with error message:  "+e.toString(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
        reference.child(currentUser.getUid()).child("Course").setValue(course)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(SignUpActivity4.this, SignUpActivity3.class));
                        next_fragment.setClickable(true);
                        LayoutprogressBar.setVisibility(View.INVISIBLE);
                        content.setForeground(null);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        next_fragment.setClickable(true);
                        LayoutprogressBar.setVisibility(View.INVISIBLE);
                        content.setForeground(null);
                        Toast.makeText(SignUpActivity4.this, "An error occurred, with error message:  "+e.toString(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                });



    }

}
