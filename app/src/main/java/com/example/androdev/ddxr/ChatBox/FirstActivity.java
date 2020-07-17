package com.example.androdev.ddxr.ChatBox;

import android.content.Intent;

import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androdev.ddxr.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FirstActivity extends AppCompatActivity {


    FirebaseDatabase messageDatabase;
    TextView receiverName;
    DatabaseReference messageReference;
    FirebaseUser user;
    String todays_time;
    String todays_date;
    int count;
    EditText messageText;
    Boolean date_change;
    Button sendButton;
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    ArrayList<BaseMessage> baseMessageList=new ArrayList<>();
    FirebaseDatabase messageList;
    DatabaseReference messageListReference;
    DatabaseReference newrefer;
    String key3;
    ImageView BackButton;
    String date_previuos;
    String date_after;

    DatabaseReference newsendingReference;
    String ReceiverProfileImage;
    String ReceiverName;
    String ReceiverUID;
    int count2;
    String SenderProfileImage;
    final Map<String, Object>  newSendingData = new HashMap<>();;
    String SenderName;
    DatabaseReference dbref2;
    DatabaseReference parentRefernce;
    String parentMsgList;
    String profileMsg;
    CircleImageView ReceiverImage;
    int newMessages ;
    int y;
    ProgressBar progressBar;
    DatabaseReference adminReference;
    TextView no_messages;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        newMessages=0;
        y=0;

        no_messages=(TextView)findViewById(R.id.no_messages) ;


        Intent i = getIntent();
        parentMsgList=i.getStringExtra("ActivityName");

        ReceiverName = i.getStringExtra("ReceiverName");
        ReceiverUID = i.getStringExtra("ReceiverUID");
        // ReceiverProfileImage=i.getStringExtra("ReceiverImageUrl");
        //  SenderProfileImage=i.getStringExtra("SenderProfileImage");
        SenderName=i.getStringExtra("SenderName");
        //    Toast.makeText(FirstActivity.this,"Hello "+SenderName,Toast.LENGTH_SHORT).show();

        parentRefernce=FirebaseDatabase.getInstance("https://ddrx-172d3.firebaseio.com/").getReference();
        adminReference=FirebaseDatabase.getInstance("https://ddrx-admin-172d3.firebaseio.com/").getReference();


        getSenderEditedDetails(new FirebaseCallBack3() {
            @Override
            public void onCallBack(String string) {
                SenderProfileImage = string;
                //   Toast.makeText(
            }

        });


        getReceiverEditedDetails(new FirebaseCallBack4() {
            @Override
            public void onCallBack(String string) {
                ReceiverProfileImage = string;

            }

        });










        mMessageAdapter = new MessageListAdapter(this, baseMessageList);

        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);

        //   mMessageRecycler.setLayoutManager(new WrapContentLinearLayoutManager(FirstActivity.this, LinearLayout.VERTICAL,false));
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        BackButton=(ImageView)findViewById(R.id.imageView_backArrow);
        progressBar=(ProgressBar) findViewById(R.id.progress_bar);


        mMessageRecycler.setAdapter(mMessageAdapter);
       /* mMessageAdapter.setOnTopReachedListener(new OnTopReachedListener() {
            @Override
            public void onTopReached(int position) {
                //your code goes here

                baseMessageList.clear();
                mMessageRecycler.getRecycledViewPool().clear();
//                mMessageAdapter.notifyDataSetChanged();


                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // Actions to do after 10 seconds
                        getMessages(15);
                    }
                }, 2000);

                mMessageRecycler.post(new Runnable()
                {
                    @Override
                    public void run() {






                    }
                });


                Toast.makeText(FirstActivity.this,"Time to load new messages",Toast.LENGTH_SHORT).show();
            }
        });*/


        messageText = (EditText) findViewById(R.id.edittext_chatbox);
        messageDatabase = FirebaseDatabase.getInstance("https://messages23c62.firebaseio.com/");
        messageReference = messageDatabase.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        messageList=FirebaseDatabase.getInstance("https://messagelist-6b6d2.firebaseio.com/");
        messageListReference=messageList.getReference();
        sendButton = (Button) findViewById(R.id.button_chatbox_send);
        receiverName=(TextView) findViewById(R.id.receiver_name);
        ReceiverImage=(CircleImageView)findViewById(R.id.receiver_profile_image);

        receiverName.setText(ReceiverName);



       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(ReceiverName);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/




        Log.v("Arraylist count",""+baseMessageList.size());







        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //validate and send message
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(messageText.getText().toString().length()==0){

                    Toast.makeText(FirstActivity.this,"Messages must not be empty",Toast.LENGTH_SHORT).show();
                }

                else{

                    getTodaysTime();
                    final DatabaseReference sendingnodereference = messageReference.child(user.getUid()).child(ReceiverUID).push();
                    final Map<String, Object> newSendingData = new HashMap<>();
                    Map<String, Object> newReceivingData = new HashMap<>();

                    newsendingReference = messageReference.child(user.getUid()).child(ReceiverUID);
                    String key = sendingnodereference.getKey();
                    Log.e("key", "" + key);
















                    newSendingData.put("Text", messageText.getText().toString());
                    newSendingData.put("Time", todays_time);
                    newSendingData.put("Date", todays_date);
                    newSendingData.put("TextType", "Sending");
                    newSendingData.put("OtherParty", ReceiverUID);
                    newSendingData.put("OtherPartyName",ReceiverName);
                    newSendingData.put("OtherPartyProfileImage",ReceiverProfileImage);
                    newSendingData.put("MyName",SenderName);





                    DatabaseReference receivingnodereference = messageReference.child(ReceiverUID).child(user.getUid()).push();
                    DatabaseReference newreceivingReference = messageReference.child(ReceiverUID).child(user.getUid());
                    String key2 = receivingnodereference.getKey();
                    Log.e("key", "" + key);



                    //  Toast.makeText(FirstActivity.this,""+SenderName,Toast.LENGTH_SHORT).show();



                    newReceivingData.put("Text",messageText.getText().toString());
                    newReceivingData.put("Time",todays_time);
                    newReceivingData.put("Date",todays_date);
                    newReceivingData.put("TextType","Receiving");
                    newReceivingData.put("OtherParty",user.getUid());
                    newReceivingData.put("OtherPartyName",SenderName);
                    newReceivingData.put("OtherPartyProfileImage",SenderProfileImage);
                    newReceivingData.put("MyName",ReceiverName);





                    newreceivingReference.child(key2).updateChildren(newReceivingData);

                    newsendingReference.child(key).updateChildren(newSendingData);



                    dbref2=FirebaseDatabase.getInstance("https://messagelist-6b6d2.firebaseio.com/").getReference();
                    DatabaseReference dbex=dbref2.child(user.getUid()).push();
                    DatabaseReference dbex2=dbref2.child(ReceiverUID).push();
                    String key5=dbex2.getKey();
                    String key4=dbex.getKey();
                    dbref2.child(user.getUid()).child(key4).setValue(ReceiverUID);
                    dbref2.child(ReceiverUID).child(key5).setValue(user.getUid());

                    final Map<String, Object> newSendingData2 = new HashMap<>();
                    newSendingData2.put("Text", messageText.getText().toString());
                    newSendingData2.put("Time", todays_time);
                    newSendingData2.put("Date", todays_date);
                    newSendingData2.put("TextType", "Sending");
                    newSendingData2.put("OtherParty", ReceiverUID);
                    newSendingData2.put("OtherPartyName",ReceiverName);
                    Log.e("RecieveC","h"+ReceiverProfileImage);
                    newSendingData2.put("OtherPartyProfileImage",ReceiverProfileImage);
                    newSendingData2.put("MyName",SenderName);
                    newSendingData2.put("UserImageUrl",SenderProfileImage);

                    Map<String, Object> newReceivingData2 = new HashMap<>();
                    newReceivingData2.put("Text",messageText.getText().toString());
                    newReceivingData2.put("Time",todays_time);
                    newReceivingData2.put("Date",todays_date);
                    newReceivingData2.put("TextType","Receiving");
                    newReceivingData2.put("OtherParty",user.getUid());
                    newReceivingData2.put("OtherPartyName",SenderName);
                    newReceivingData2.put("OtherPartyProfileImage",SenderProfileImage);
                    newReceivingData2.put("MyName",ReceiverName);
                    newReceivingData2.put("UserImageUrl",ReceiverProfileImage);

                    dbref2.child(user.getUid()).child(key4).setValue(newSendingData2);
                    //dbref2.child(user.getUid()).child(key4).child("UserImageUrl").setValue(SenderProfileImage);
                    dbref2.child(ReceiverUID).child(key5).setValue(newReceivingData2);
                    // dbref2.child(ReceiverUID).child(key5).child("UserImageUrl").setValue(ReceiverProfileImage);
























                    messageText.setText("");




                }




            }
        });




        getMessages(20);






    }


   /* public void fetch(String key3){

        messageListReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                dbref2.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        if(dataSnapshot.getValue().equals(ReceiverUID)){
                            count=count+1;
                            if(count>1){

                            }
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
                })





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






    }*/








    public void AddSingleValue(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 10 seconds
                newrefer.updateChildren(newSendingData);
            }
        }, 10000);



    }


    public void getSenderEditedDetails(final FirebaseCallBack3 firebasecallback3){


        parentRefernce.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user.getUid())){
                    String SenderProfileImageS=dataSnapshot.child(user.getUid()).child("ImageUrl").getValue(String.class);
                    firebasecallback3.onCallBack(SenderProfileImageS);
                }
                else{
                    adminReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String SenderProfileImageS= dataSnapshot.child(user.getUid()).child("ProfileImageUrl").getValue(String.class);
                            firebasecallback3.onCallBack(SenderProfileImageS);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    public void getReceiverEditedDetails(final FirebaseCallBack4 firebasecallback4){



        parentRefernce.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(ReceiverUID)){
                    Log.e("RecieveD","No");
                    String ReceiverProfileImageS=dataSnapshot.child(ReceiverUID).child("ImageUrl").getValue(String.class);
                    if(ReceiverProfileImageS!=null){

                        Picasso.get()
                                .load(Uri.parse(ReceiverProfileImageS))

                                .noFade()
                                .error(R.drawable.add_photo3)
                                .into(ReceiverImage);

                    }
                    firebasecallback4.onCallBack(ReceiverProfileImageS);
                }
                else{

                    adminReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.e("RecieveE","No");
                            Log.e("Recievek","h"+ReceiverUID);
                            Log.e("Recievel","h"+dataSnapshot.hasChild(ReceiverUID));
                            Log.e("Recieveg","h"+dataSnapshot.child(ReceiverUID).hasChild("ProfileImageUrl"));
                            String ReceiverProfileImageS=dataSnapshot.child(ReceiverUID).child("ProfileImageUrl").getValue(String.class);
                            if(ReceiverProfileImageS!=null){

                                Log.e("Recievef","No");

                                Picasso.get()
                                        .load(Uri.parse(ReceiverProfileImageS))

                                        .noFade()
                                        .error(R.drawable.add_photo3)
                                        .into(ReceiverImage);

                            }
                            firebasecallback4.onCallBack(ReceiverProfileImageS);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(parentMsgList.equals("HomeMessage")){

            Intent intent=new Intent(FirstActivity.this,HomeMessage.class);
            startActivity(intent);

        }

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

    public void getMessages(int x){





        final Query messageQuery=messageReference.child(user.getUid()).child(ReceiverUID).orderByKey();
        messageQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                messageQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {

                        String message = snapshot.child("Text").getValue(String.class);
                        String date = snapshot.child("Date").getValue(String.class);
                        String time = snapshot.child("Time").getValue(String.class);
                        String textType=snapshot.child("TextType").getValue(String.class);
                        //  String otherparty=snapshot.child("OtherParty").getValue(String.class);

                        Log.e("fetch",""+textType);
                        Log.e("Arraylist count3",""+baseMessageList.size());

                        date_after=date;



                        Log.e("Date previous",""+date_previuos);
                        Log.e("Date after",""+date_after);


                        if(date_previuos==null||!date_previuos.equals(date_after)){
                            date_change=true;
                        }
                        else{
                            date_change=false;


                        }

                        Log.e("Change",""+date_change);

                        baseMessageList.add(new BaseMessage(message, date, time, textType,date_change));
                        no_messages.setVisibility(View.INVISIBLE );
                        Log.e("User", "" + textType);
                        Log.e("Arraylist count2", "" + baseMessageList.size());

                        date_previuos=date;




                        mMessageAdapter.notifyDataSetChanged();
                        mMessageRecycler.scrollToPosition(baseMessageList.size()-1);














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



                messageQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(baseMessageList.size()==0){
                            no_messages.setVisibility(View.VISIBLE);
                        }
                        else{
                            no_messages.setVisibility(View.INVISIBLE );
                        }
                        progressBar.setVisibility(View.INVISIBLE);

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
}
