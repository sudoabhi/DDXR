package com.example.androdev.ddxr.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.androdev.ddxr.R;
import com.example.androdev.ddxr.UserFragments.MainUserActivity;
import com.example.androdev.ddxr.UserFragments.MyApp;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.events.Subscriber;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.platforminfo.UserAgentPublisher;

import java.util.Map;

/**
 * Created by Asus on 10/15/2019.
 */

public class MyFirebaseIdService extends FirebaseMessagingService {



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String click_action=remoteMessage.getNotification().getClickAction();
        if(click_action!=null){
            Intent intent=new Intent(click_action);
        }

        Log.e("jump2", "The message received is " + remoteMessage);
        showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), remoteMessage.getData());
    }


    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        DatabaseReference parentReference;
        final FirebaseUser user;

        parentReference=FirebaseDatabase.getInstance().getReference();
        user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            parentReference.child(user.getUid()).child("Token").setValue(s);
        }

    }

    private void showNotification(String title, String body, Map<String, String> data) {
        Context context = getApplicationContext();
        Intent intent;
        Log.e("jump","h"+data.toString());
        if(data.containsKey("manual")){
             intent = new Intent(context, ManualNotification.class);
        }
        else{
            intent = new Intent(context, MainUserActivity.class);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,0);

        String channelId = "Divine Care";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MyApp.CHANNEL_ONE_ID)
                .setSmallIcon(R.drawable.ic_link_black_24dp)
                //.setLargeIcon(bitmap)
                .setContentTitle(title)
                .setContentText(body)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH);
        //.setStyle(new NotificationCompat.BigTextStyle().bigText(body));
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        builder.setAutoCancel(true);
        //builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Divine Care",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            builder.setSmallIcon(R.drawable.ic_link_black_24dp);
        } else {
            builder.setSmallIcon(R.drawable.ic_library_books_black_24dp);
        }
        notificationManager.notify(0 /* ID of notification */, builder.build());
    }
}
