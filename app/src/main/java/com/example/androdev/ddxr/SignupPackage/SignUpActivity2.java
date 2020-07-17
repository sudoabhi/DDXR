package com.example.androdev.ddxr.SignupPackage;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

public class SignUpActivity2 extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference dbRef;


    EditText UserID_ET;
    EditText FullName_ET;
    Button next_viewpager;
    EditText user_skill;
    ProgressBar LayoutprogressBar;
    FrameLayout content;

    FirebaseUser currentUser;

    ArrayAdapter adapter;
    String genderS;
    String[] gender;

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    String monthS;
    String dayS;
    String yearS;
    String todays_date;
    Date todays_date_oc;
    String month;

    TextInputEditText et_dob;
    String dobS;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment2);


        db=FirebaseDatabase.getInstance();
        dbRef=db.getReference();
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        UserID_ET=(EditText)findViewById(R.id.et_user_id);
        FullName_ET=(EditText)findViewById(R.id.et_full_name);
        user_skill=(EditText) findViewById(R.id.et_user_skill);

        next_viewpager=(Button)findViewById(R.id.next_viewpager);
        LayoutprogressBar=(ProgressBar) findViewById(R.id.progressBar);
        LayoutprogressBar.setVisibility(View.INVISIBLE);
        content=findViewById(R.id.content);

        next_viewpager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String  UserID_ETs= UserID_ET.getText().toString();
                String  FullName_ETs= FullName_ET.getText().toString();
                String  UserSkill_ETs= user_skill.getText().toString();
                if(TextUtils.isEmpty(UserID_ETs)||TextUtils.isEmpty(UserSkill_ETs)||TextUtils.isEmpty(FullName_ETs)
                    ||TextUtils.isEmpty(genderS)||TextUtils.isEmpty(dobS)){

                    next_viewpager.setClickable(true);
                    LayoutprogressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(SignUpActivity2.this, "Please fill in the empty fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {

                    next_viewpager.setClickable(false);
                    LayoutprogressBar.setVisibility(View.VISIBLE);
                    content.setForeground(new ColorDrawable(Color.parseColor("#60000000")));

                    sendData();
                }




            }
        });

        Spinner genderSp = (Spinner) findViewById(R.id.gender);
        gender = new String[]{"Male", "Female", "Others"};
        adapter = new ArrayAdapter(SignUpActivity2.this, android.R.layout.simple_list_item_1, gender);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSp.setAdapter(adapter);

        genderSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                genderS=adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        et_dob=findViewById(R.id.et_dob);


        final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
        et_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view.startAnimation(buttonClick);
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int monthofYear = calendar.get(Calendar.MONTH);
                int dayofMonth = calendar.get(Calendar.DAY_OF_MONTH);
                Date start_date_oc;
                final String[] p = new String[1];
                final String[] l = new String[1];

                //  start_date_oc=calendar.getTime().get;


                // mCalendarView.setMinDate(date);
                datePickerDialog = new DatePickerDialog(SignUpActivity2.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                        switch(i1+1){
                            case 1:
                                month= "Jan";
                                break;
                            case 2:
                                month= "Feb";
                                break;
                            case 3:
                                month= "March";
                                break;
                            case 4:
                                month= "April";
                                break;
                            case 5:
                                month= "May";
                                break;
                            case 6:
                                month= "June";
                                break;
                            case 7:
                                month= "July";
                                break;
                            case 8:
                                month= "August";
                                break;
                            case 9:
                                month= "Sept";
                                break;
                            case 10:
                                month= "Oct";
                                break;
                            case 11:
                                month= "Nov";
                                break;
                            case 12:
                                month= "Dec";
                                break;

                        }
                        if(i2<10){
                            p[0] ="0"+String.valueOf(i2);

                            Log.e("date2","h2"+ p[0]);

                        }
                        else{
                            p[0]=String.valueOf(i2);
                        }
                        int m=i1+1;
                        if((m)<10){
                            l[0] ="0"+String.valueOf(m);

                            Log.e("month","h"+ l[0]);
                        }
                        else{
                            l[0]=String.valueOf(m);
                        }
                        String x=String.valueOf(i);
                        String parts=x.substring(2);
                        dobS= p[0] +"/"+ l[0] +"/"+parts;
                        Log.e("StartDate","h"+dobS);

                        et_dob.setText(dobS);

                    }
                }, year, monthofYear, dayofMonth);


                datePickerDialog.show();


            }


        });



    }


    private void sendData()
    {


        dbRef.child(currentUser.getUid()).child("UserSkill").setValue(user_skill.getText().toString())
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        next_viewpager.setClickable(true);
                        LayoutprogressBar.setVisibility(View.INVISIBLE);
                        content.setForeground(null);
                        Toast.makeText(SignUpActivity2.this, "An error occurred, with error message:  "+e.toString(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                });

        dbRef.child(currentUser.getUid()).child("UserID").setValue(""+UserID_ET.getText().toString())
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        next_viewpager.setClickable(true);
                        LayoutprogressBar.setVisibility(View.INVISIBLE);
                        content.setForeground(null);
                        Toast.makeText(SignUpActivity2.this, "An error occurred, with error message:  "+e.toString(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                });

        dbRef.child(currentUser.getUid()).child("Gender").setValue(""+genderS)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        next_viewpager.setClickable(true);
                        LayoutprogressBar.setVisibility(View.INVISIBLE);
                        content.setForeground(null);
                        Toast.makeText(SignUpActivity2.this, "An error occurred, with error message:  "+e.toString(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                });

        dbRef.child(currentUser.getUid()).child("DOB").setValue(""+dobS)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        next_viewpager.setClickable(true);
                        LayoutprogressBar.setVisibility(View.INVISIBLE);
                        content.setForeground(null);
                        Toast.makeText(SignUpActivity2.this, "An error occurred, with error message:  "+e.toString(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                });

        dbRef.child(currentUser.getUid()).child("UserName").setValue(""+FullName_ET.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        next_viewpager.setClickable(true);
                        LayoutprogressBar.setVisibility(View.INVISIBLE);
                        content.setForeground(null);
                        startActivity(new Intent(SignUpActivity2.this, SignUpActivity4.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        next_viewpager.setClickable(true);
                        LayoutprogressBar.setVisibility(View.INVISIBLE);
                        content.setForeground(null);
                        Toast.makeText(SignUpActivity2.this, "An error occurred, with error message:  "+e.toString(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                });


    }



}


