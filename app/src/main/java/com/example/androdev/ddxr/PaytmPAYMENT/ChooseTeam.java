package com.example.androdev.ddxr.PaytmPAYMENT;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.androdev.ddxr.R;
import com.example.androdev.ddxr.UserFragments.FirebaseCallBackXM;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.messaging.FirebaseMessaging;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class ChooseTeam extends AppCompatActivity {

    Toolbar toolbar;

    EditText et_teamname;
    Button register;
   // String TeamName;
    String EventKey;
    DatabaseReference eventReference;
    DatabaseReference parentReference;
    ProgressBar progressBar2;
    LinearLayout parent_view;
    String EventBannerURLX;
    String thiseventurl;
    FirebaseUser user;
    String EventNameX;
    String MaxParticipants;
    String TicketKey;
    String todays_date;
    String todays_time;


    String ClubUID;
    DatabaseReference adminReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_team);

        user= FirebaseAuth.getInstance().getCurrentUser();

        et_teamname=(EditText) findViewById(R.id.et_teamname);
        register=(Button)findViewById(R.id.register);
        parent_view=(LinearLayout)findViewById(R.id.parent_view);
        progressBar2=(ProgressBar) findViewById(R.id.progress_bar2);


        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Book team ticket");

        toolbar.setTitleTextColor(ContextCompat.getColor(ChooseTeam.this,R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        final Intent i=getIntent();
         EventKey=i.getStringExtra("EventKey");
         TicketKey=i.getStringExtra("TicketKey");
        eventReference = FirebaseDatabase.getInstance("https://ddrx-events-172d3.firebaseio.com/").getReference();
         parentReference=FirebaseDatabase.getInstance().getReference();
        adminReference = FirebaseDatabase.getInstance("https://ddrx-admin-172d3.firebaseio.com/").getReference();

         ClubUID=i.getStringExtra("ClubUID");

         getEventURL();

        Log.e("EventKeyC",""+EventKey);





        getEventImageURL(new FirebaseCallBackXM() {
            @Override
            public void onCallBack1(String string) {
                EventBannerURLX=string;

                Log.e("myself2",""+EventBannerURLX);


            }

            @Override
            public void onCallBack2(String string) {
                EventNameX=string;

            }

            @Override
            public void onCallBack3(String string) {
                MaxParticipants=string;
            }


        });



         register.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 if(et_teamname.getText().toString().trim().equals("")){
                     Toast.makeText(ChooseTeam.this,"Team Name cannot be empty",Toast.LENGTH_LONG).show();
                 }
                 else{

                     parent_view.setVisibility(View.INVISIBLE);
                     progressBar2.setVisibility(View.VISIBLE);
                     processPaytm(i.getStringExtra("UserUID"),i.getStringExtra("EventKey"),i.getStringExtra("TicketKey"),i.getStringExtra("TicketPrice"),i.getStringExtra("TicketName"),i.getStringExtra("EventName"),i.getStringExtra("TEAM_CHECK"),i.getStringExtra("PRICE_CHECK"),et_teamname.getText().toString().trim());

                 }

             }
         });



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void processPaytm(final String userUID, String EventKeyX, final String TicketKey,final String TicketPrice,final String TicketName,final String EventName,final String TeamCheck, final String PriceCheck,final String TeamName) {

        if (ContextCompat.checkSelfPermission(ChooseTeam.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ChooseTeam.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }

        String custID = userUID;
        final int price=Integer.parseInt(TicketPrice);
        final String eventfirst=EventKey.substring(0,4);
        final String orderID = eventfirst+""+ ThreadLocalRandom.current().nextInt(100000000, 999999999);
        String callBackurl = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID" + orderID;

        final Paytm paytm = new Paytm(
                "copYkQ20143736243713",
                "WAP",
                "1.02"  ,
                "DEFAULT",
                callBackurl,
                "Retail",
                orderID,
                custID
        );

        WebServiceCaller.getClient().getChecksum(paytm.getmId(), paytm.getOrderId(), paytm.getCustId()
                , paytm.getChannelId(), paytm.getTxnAmount(), paytm.getWebsite(), paytm.getCallBackUrl(), paytm.getIndustryTypeId()
        ).enqueue(new retrofit2.Callback<Checksum>() {
            @Override
            public void onResponse(retrofit2.Call<Checksum> call, retrofit2.Response<Checksum> response) {
                if (response.isSuccessful()) {
                    processToPay(response.body().getChecksumHash(),paytm,userUID,EventKey,TicketKey,orderID,TicketPrice,TicketName,EventName,TeamCheck,PriceCheck,TeamName);

                    Log.e("Work","Yes");
                    Log.e("WorkX",""+response.message());
                }
                else{
                    progressBar2.setVisibility(View.INVISIBLE);
                    parent_view.setVisibility(View.VISIBLE);
                    Toast.makeText(ChooseTeam.this,response.message(),Toast.LENGTH_LONG).show();
                    Log.e("Work","No");
                    Log.e("WorkX","h"+response.message());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Checksum> call, Throwable t) {
                Log.e("Work","Nota");
                progressBar2.setVisibility(View.INVISIBLE);
                Toast.makeText(ChooseTeam.this,"Response failure",Toast.LENGTH_LONG).show();
                parent_view.setVisibility(View.VISIBLE);
            }
        });


    }

    private void processToPay(String checksumHash, Paytm paytm, final String UserUID, final String EventKey, final String TicketKey, final String orderID,final String price,final String TicketName,final String EventName,final String TeamCheck,final String PriceCheck,final String TeamNameX) {
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

               Log.e("myself",""+EventBannerURLX);

                if((RespCodeI==1)){

                    FirebaseMessaging.getInstance().subscribeToTopic(TicketKey);


                    getTodaysTime();


                    final DatabaseReference parentTicketReference= parentReference.child(user.getUid()).child("BookedTickets").push();
                  final  String KeyZ=parentTicketReference.getKey();



                    final HashMap<String,Object> studentOrder=new HashMap<>();
                    studentOrder.put("TicketKey",TicketKey);
                    studentOrder.put("OrderID",orderID );
                    studentOrder.put("EventKey",EventKey);
                    studentOrder.put("FeePaid",price);
                    studentOrder.put("EventName",EventName);
                    studentOrder.put("TicketName",TicketName);
                    studentOrder.put("EventBannerURL",EventBannerURLX);

                    studentOrder.put("TEAM_CHECK",TeamCheck);
                    studentOrder.put("PRICE_CHECK",PriceCheck);
                    studentOrder.put("Key",KeyZ);
                    studentOrder.put("Date",todays_date);
                    studentOrder.put("Time",todays_time);
                    studentOrder.put("Leader","true");
                    studentOrder.put("EventURL",thiseventurl);
                    studentOrder.put("TeamName",TeamNameX);
                    studentOrder.put("MaxParticipants",MaxParticipants);
                    Log.e("Query",""+MaxParticipants);








                    parentTicketReference.updateChildren(studentOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){



                                eventReference.child(EventKey).child("EventURL").setValue(thiseventurl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){






                                            DatabaseReference newparentTicket=parentReference.child(user.getUid()).child("BookedTickets").child(KeyZ).child("TeamParticipants").push();
                                          final  String KeyQ=newparentTicket.getKey();


                                            parentReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                  String UserNameS=  dataSnapshot.child(user.getUid()).child("UserName").getValue(String.class);
                                                  String UserImageS= dataSnapshot.child(user.getUid()).child("ImageUrl").getValue(String.class);
                                                    String RollNumS= dataSnapshot.child(user.getUid()).child("RollNum").getValue(String.class);
                                                    String PhoneNo=dataSnapshot.child(user.getUid()).child("Phone").getValue(String.class);
                                                    String ClubLocationS=dataSnapshot.child(user.getUid()).child("ClubLocation").getValue(String.class);



                                                    final HashMap<String,Object> studentOrder2=new HashMap<>();

                                                    studentOrder2.put("OrderID",orderID );
                                                    studentOrder2.put("FeePaid",price);
                                                    studentOrder2.put("UserUID",user.getUid());
                                                    studentOrder2.put("UserName",UserNameS);
                                                    studentOrder2.put("UserImage",UserImageS);
                                                    studentOrder2.put("RollNo",RollNumS);
                                                    studentOrder2.put("Phone",PhoneNo);
                                                    studentOrder2.put("Date",todays_date);
                                                    studentOrder2.put("Time",todays_time);

                                                    studentOrder2.put("Verified","false");
                                                    studentOrder2.put("ClubLocation",ClubLocationS);
                                                    studentOrder2.put("TeamName",TeamNameX);

                                                    eventReference.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(KeyZ).child("TeamName").setValue(TeamNameX);

                                                    eventReference.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(KeyZ).child(user.getUid()).updateChildren(studentOrder2);


                                                    DatabaseReference aa=adminReference.child(ClubUID).child("Attendees").child(UserUID).push();
                                                    String keyx=aa.getKey();
                                                    final HashMap<String,Object> adminStore=new HashMap<>();
                                                    adminStore.put("UserUID",UserUID);
                                                    adminStore.put("Key",keyx);
                                                    adminStore.put("EventName",EventName);
                                                    adminStore.put("DateBooked",todays_date);
                                                    adminStore.put("EventKey",EventKey);
                                                    aa.updateChildren(adminStore);

                                                    parentReference.child(user.getUid()).child("BookedTickets").child(KeyZ).child("TeamParticipants").child(KeyQ).child("UserUID").setValue(user.getUid());

                                                    parentReference.child(user.getUid()).child("BookedTickets").child(KeyZ).child("TeamParticipants").child(KeyQ).child("UserName").setValue(UserNameS).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                Intent intent=new Intent(ChooseTeam.this, TicketActivity.class);
                                                                Log.e("EventKeyV",""+EventKey);
                                                                intent.putExtra("EventKey",EventKey);
                                                                intent.putExtra("TicketKey",TicketKey);
                                                                intent.putExtra("UserUID",UserUID);
                                                                intent.putExtra("EventURL", thiseventurl);
                                                                intent.putExtra("Key",KeyZ);
                                                                intent.putExtra("TeamName",TeamNameX);

                                                                startActivity(intent);
                                                                finish();

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
                                            Toast.makeText(ChooseTeam.this,"Error occured. Send screenshot.",Toast.LENGTH_LONG).show();
                                        }


                                    }
                                });

                            }
                            else{
                                Toast.makeText(ChooseTeam.this,"Error occured. Send screenshot.",Toast.LENGTH_LONG).show();
                            }
                            progressBar2.setVisibility(View.INVISIBLE);
                            parent_view.setVisibility(View.VISIBLE);
                        }
                    });


                }
                else{
                    Log.e("RespCodeX",""+RespCodeI);
                    Toast.makeText(ChooseTeam.this,"Response Code: "+RespCodeI,Toast.LENGTH_LONG).show();
                    progressBar2.setVisibility(View.INVISIBLE);
                    parent_view.setVisibility(View.VISIBLE);
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

    public void getEventURL(){


        String uri="http://hungryhunter96.github.io/share_event?Key="+EventKey;


        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(uri))
                .setDomainUriPrefix("https://ddxr.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())

                .buildShortDynamicLink()
                .addOnCompleteListener(ChooseTeam.this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {


                            Uri shortLink = task.getResult().getShortLink();
                            thiseventurl=shortLink.toString();


                        } else {

                            Toast.makeText(ChooseTeam.this,"Event link cannot be created.",Toast.LENGTH_SHORT).show();

                        }
                    }
                });



    }



    public void getEventImageURL(final FirebaseCallBackXM firebaseCallbackX){

        eventReference.child(EventKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String EventBannerURL=dataSnapshot.child("EventThumbnailURL").getValue(String.class);
                String EventName=dataSnapshot.child("EventName").getValue(String.class);
                String MaxParticipantS=dataSnapshot.child("Tickets").child(TicketKey).child("TeamMembers").getValue(String.class);
                firebaseCallbackX.onCallBack1(EventBannerURL);
                firebaseCallbackX.onCallBack2(EventName);
                firebaseCallbackX.onCallBack3(MaxParticipantS);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



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
