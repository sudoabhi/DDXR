package com.example.androdev.ddxr.UserFragments;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asus on 9/21/2019.
 */

public class  MyApp extends Application {

    public static final String CHANNEL_ONE_ID="channel_one_id";
    public static final String CHANNEL_TWO_ID="channel_two_id";
    DatabaseReference parentReference;
    DatabaseReference adminReference;
    FirebaseUser user;




    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseDatabase.getInstance("https://ddrx-172d3-2a5f7.firebaseio.com/").setPersistenceEnabled(true);
        FirebaseDatabase.getInstance("https://ddrx-admin-172d3.firebaseio.com/").setPersistenceEnabled(true);
        FirebaseDatabase.getInstance("https://ddrx-college-list.firebaseio.com/").setPersistenceEnabled(true);
        FirebaseDatabase.getInstance("https://ddrx-events-172d3.firebaseio.com/").setPersistenceEnabled(true);
        FirebaseDatabase.getInstance("https://ddrx-feedback-172d3.firebaseio.com/").setPersistenceEnabled(true);
        FirebaseDatabase.getInstance("https://eventdiscussions.firebaseio.com/").setPersistenceEnabled(true);
        FirebaseDatabase.getInstance("https://manualnotifications.firebaseio.com/").setPersistenceEnabled(true);
        FirebaseDatabase.getInstance("https://messagelist-6b6d2.firebaseio.com/").setPersistenceEnabled(true);
        FirebaseDatabase.getInstance("https://messages23c62.firebaseio.com/").setPersistenceEnabled(true);
        FirebaseDatabase.getInstance("https://organizersblog.firebaseio.com/").setPersistenceEnabled(true);

       parentReference= FirebaseDatabase.getInstance().getReference();

        user= FirebaseAuth.getInstance().getCurrentUser();


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {

                        if(task.isSuccessful()){
                            String token=task.getResult().getToken();
                            if(user!=null) {
                                parentReference.child(user.getUid()).child("Token").setValue(token);
                            }
                            Log.d("MyApp", "onComplete: Token: "+token);

                        }else{
                            Log.d("MyApp", "onComplete: Unsuccessful token transfer");
                        }

                    }
                });

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel Channel_One=new NotificationChannel(CHANNEL_ONE_ID,"Channel One", NotificationManager.IMPORTANCE_HIGH);
            Channel_One.setDescription("This is for video notifications");

            NotificationChannel Channel_Two=new NotificationChannel(CHANNEL_TWO_ID,"Channel Two", NotificationManager.IMPORTANCE_HIGH);
            Channel_Two.setDescription("This is for audio notifications");

            NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            List<NotificationChannel> channels=new ArrayList<>();
            channels.add(Channel_One);
            channels.add(Channel_Two);
            manager.createNotificationChannel(channels.get(0));
        }
        }




    }

