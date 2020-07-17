package com.example.androdev.ddxr.PaytmPAYMENT;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androdev.ddxr.R;
import com.example.androdev.ddxr.UserFragments.FirebaseCallbackXX;
import com.example.androdev.ddxr.UserFragments.FirebaseCallbackXX2;
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
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.messaging.FirebaseMessaging;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;



public class InviteTeam extends AppCompatActivity {


    String key;
   String linksender;
    DatabaseReference parentReference;
    FirebaseUser user;
    Button  register;
    ProgressBar progressBarX;

    TextView EventNameTV;
    TextView TicketNameTV;
    TextView EventURLTV;
    TextView InvitedByTV;
    TextView FeetopayTV;
    long alreadyAdded;



    String EventBannerURL;
    String EventKeyX;
    String EventName;

    String TicketKey;
    String TicketName;
    String EventURL;
    String UserName;

    String Fee;
    String FeeOptX;
    String todays_date;
    String todays_time;

    String PriceCheck;
    DatabaseReference eventReference;
    ProgressBar progressBar2;
    LinearLayout parent_view;

    String OrderValid="true";
    String TeamName;
    Toolbar  toolbar;

    String ParticipantName;
    String ParticipantImageURL;
    String ParticipantRollNo;
    String ParticipantCollege;
    String ParticipantVerified;

    String eventkeyM;
    String ticketkeyM;
    String teamnameM;



