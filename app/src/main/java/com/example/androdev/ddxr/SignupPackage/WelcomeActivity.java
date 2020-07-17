package com.example.androdev.ddxr.SignupPackage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androdev.ddxr.IntroPackage.ScreenActivity;
import com.example.androdev.ddxr.R;
import com.example.androdev.ddxr.UserFragments.MainUserActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class WelcomeActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    FirebaseDatabase db;
    DatabaseReference dbRef;
    ValueEventListener listener;
    TextView username;
    String user_idS;
    TextView wlcm;
    TextView progressTv;
    ProgressBar progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference();
        username = findViewById(R.id.user_id);
        wlcm = findViewById(R.id.wlcm);
        wlcm.setVisibility(View.GONE);
        progress=findViewById(R.id.myProgress);
        progressTv=findViewById(R.id.myTextProgress);

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(currentUser.getUid())) {
                    if (dataSnapshot.child(currentUser.getUid()).hasChild("UserID")) {
                        user_idS = dataSnapshot.child(currentUser.getUid()).child("UserID").getValue(String.class);

                        if (user_idS != null) {
                            progress.setVisibility(View.GONE);
                            progressTv.setVisibility(View.GONE);
                            wlcm.setVisibility(View.VISIBLE);
                            username.setVisibility(View.VISIBLE);
                            username.setText(user_idS);
                            remove(); //see bottom of this code
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    Intent intent = new Intent(getApplicationContext(), MainUserActivity.class);
                                    startActivity(intent);
                                    finishAffinity();
                                }
                            }, 3000);
                        } else {
                                    /*Intent intent = new Intent(ScreenActivity.this, ScreenSlidePagerActivity.class);
                                    startActivity(intent);
                                    remove();
                                    finishAffinity();*/
                            Toast.makeText(getApplicationContext(), "There was an error with the registration!\n Please Login Again to continue.", Toast.LENGTH_LONG).show();
                            firebaseAuth.signOut();
                            Intent intent = new Intent(getApplicationContext(), ScreenActivity.class);
                            startActivity(intent);
                            finishAffinity();
                            remove();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "There was an error with the registration!\n Please Login Again to continue.", Toast.LENGTH_LONG).show();
                        firebaseAuth.signOut();
                        remove();
                        Intent intent = new Intent(getApplicationContext(), ScreenActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "There was an error with the registration!\n Please Login Again to continue.", Toast.LENGTH_LONG).show();
                    firebaseAuth.signOut();
                    remove();
                    Intent intent = new Intent(getApplicationContext(), ScreenActivity.class);
                    startActivity(intent);
                    finishAffinity();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        };
        dbRef.addValueEventListener(listener);

    }


    public void remove(){
        dbRef.removeEventListener(listener);
    }


}