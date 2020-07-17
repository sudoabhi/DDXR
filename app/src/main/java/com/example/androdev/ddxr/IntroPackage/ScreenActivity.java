package com.example.androdev.ddxr.IntroPackage;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androdev.ddxr.CommonPackage.PrivacyTermsActivity;
import com.example.androdev.ddxr.R;
import com.example.androdev.ddxr.SignupPackage.SignUpActivity1;
import com.example.androdev.ddxr.UserFragments.MainUserActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ScreenActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private static final int RC_SIGN_IN = 9001;
    Button googleButton;
    Button emailbutton;
    FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    GoogleSignInClient mGoogleSignInClient;
    Boolean emailVerified;
    ProgressBar progressBar;
    FirebaseDatabase db;
    DatabaseReference dbRef;
    FirebaseDatabase adminDatabase;
    DatabaseReference adminReference;
    ValueEventListener listener;
    String user_type=null;
    String reg;
    RelativeLayout screen;
    TextView privacy;
    TextView terms;

    RelativeLayout inner1;
    RelativeLayout inner2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_screen);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;
        int deviceHeight = displayMetrics.heightPixels;

        inner1=findViewById(R.id.inner1);
        inner2=findViewById(R.id.inner2);

        ViewGroup.LayoutParams params=inner1.getLayoutParams();
        params.height=(deviceHeight/2);
        inner1.setLayoutParams(params);
        ViewGroup.LayoutParams params2=inner2.getLayoutParams();
        params2.height=(deviceHeight/2);
        inner2.setLayoutParams(params2);


        googleButton = (Button) findViewById(R.id.google_button);
        emailbutton = (Button) findViewById(R.id.email_button);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser=firebaseAuth.getCurrentUser();
        progressBar=findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);
        db=FirebaseDatabase.getInstance();
        dbRef=db.getReference();
        screen=findViewById(R.id.splash_screen);
        terms=findViewById(R.id.terms);
        privacy=findViewById(R.id.privacy);

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ScreenActivity.this, PrivacyTermsActivity.class);
                i.putExtra("Type","Terms");
                startActivity(i);
            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ScreenActivity.this, PrivacyTermsActivity.class);
                i.putExtra("Type","Privacy");
                startActivity(i);
            }
        });

        adminDatabase=FirebaseDatabase.getInstance("https://ddrx-admin-172d3.firebaseio.com/");
        adminReference=adminDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user==null){
                    currentUser=null;
                }

            }
        };



        if(currentUser!=null){

            emailVerified = currentUser.isEmailVerified();
            if(!emailVerified){
                //Toast.makeText(getApplicationContext(),"Your Email is not verified. " +"Please verify and Login again!",Toast.LENGTH_LONG).show();
                firebaseAuth.signOut();
                screen.setVisibility(View.GONE);
            }
            else{
                listener= new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(currentUser.getUid())) {

                            if (dataSnapshot.child(currentUser.getUid()).hasChild("Registered")) {

                                if (dataSnapshot.child(currentUser.getUid()).hasChild("UserType")) {

                                    if ((dataSnapshot.child(currentUser.getUid()).child("UserType").getValue(String.class)).equals("Student")) {


                                        reg = dataSnapshot.child(currentUser.getUid()).child("Registered").getValue(String.class);

                                        if (reg.equals("True")) {
                                            Intent intent = new Intent(ScreenActivity.this, MainUserActivity.class);
                                            startActivity(intent);
                                            remove(); //see bottom of this code
                                            finishAffinity();
                                        } else {
                                            dataSnapshot.child(currentUser.getUid()).getRef().removeValue();
                                            Toast.makeText(getApplicationContext(), "Registration was not completed!\n Please Login Again to continue.", Toast.LENGTH_LONG).show();
                                            firebaseAuth.signOut();
                                            screen.setVisibility(View.GONE);
                                            remove();
                                        }

                                    } else {

                                        Toast.makeText(getApplicationContext(), "Email ID mismatch.",
                                                Toast.LENGTH_LONG).show();
                                        firebaseAuth.signOut();
                                        screen.setVisibility(View.GONE);
                                        remove();

                                    }
                                }
                                else{
                                    dataSnapshot.child(currentUser.getUid()).getRef().removeValue();
                                    screen.setVisibility(View.GONE);
                                    firebaseAuth.signOut();
                                    remove();
                                }
                            } else {
                                dataSnapshot.child(currentUser.getUid()).getRef().removeValue();
                                screen.setVisibility(View.GONE);
                                firebaseAuth.signOut();
                                remove();
                            }
                        }
                        else{
                            screen.setVisibility(View.GONE);
                            firebaseAuth.signOut();
                            remove();
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),""+databaseError.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                };
                dbRef.addValueEventListener(listener);

                /*
                Intent intent = new Intent(ScreenActivity.this, MainUserActivity.class);
                startActivity(intent);
                finish();*/
            }
        }
        else{
            firebaseAuth.signOut();
            screen.setVisibility(View.GONE);
        }





        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("568432393374-2ucqridljg77s0k03q5obed1a181s4gb.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        /*mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(ScreenActivity.this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();*/


        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                signIn();
            }
        });

        emailbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ScreenActivity.this);
                startActivity(new Intent(getApplicationContext(), MailActivity.class),options.toBundle());
            }
        });
    }

        private void signIn() {
            /*Intent signIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);*/
            mGoogleSignInClient.signOut();
            Intent signIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signIntent,RC_SIGN_IN);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            /*if(requestCode==RC_SIGN_IN){
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if(result.isSuccess()){
                    GoogleSignInAccount account = result.getSignInAccount();
                    authWithGoogle(account);
                }
                else{
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
            else{
                progressBar.setVisibility(View.INVISIBLE);
            }*/

            if (requestCode == RC_SIGN_IN) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    authWithGoogle(account);

                } catch (ApiException e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"Please select a valid Google ID to continue.",Toast.LENGTH_SHORT).show();

                    //Toast.makeText(getApplicationContext(),"Error:  "+e.toString(),Toast.LENGTH_SHORT).show();

                }
            }else{
                progressBar.setVisibility(View.INVISIBLE);
            }


        }


        private void authWithGoogle(GoogleSignInAccount account) {
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
                        if(isNewUser) {
                            currentUser=firebaseAuth.getCurrentUser();
                            dbRef.child(currentUser.getUid()).child("Registered").setValue("False");

                            startActivity(new Intent(getApplicationContext(), SignUpActivity1.class));
                            finishAffinity();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        else {
                            currentUser=firebaseAuth.getCurrentUser();

                            listener= new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.hasChild(currentUser.getUid())&&dataSnapshot.child(currentUser.getUid()).hasChild("Registered")) {

                                        if (dataSnapshot.child(currentUser.getUid()).hasChild("UserType")) {

                                            user_type = dataSnapshot.child(currentUser.getUid()).child("UserType").getValue(String.class);
                                            if (user_type.equals("Student")) {


                                                reg = dataSnapshot.child(currentUser.getUid()).child("Registered").getValue(String.class);

                                                if (reg.equals("True")) {
                                                    Intent intent = new Intent(ScreenActivity.this, MainUserActivity.class);
                                                    startActivity(intent);
                                                    remove();
                                                    finishAffinity();
                                                } else {
                                                    Intent intent = new Intent(ScreenActivity.this, SignUpActivity1.class);
                                                    startActivity(intent);
                                                    finishAffinity();
                                                    remove();
                                                }


                                            } else {
                                                Toast.makeText(getApplicationContext(), "This Google ID is already registered by another user on ddxr admin app.",
                                                        Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.INVISIBLE);
                                                remove();
                                                return;

                                            }

                                        } else {

                                            dbRef.child(currentUser.getUid()).child("Registered").setValue("False");
                                            Intent intent = new Intent(ScreenActivity.this, SignUpActivity1.class);
                                            startActivity(intent);
                                            progressBar.setVisibility(View.INVISIBLE);
                                            remove();
                                            finishAffinity();

                                        }
                                    }
                                    else {
                                        adminReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.hasChild(currentUser.getUid())&&dataSnapshot.child(currentUser.getUid()).hasChild("Registered")) {
                                                    String reg = dataSnapshot.child(currentUser.getUid()).child("Registered").getValue(String.class);
                                                    if (reg.equals("True")) {
                                                        Toast.makeText(getApplicationContext(), "This Google ID is already registered by another user on ddxr admin app.",
                                                                Toast.LENGTH_SHORT).show();
                                                        firebaseAuth.signOut();
                                                        progressBar.setVisibility(View.INVISIBLE);
                                                        remove();
                                                        return;
                                                    } else {
                                                        dbRef.child(currentUser.getUid()).child("Registered").setValue("False");
                                                        Intent intent = new Intent(ScreenActivity.this, SignUpActivity1.class);
                                                        startActivity(intent);
                                                        progressBar.setVisibility(View.INVISIBLE);
                                                        remove();
                                                        finishAffinity();
                                                    }
                                                }else {
                                                    dbRef.child(currentUser.getUid()).child("Registered").setValue("False");
                                                    Intent intent = new Intent(ScreenActivity.this, SignUpActivity1.class);
                                                    startActivity(intent);
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    remove();
                                                    finishAffinity();
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
                                    Toast.makeText(getApplicationContext(),""+databaseError.getMessage(),Toast.LENGTH_SHORT).show();

                                }
                            };
                            dbRef.addValueEventListener(listener);
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Auth Error",Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            Toast.makeText(getApplicationContext(),"Connection Failed"+ connectionResult.toString(),Toast.LENGTH_SHORT).show();
            if (!connectionResult.hasResolution()) {
                // show the localized error dialog.
                progressBar.setVisibility(View.INVISIBLE);

                //GoogleApiAvailability.getInstance().getErrorDialog(this, connectionResult.getErrorCode(), 0).show();
                return;
            }

        }

    public void remove(){
        dbRef.removeEventListener(listener);
    }


}