    DatabaseReference adminReference;
    String ClubUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_team);

        parentReference= FirebaseDatabase.getInstance().getReference();
        user= FirebaseAuth.getInstance().getCurrentUser();
        register=(Button)findViewById(R.id.register_button);
        progressBarX=(ProgressBar) findViewById(R.id.progress_barX);
        progressBar2=(ProgressBar) findViewById(R.id.progress_bar2);
        parent_view=(LinearLayout) findViewById(R.id.parent_view);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        EventNameTV=(TextView)findViewById(R.id.EventName);
        TicketNameTV=(TextView)findViewById(R.id.TicketName);
        EventURLTV=(TextView)findViewById(R.id.EventURL);
        InvitedByTV=(TextView)findViewById(R.id.InvitedBy);
        eventReference=FirebaseDatabase.getInstance("https://ddrx-events-172d3.firebaseio.com/").getReference();
        FeetopayTV=(TextView)findViewById(R.id.FeetobePaid);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Team Invite");

        adminReference = FirebaseDatabase.getInstance("https://ddrx-admin-172d3.firebaseio.com/").getReference();


        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                        }
                        key=deepLink.getQueryParameter("Key");
                       linksender=deepLink.getQueryParameter("Sender");
                        eventkeyM=deepLink.getQueryParameter("EventKey");
                        ticketkeyM=deepLink.getQueryParameter("TicketKey");
                        teamnameM=deepLink.getQueryParameter("TeamName");

                        Log.e("kkey",""+key);
                        Log.e("ssender",""+linksender);
                        Log.e("event1",""+eventkeyM);
                        Log.e("event2",""+ticketkeyM);
                        Log.e("event3",""+teamnameM);
                        Log.e("kkey2",""+user.getUid());


                        getTicketDetails(new FirebaseCallbackXX2() {
                            @Override
                            public void onCallBack1(String string) {
                                EventBannerURL=string;
                            }

                            @Override
                            public void onCallBack2(String string) {
                                 EventKeyX=string;
                            }

                            @Override
                            public void onCallBack3(String string) {
                                EventName=string;
                                EventNameTV.setText(": "+EventName);
                            }

                            @Override
                            public void onCallBack4(String string) {
                               TicketKey=string;
                            }

                            @Override
                            public void onCallBack5(String string) {
                                 TicketName=string;
                                TicketNameTV.setText(": "+TicketName);
                            }

                            @Override
                            public void onCallBack11(String string) {
                                TeamName=string;
                            }

                            @Override
                            public void onCallBack7(String string) {
                                   EventURL=string;
                                EventURLTV.setText(": "+EventURL);
                            }

                            @Override
                            public void onCallBack8(String string) {
                                    UserName=string;
                                InvitedByTV.setText(": "+UserName);
                            }

                            @Override
                            public void onCallBack9(String string) {
                                    FeeOptX=string;
                            }

                            @Override
                            public void onCallBack10(String string) {

                               PriceCheck=string;
                               Log.e("PriceCheck1","h"+PriceCheck);
                                if(PriceCheck.equals("Team")){

                                    FeetopayTV.setText(": Paid by the team leader");
                                }
                                else{
                                    Log.e("FeeOpt",""+FeeOptX);
                                    if(FeeOptX==null||Integer.valueOf(FeeOptX)==0){
                                        FeetopayTV.setText(": Free");
                                    }
                                    else{
                                        FeetopayTV.setText(": ₹ "+(Integer.parseInt(FeeOptX)));
                                    }
                                }


                            }

                        });


                        Log.e("PriceCheck2","h"+PriceCheck);








                     //   getEventInfo(linksender,key);


                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Heyya", "getDynamicLink:onFailure", e);
                    }
                });


        eventReference.child(eventkeyM).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ClubUID=dataSnapshot.child("UploadedBy").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBarX.setVisibility(View.VISIBLE);
                Log.e("event3",""+eventkeyM);
                Log.e("event4",""+ticketkeyM);
                 eventReference.child(eventkeyM).child("Tickets").child(ticketkeyM).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int maxValue=Integer.parseInt(dataSnapshot.child("TeamMembers").getValue(String.class));
                         alreadyAdded=dataSnapshot.child("BookedTickets").child(key).getChildrenCount();
                       if( dataSnapshot.child("BookedTickets").child(key).hasChild("TeamName")){
                           alreadyAdded--;
                           if(dataSnapshot.child("BookedTickets").child(key).hasChild("Verified")){
                               alreadyAdded--;
                           }

                       }
                        final int[] count = {0};
                        if(maxValue>alreadyAdded){

                            //proceed

                            dataSnapshot.child("BookedTickets").child(key).getRef().addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    if(!dataSnapshot.getKey().equals(user.getUid())){

                                        count[0]++;
                                        if(count[0]==alreadyAdded){



                                            if(PriceCheck.equals("Team")||Integer.parseInt(FeeOptX)==0){
                                                FeeOptX="0";

                                                progressBarX.setVisibility(View.VISIBLE);


                                                DatabaseReference DReference=parentReference.child(user.getUid()).child("BookedTickets");
                                               // final String keyD=DReference.getKey();

                                                Map<String,Object> newStudentOrder=new HashMap<>();
                                                newStudentOrder.put("EventBannerURL",EventBannerURL);
                                                newStudentOrder.put("EventKey",EventKeyX);
                                                newStudentOrder.put("EventName",EventName);
                                                newStudentOrder.put("TicketKey",TicketKey);
                                                newStudentOrder.put("TeamName", TeamName);
                                                newStudentOrder.put("TicketName",TicketName);
                                                newStudentOrder.put("Key",key);
                                                newStudentOrder.put("OrderID","-" );
                                                newStudentOrder.put("FeePaid","0");
                                                newStudentOrder.put("TEAM_CHECK","Team");
                                                newStudentOrder.put("PRICE_CHECK",PriceCheck);
                                                newStudentOrder.put("EventURL",EventURL);
                                                newStudentOrder.put("TeamName",TeamName);





                                                DReference.child(key).updateChildren(newStudentOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){




                                                            DatabaseReference newparentTicket=parentReference.child(user.getUid()).child("BookedTickets").child(key).child("TeamParticipants").push();
                                                            final  String KeyQ=newparentTicket.getKey();



                                                            parentReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    String UserNameS=  dataSnapshot.child(user.getUid()).child("UserName").getValue(String.class);
                                                                    String UserImageS= dataSnapshot.child(user.getUid()).child("ImageUrl").getValue(String.class);
                                                                    String RollNumS= dataSnapshot.child(user.getUid()).child("RollNum").getValue(String.class);
                                                                    String ClubLocationS=dataSnapshot.child(user.getUid()).child("ClubLocation").getValue(String.class);

                                                                    final HashMap<String,Object> studentOrder2=new HashMap<>();

                                                                    studentOrder2.put("OrderID","-" );
                                                                    studentOrder2.put("FeePaid","0");
                                                                    studentOrder2.put("UserUID",user.getUid());
                                                                    studentOrder2.put("UserName",UserNameS);
                                                                    studentOrder2.put("UserImage",UserImageS);
                                                                    studentOrder2.put("RollNo",RollNumS);
                                                                    studentOrder2.put("Verified","false");
                                                                    studentOrder2.put("ClubLocation",ClubLocationS);
                                                                    studentOrder2.put("TeamName",teamnameM);

                                                                    eventReference.child(eventkeyM).child("Tickets").child(TicketKey).child("BookedTickets").child(key).child(user.getUid()).updateChildren(studentOrder2);


                                                                    DatabaseReference aa=adminReference.child(ClubUID).child("Attendees").child(user.getUid()).push();
                                                                    String keyx=aa.getKey();
                                                                    final HashMap<String,Object> adminStore=new HashMap<>();
                                                                    adminStore.put("UserUID",user.getUid());
                                                                    adminStore.put("Key",keyx);
                                                                    adminStore.put("EventName",EventName);
                                                                    adminStore.put("DateBooked",todays_date);
                                                                    adminStore.put("EventKey",EventKeyX);
                                                                    aa.updateChildren(adminStore);

                                                                    parentReference.child(user.getUid()).child("BookedTickets").child(key).child("TeamParticipants").child(KeyQ).child("UserUID").setValue(user.getUid());

                                                                    parentReference.child(user.getUid()).child("BookedTickets").child(key).child("TeamParticipants").child(KeyQ).child("UserName").setValue(UserNameS).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if(task.isSuccessful()){


                                                                                progressBarX.setVisibility(View.GONE);
                                                                                Toast.makeText(InviteTeam.this,"Registration Successful",Toast.LENGTH_LONG).show();
                                                                                Intent intent=new Intent(InviteTeam.this, TicketActivity.class);
                                                                                intent.putExtra("EventKey",EventKeyX);
                                                                                intent.putExtra("TicketKey",TicketKey);
                                                                                intent.putExtra("UserUID",user.getUid());
                                                                                intent.putExtra("EventURL", EventURL);
                                                                                intent.putExtra("Key",key);
                                                                                startActivity(intent);

                                                                            }
                                                                        }
                                                                    });

                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });









                                                        }
                                                        else{
                                                            Toast.makeText(InviteTeam.this,"Error occured. Send screenshot.",Toast.LENGTH_LONG).show();
                                                        }
                                                        progressBar2.setVisibility(View.INVISIBLE);
                                                        parent_view.setVisibility(View.VISIBLE);
                                                        progressBarX.setVisibility(View.GONE);
                                                    }
                                                });


                                            }
                                            else{
                                                parent_view.setVisibility(View.INVISIBLE);
                                                progressBar2.setVisibility(View.VISIBLE);

                                                processPaytm(user.getUid(),EventKeyX,TicketKey,FeeOptX,TicketName,EventName,"Team",PriceCheck);
                                            }




                                        }
                                        else{
                                       //     Toast.makeText(InviteTeam.this,"User already registered",Toast.LENGTH_LONG).show();
                                            progressBarX.setVisibility(View.GONE);
                                        }


                                    }

                                    else{

                                        Toast.makeText(InviteTeam.this,"User already registered",Toast.LENGTH_LONG).show();
                                        progressBarX.setVisibility(View.GONE);
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
                            });


                        }
                        else{
                            Toast.makeText(InviteTeam.this,"Team already full",Toast.LENGTH_LONG).show();
                            progressBarX.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

              //  eventReference.child(eventkeyM).child("Tickets").child(ticketkeyM).child("BookedTickets").child(key).updateChildren(X);


         /*       parentReference.child(linksender).child("BookedTickets").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                         int maxValue=Integer.parseInt(dataSnapshot.child("MaxParticipants").getValue(String.class));
                        final long alreadyAdded=dataSnapshot.child("TeamParticipants").getChildrenCount();
                        final int[] count = {0};
                        if(maxValue>alreadyAdded){

                            //proceed

                            dataSnapshot.child("TeamParticipants").getRef().addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    if(!dataSnapshot.child("UserUID").getValue(String.class).equals(user.getUid())){

                                        count[0]++;
                                        if(count[0]==alreadyAdded){



                                            if(PriceCheck.equals("Team")||Integer.parseInt(FeeOptX)==0){
                                                       FeeOptX="0";

                                                       progressBarX.setVisibility(View.VISIBLE);


                                                DatabaseReference DReference=parentReference.child(user.getUid()).child("BookedTickets").push();
                                               final String keyD=DReference.getKey();

                                                Map<String,Object> newStudentOrder=new HashMap<>();
                                                newStudentOrder.put("EventBannerURL",EventBannerURL);
                                                newStudentOrder.put("EventKey",EventKeyX);
                                                newStudentOrder.put("EventName",EventName);
                                                newStudentOrder.put("TicketKey",TicketKey);
                                                newStudentOrder.put("TeamName", TeamName);
                                                newStudentOrder.put("TicketName",TicketName);
                                                newStudentOrder.put("Key",keyD);
                                                newStudentOrder.put("OrderID","-" );
                                                newStudentOrder.put("FeePaid","0");
                                                newStudentOrder.put("TEAM_CHECK","Team");
                                                newStudentOrder.put("PRICE_CHECK",PriceCheck);
                                                newStudentOrder.put("EventURL",EventURL);
                                                newStudentOrder.put("TeamName",TeamName);





                                                DReference.updateChildren(newStudentOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){



                                                                        DatabaseReference newparentTicket=parentReference.child(user.getUid()).child("BookedTickets").child(keyD).child("TeamParticipants").push();
                                                                        final  String KeyQ=newparentTicket.getKey();


                                                                        parentReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                String UserNameS=  dataSnapshot.child(user.getUid()).child("UserName").getValue(String.class);
                                                                                parentReference.child(user.getUid()).child("BookedTickets").child(keyD).child("TeamParticipants").child(KeyQ).child("UserUID").setValue(user.getUid());

                                                                                parentReference.child(user.getUid()).child("BookedTickets").child(keyD).child("TeamParticipants").child(KeyQ).child("UserName").setValue(UserNameS).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if(task.isSuccessful()){
                                                                                            progressBarX.setVisibility(View.GONE);
                                                                                            Toast.makeText(InviteTeam.this,"Registration Successful",Toast.LENGTH_LONG).show();
                                                                                            Intent intent=new Intent(InviteTeam.this, TicketActivity.class);
                                                                                            intent.putExtra("EventKey",EventKeyX);
                                                                                            intent.putExtra("TicketKey",TicketKey);
                                                                                            intent.putExtra("UserUID",user.getUid());
                                                                                            intent.putExtra("EventURL", EventURL);
                                                                                            intent.putExtra("Key",keyD);
                                                                                            startActivity(intent);

                                                                                        }
                                                                                    }
                                                                                });

                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                            }
                                                                        });









                                                        }
                                                        else{
                                                            Toast.makeText(InviteTeam.this,"Error occured. Send screenshot.",Toast.LENGTH_LONG).show();
                                                        }
                                                        progressBar2.setVisibility(View.INVISIBLE);
                                                        parent_view.setVisibility(View.VISIBLE);
                                                        progressBarX.setVisibility(View.GONE);
                                                    }
                                                });


                                            }
                                            else{
                                                parent_view.setVisibility(View.INVISIBLE);
                                                progressBar2.setVisibility(View.VISIBLE);

                                                processPaytm(user.getUid(),EventKeyX,TicketKey,FeeOptX,TicketName,EventName,"Team",PriceCheck);
                                            }




                                        }
                                        else{
                                            Toast.makeText(InviteTeam.this,"User already registered",Toast.LENGTH_LONG).show();
                                            progressBarX.setVisibility(View.GONE);
                                        }


                                    }

                                    else{

                                        Toast.makeText(InviteTeam.this,"User already registered",Toast.LENGTH_LONG).show();
                                        progressBarX.setVisibility(View.GONE);
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
                            });


                        }
                        else{
                            Toast.makeText(InviteTeam.this,"Team already full",Toast.LENGTH_LONG).show();
                            progressBarX.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/



            }
        });

    }



   /* public void getEventInfo(String linksender,final String key){


        parentReference.child(linksender).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String EventBannerURL= dataSnapshot.child("BookedTickets").child(key).child("EventBannerURL").getValue(String.class);
                String EventKey= dataSnapshot.child("BookedTickets").child(key).child("EventBannerURL").getValue(String.class);
                String EventName= dataSnapshot.child("BookedTickets").child(key).child("EventBannerURL").getValue(String.class);
               // String Key= dataSnapshot.child("EventBannerURL").getValue(String.class);
               String TicketKey= dataSnapshot.child("BookedTickets").child(key).child("EventBannerURL").getValue(String.class);
                String TicketName= dataSnapshot.child("BookedTickets").child(key).child("EventBannerURL").getValue(String.class);
                String EventURL=dataSnapshot.child("BookedTickets").child(key).child("EventURL").getValue(String.class);
                String UserName=dataSnapshot.child("UserName").getValue(String.class);
                String FeeOpt=dataSnapshot.child("BookedTickets").child(key).child("FeePaid").getValue(String.class);
                String Fee;
                if(dataSnapshot.child("BookedTickets").child(key).child("PRICE_CHECK").getValue(String.class).equals("Team")){
                    Fee="Free";
                }
                else{
                    Fee="₹ "+FeeOpt;
                }



                EventNameTV.setText(": "+EventName);
                TicketNameTV.setText(": "+TicketName);
                EventURLTV.setText(": "+EventURL);
                InvitedByTV.setText(": "+UserName);
                FeetopayTV.setText(": "+Fee);



                DatabaseReference DReference=parentReference.child(user.getUid()).child("BookedTickets").push();
                String keyD=DReference.getKey();

                Map<String,Object> newStudentOrder=new HashMap<>();
                newStudentOrder.put("EventBannerURL",EventBannerURL);
                newStudentOrder.put("EventKey",EventKey);
                newStudentOrder.put("EventName",EventName);
                newStudentOrder.put("TicketKey",TicketKey);
                newStudentOrder.put("TicketName",TicketName);
                newStudentOrder.put("Key",keyD);




                parentReference.child(user.getUid()).child("BookedTickets").child()

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }*/

    private void processPaytm(final String userUID, final String EventKeyX, final String TicketKey, final String TicketPrice, final String TicketName, final String EventName, final String TeamCheck, final String PriceCheck) {

        if (ContextCompat.checkSelfPermission(InviteTeam.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(InviteTeam.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }

        String custID = userUID;
        final int price=Integer.parseInt(TicketPrice);
        final String eventfirst=EventKeyX.substring(0,4);
        final String orderID = eventfirst+""+ ThreadLocalRandom.current().nextInt(100000000, 999999999);
        String callBackurl = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID" + orderID;

        final Paytm paytm = new Paytm(
                "copYkQ20143736243713",
                "WAP",
                TicketPrice,
                "DEFAULT",
                callBackurl,
                "Retail",
                orderID,
                custID
        );

        if(Integer.parseInt(TicketPrice)<1){
            OrderValid="false";
        }

        WebServiceCaller.getClient().getChecksum(paytm.getmId(), paytm.getOrderId(), paytm.getCustId()
                , paytm.getChannelId(), paytm.getTxnAmount(), paytm.getWebsite(), paytm.getCallBackUrl(), paytm.getIndustryTypeId()
        ).enqueue(new retrofit2.Callback<Checksum>() {
            @Override
            public void onResponse(retrofit2.Call<Checksum> call, retrofit2.Response<Checksum> response) {
                if (response.isSuccessful()) {
                    processToPay(response.body().getChecksumHash(),paytm,userUID,EventKeyX,TicketKey,orderID,TicketPrice,TicketName,EventName,TeamCheck,PriceCheck);

                    Log.e("Work","Yes");
                    Log.e("WorkX",""+response.message());
                }
                else{
                    progressBar2.setVisibility(View.INVISIBLE);
                    parent_view.setVisibility(View.VISIBLE);
                    progressBarX.setVisibility(View.GONE);
                    Toast.makeText(InviteTeam.this,response.message(),Toast.LENGTH_LONG).show();
                    Log.e("Work","No");
                    Log.e("WorkX","h"+response.message());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Checksum> call, Throwable t) {
                Log.e("Work","Nota");
                progressBar2.setVisibility(View.INVISIBLE);
                progressBarX.setVisibility(View.GONE);
                Toast.makeText(InviteTeam.this,"Response failure",Toast.LENGTH_LONG).show();
                parent_view.setVisibility(View.VISIBLE);
            }
        });


    }

    private void processToPay(String checksumHash, Paytm paytm, final String UserUID, final String EventKey, final String TicketKey, final String orderID,final String price,final String TicketName,final String EventName,final String TeamCheck,final String PriceCheck) {
       /* progressBar2.setVisibility(View.INVISIBLE);
        parent_view.setVisibility(View.VISIBLE);*/
        PaytmPGService Service = PaytmPGService.getProductionService();

        HashMap<String, String> paramMap = new HashMap<String,String>();
        paramMap.put( "MID" , paytm.getmId());
// Key in your staging and production MID available in your dashboard
        paramMap.put( "ORDER_ID" , paytm.getOrderId());
        paramMap.put( "CUST_ID" , paytm.getCustId());
        paramMap.put( "CHANNEL_ID" , paytm.getChannelId());
        paramMap.put( "TXN_AMOUNT" , paytm.getTxnAmount());
        paramMap.put( "WEBSITE" , paytm.getWebsite());
// This is the staging value. Production value is available in your dashboard
        paramMap.put( "INDUSTRY_TYPE_ID" , paytm.getIndustryTypeId());
// This is the staging value. Production value is available in your dashboard
        paramMap.put( "CALLBACK_URL", paytm.getCallBackUrl());
        paramMap.put( "CHECKSUMHASH" , checksumHash);
        PaytmOrder Order = new PaytmOrder(paramMap);

            Service.initialize(Order, null);



        Service.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback() {
            /*Call Backs*/
            public void someUIErrorOccurred(String inErrorMessage) {
                Toast.makeText(getApplicationContext(), "UI Error " + inErrorMessage , Toast.LENGTH_LONG).show();

            }
            public void onTransactionResponse(Bundle inResponse) {


                String RespCodeS=inResponse.getString("RESPCODE");

                int RespCodeI=Integer.parseInt(RespCodeS);


               /* Intent intent=new Intent(EventDetail.this, TicketActivity.class);
                intent.putExtra("RESPCODE",RespCodeI);
                startActivity(intent);*/

                if((RespCodeI==1)){


                    getTodaysTime();

                    FirebaseMessaging.getInstance().subscribeToTopic(TicketKey);


                   /* final DatabaseReference parentTicketReference= parentReference.child(user.getUid()).child("BookedTickets").push();
                    final  String KeyZ=parentTicketReference.getKey();*/



                    DatabaseReference DReference=parentReference.child(user.getUid()).child("BookedTickets").push();
                    String keyD=DReference.getKey();

                    Map<String,Object> OrganizerAttendeeCopy=new HashMap<>();

                    OrganizerAttendeeCopy.put("OrderID",orderID );
                    //OrganizerAttendeeCopy.put("")


                    Map<String,Object> newStudentOrder2=new HashMap<>();
                    newStudentOrder2.put("EventBannerURL",EventBannerURL);
                    newStudentOrder2.put("EventKey",EventKey);
                    newStudentOrder2.put("EventName",EventName);
                    newStudentOrder2.put("TicketKey",TicketKey);
                    newStudentOrder2.put("TeamName",teamnameM);
                    newStudentOrder2.put("TicketName",TicketName);
                    newStudentOrder2.put("Key",keyD);
                    newStudentOrder2.put("Date",todays_date);
                    newStudentOrder2.put("Time",todays_time);

                    if(orderID==null){
                        newStudentOrder2.put("OrderID","-" );
                    }
                    else{
                        newStudentOrder2.put("OrderID",orderID );
                    }




                    if(price==null){
                        newStudentOrder2.put("FeePaid","0");

                    }
                    else{
                        newStudentOrder2.put("FeePaid",price);

                    }


                    newStudentOrder2.put("TEAM_CHECK",TeamCheck);
                    newStudentOrder2.put("PRICE_CHECK",PriceCheck);

                    newStudentOrder2.put("EventURL",EventURL);





                    DReference.updateChildren(newStudentOrder2).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                eventReference.child(EventKey).child("EventURL").setValue(EventURL).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){

                                            DatabaseReference newparentTicket=parentReference.child(user.getUid()).child("BookedTickets").child(key).child("TeamParticipants").push();
                                            final  String KeyQ=newparentTicket.getKey();


                                            parentReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    parentReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            String UserNameS=  dataSnapshot.child(user.getUid()).child("UserName").getValue(String.class);
                                                            String UserImageS= dataSnapshot.child(user.getUid()).child("ImageUrl").getValue(String.class);
                                                            String RollNumS= dataSnapshot.child(user.getUid()).child("RollNum").getValue(String.class);
                                                            String ClubLocationS=dataSnapshot.child(user.getUid()).child("ClubLocation").getValue(String.class);

                                                            final HashMap<String,Object> studentOrder3=new HashMap<>();

                                                            studentOrder3.put("OrderID",orderID );
                                                            studentOrder3.put("FeePaid",price);
                                                            studentOrder3.put("UserUID",user.getUid());
                                                            studentOrder3.put("UserName",UserNameS);
                                                            studentOrder3.put("UserImage",UserImageS);
                                                            studentOrder3.put("RollNo",RollNumS);
                                                            studentOrder3.put("Verified","false");
                                                            studentOrder3.put("ClubLocation",ClubLocationS);
                                                            studentOrder3.put("TeamName",teamnameM);
                                                            studentOrder3.put("Date",todays_date);
                                                            studentOrder3.put("Time",todays_time);

                                                            eventReference.child(eventkeyM).child("Tickets").child(TicketKey).child("BookedTickets").child(key).child(user.getUid()).updateChildren(studentOrder3).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {


                                                                }
                                                            });


                                                            DatabaseReference aa=adminReference.child(ClubUID).child("Attendees").child(user.getUid()).push();
                                                            String keyx=aa.getKey();
                                                            final HashMap<String,Object> adminStore=new HashMap<>();
                                                            adminStore.put("UserUID",user.getUid());
                                                            adminStore.put("Key",keyx);
                                                            adminStore.put("EventName",EventName);
                                                            adminStore.put("DateBooked",todays_date);
                                                            adminStore.put("EventKey",EventKeyX);
                                                            aa.updateChildren(adminStore);



                                                            parentReference.child(user.getUid()).child("BookedTickets").child(key).child("TeamParticipants").child(KeyQ).child("UserUID").setValue(user.getUid());

                                                            parentReference.child(user.getUid()).child("BookedTickets").child(key).child("TeamParticipants").child(KeyQ).child("UserName").setValue(UserNameS).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){


                                                                        progressBarX.setVisibility(View.GONE);
                                                                        Toast.makeText(InviteTeam.this,"Registration Successful",Toast.LENGTH_LONG).show();


                                                                        Intent intent=new Intent(InviteTeam.this, TicketActivity.class);

                                                                        Log.e("Q1",""+EventKeyX);
                                                                        Log.e("Q2",""+TicketKey);
                                                                        Log.e("Q3",""+user.getUid());
                                                                        Log.e("Q4",""+EventURL);
                                                                        Log.e("Q5",""+key);
                                                                        Log.e("Q6",""+teamnameM);

                                                                        intent.putExtra("EventKey",EventKeyX);
                                                                        intent.putExtra("TicketKey",TicketKey);
                                                                        intent.putExtra("UserUID",user.getUid());
                                                                        intent.putExtra("EventURL", EventURL);
                                                                        intent.putExtra("Key",key);
                                                                        intent.putExtra("TeamName",teamnameM);
                                                                        startActivity(intent);


                                                                    }
                                                                }
                                                            });

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







                                        }

                                        else{
                                            Toast.makeText(InviteTeam.this,"Error occured. Send screenshot.",Toast.LENGTH_LONG).show();
                                        }


                                    }
                                });

                            }
                            else{
                                Toast.makeText(InviteTeam.this,"Error occured. Send screenshot.",Toast.LENGTH_LONG).show();
                            }
                            progressBar2.setVisibility(View.INVISIBLE);
                            parent_view.setVisibility(View.VISIBLE);
                            progressBarX.setVisibility(View.GONE);
                        }
                    });


                }
                else{
                    Log.e("RespCodeX",""+RespCodeI);
                    Toast.makeText(InviteTeam.this,"Response Code: "+RespCodeI,Toast.LENGTH_LONG).show();
                    progressBar2.setVisibility(View.INVISIBLE);
                    parent_view.setVisibility(View.VISIBLE);
                    progressBarX.setVisibility(View.GONE);
                }




            }
            public void networkNotAvailable() {

                Toast.makeText(getApplicationContext(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();
            }
            public void clientAuthenticationFailed(String inErrorMessage) {
                Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + inErrorMessage, Toast.LENGTH_LONG).show();
            }
            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                Toast.makeText(getApplicationContext(), "Unable to load webpage " + inErrorMessage, Toast.LENGTH_LONG).show();
            }
            public void onBackPressedCancelTransaction() {

                Toast.makeText(getApplicationContext(), "Transaction cancelled" , Toast.LENGTH_LONG).show();
            }
            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {}
        });

    }


    private void getTicketDetails(final FirebaseCallbackXX2 firebaseCallbackXX){

        Log.e("PriceCheck4","h"+linksender);
        Log.e("PriceCheck5","h"+key);

        parentReference.child(linksender).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String EventBannerURL= dataSnapshot.child("BookedTickets").child(key).child("EventBannerURL").getValue(String.class);
                String EventKey= dataSnapshot.child("BookedTickets").child(key).child("EventKey").getValue(String.class);
                String EventName= dataSnapshot.child("BookedTickets").child(key).child("EventName").getValue(String.class);
                // String Key= dataSnapshot.child("EventBannerURL").getValue(String.class);
                String TicketKey= dataSnapshot.child("BookedTickets").child(key).child("TicketKey").getValue(String.class);
                String TicketName= dataSnapshot.child("BookedTickets").child(key).child("TicketName").getValue(String.class);
                String EventURL=dataSnapshot.child("BookedTickets").child(key).child("EventURL").getValue(String.class);
                String UserName=dataSnapshot.child("UserName").getValue(String.class);
                String FeeOpt=dataSnapshot.child("BookedTickets").child(key).child("FeePaid").getValue(String.class);
                String TeamName=dataSnapshot.child("BookedTickets").child(key).child("TeamName").getValue(String.class);
                String Fee;
                String FeeOptXX;
                String PRICE_CHECK;
                if(dataSnapshot.child("BookedTickets").child(key).child("PRICE_CHECK").getValue(String.class).equals("Team")){
                    Fee="Already paid by team leader";
                    FeeOptXX="0";
                    PRICE_CHECK="Team";
                }
                else{
                    Fee="₹ "+FeeOpt;
                    FeeOptXX=FeeOpt;
                    PRICE_CHECK="Individual";
                }
                if(Integer.parseInt(FeeOpt)==0){
                    Fee="Free";
                    FeeOptXX="0";
                }

                Log.e("PriceCheck3","h"+PRICE_CHECK);


                firebaseCallbackXX.onCallBack1(EventBannerURL);
                firebaseCallbackXX.onCallBack2(EventKey);
                firebaseCallbackXX.onCallBack3(EventName);
                firebaseCallbackXX.onCallBack4(TicketKey);
                firebaseCallbackXX.onCallBack5(TicketName);
                firebaseCallbackXX.onCallBack7(EventURL);
                firebaseCallbackXX.onCallBack8(UserName);
                firebaseCallbackXX.onCallBack9(FeeOptXX);
                firebaseCallbackXX.onCallBack10(PRICE_CHECK);

                firebaseCallbackXX.onCallBack11(TeamName);











            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    private void getTodaysTime(){

        Calendar calendar = Calendar.getInstance();
        String am_pm = null;
        String monthS=null;


        int hourS=calendar.get(Calendar.HOUR_OF_DAY);
        String yearS = Integer.toString(calendar.get(Calendar.YEAR)) ;
        int monthofYear = calendar.get(Calendar.MONTH);
        String dayS = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        int minuteS=calendar.get(Calendar.MINUTE);
        //    Toast.makeText(getActivity(),hourS+minutesS,Toast.LENGTH_SHORT).show();

        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hourS);
        datetime.set(Calendar.MINUTE, minuteS);

        String minute;
        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
            am_pm= "AM";
        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";

        if(datetime.get(Calendar.MINUTE)<10){

            minute= "0"+datetime.get(Calendar.MINUTE);
        }
        else minute=""+datetime.get(Calendar.MINUTE);

        String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ? "12" : datetime.get(Calendar.HOUR) + "";
        todays_time=strHrsToShow + ":" + minute + " " + am_pm;



        switch(monthofYear+1) {
            case 1:
                monthS = "Jan";
                break;
            case 2:
                monthS = "Feb";
                break;
            case 3:
                monthS= "March";
                break;
            case 4:
                monthS= "April";
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

        todays_date=""+dayS+" "+monthS+", "+yearS;





    }

}
