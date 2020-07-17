package com.example.androdev.ddxr.PaytmPAYMENT;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.androdev.ddxr.R;
import com.example.androdev.ddxr.UserFragments.BlogActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class TicketActivity extends AppCompatActivity {

    private static final int WIDTH = 320;
    private static final int HEIGHT = 320;
    String UserUID;
    String TicketKey;
    String EventKey;
    TextView EventName;
    TextView TicketName;
    TextView TicketDescription;
    TextView Date;
    TextView FeePaid;
    TextView WebLink;
    String WebLinkS;
    DatabaseReference eventsReference;
    Toolbar toolBar;
    ArrayList<String> mobileArray;
    ArrayAdapter adapter;
    ListView listView;
    DatabaseReference parentReference;
    FirebaseUser user;
    String Key;
    TextView OrderID;
    LinearLayout pl_team;
    LinearLayout LL_TeamName;
    CardView pl_invite;
    TextView inviteurl;
    TextView TeamName;
    String TeamNameX;
    ListAdapterX customAdapter;
    TextView check_verify;
    ImageView send_whatsapp;
    TextView UserNameTV;
    TextView DateandTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        mobileArray=new ArrayList<>();
         customAdapter=new ListAdapterX(this,mobileArray);

        toolBar=(Toolbar) findViewById(R.id.toolbar);
        toolBar.setTitleTextColor(Color.WHITE);
        user= FirebaseAuth.getInstance().getCurrentUser();
        OrderID=(TextView)findViewById(R.id.OrderID);
        DateandTime=(TextView) findViewById(R.id.BookingTime);


        adapter = new ArrayAdapter<String>(this,
                R.layout.team_listview, mobileArray);
        parentReference=FirebaseDatabase.getInstance().getReference();
        listView=(ListView) findViewById(R.id.list_view);
        listView.setAdapter(customAdapter);
        pl_team=(LinearLayout) findViewById(R.id.pl_team);
        LL_TeamName=(LinearLayout) findViewById(R.id.LL_TeamName);
        pl_invite=(CardView)findViewById(R.id.pl_invite);
        inviteurl=(TextView) findViewById(R.id.inviteurl);
        send_whatsapp=(ImageView)findViewById(R.id.send_whatsapp);
        UserNameTV=(TextView) findViewById(R.id.AttendeeName);


        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Intent i=getIntent();
         UserUID=i.getStringExtra("UserUID");
         TicketKey=i.getStringExtra("TicketKey");
         EventKey=i.getStringExtra("EventKey");
         TeamNameX=i.getStringExtra("TeamName");

         Log.e("TicketKey",""+TicketKey);
         Log.e("EventKey",""+EventKey);
         Key=i.getStringExtra("Key");
        // WebLinkS=i.getStringExtra("EventURL");
        Log.e("KeyL",""+Key);


         eventsReference= FirebaseDatabase.getInstance("https://ddrx-events-172d3.firebaseio.com").getReference();
         parentReference=FirebaseDatabase.getInstance().getReference();
         check_verify=(TextView) findViewById(R.id.verification_check);

         EventName=(TextView) findViewById(R.id.EventName);
        TicketName=(TextView) findViewById(R.id.TicketName);
        TeamName=(TextView) findViewById(R.id.TeamName);
        TicketDescription=(TextView) findViewById(R.id.TicketDescription);
        Date=(TextView) findViewById(R.id.Date);
        FeePaid=(TextView) findViewById(R.id.FeePaid);
        WebLink=(TextView) findViewById(R.id.WebLink);

        if(Key!=null){

            parentReference.child(user.getUid()).child("BookedTickets").child(Key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild("Leader")){
                        pl_invite.setVisibility(View.VISIBLE);
                    }
                    else{
                        pl_invite.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            } );

        }




     /*   parentReference.child(user.getUid()).child("BookedTickets").child(Key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("Value","hi"+dataSnapshot.child(user.getUid()).child("BookedTickets").hasChild(Key));

 *//*String OrderIDZ= dataSnapshot.child("OrderID").getValue(String.class);
                String FeePaidZ=dataSnapshot.child("FeePaid").getValue(String.class);
                String TeamNameZ=dataSnapshot.child("TeamName").getValue(String.class);
              OrderID.setText(": "+OrderIDZ);
              FeePaid.setText(": ₹ "+FeePaidZ);
                TeamName.setText(": "+TeamNameZ);*//*

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


        eventsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                EventName.setText(": "+dataSnapshot.child(EventKey).child("EventName").getValue(String.class));
                String OrderIDZ;
                String FeePaidZ;
                String TeamNameZ;
                String BookingDate;
                String BookingTime;
                String UserName;



                if(dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("TEAM_CHECK").getValue(String.class).equals("Team")){
                    OrderIDZ= dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(Key).child(user.getUid()).child("OrderID").getValue(String.class);
                     FeePaidZ=dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(Key).child(user.getUid()).child("FeePaid").getValue(String.class);
                    BookingDate=dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(Key).child(user.getUid()).child("Date").getValue(String.class);
                    BookingTime=dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(Key).child(user.getUid()).child("Time").getValue(String.class);
                    UserName=dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(Key).child(user.getUid()).child("UserName").getValue(String.class);
                     TeamNameZ=dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(Key).child(user.getUid()).child("TeamName").getValue(String.class);
                     if(dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(Key).child(user.getUid()).child("Verified").getValue(String.class).equals("true")){
                         check_verify.setText("Verified");
                     }
                     else{
                         check_verify.setText("Not Verified");
                     }

                }
                else{
                    OrderIDZ= dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(user.getUid()).child("OrderID").getValue(String.class);
                    FeePaidZ=dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(user.getUid()).child("FeePaid").getValue(String.class);
                    TeamNameZ=dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(user.getUid()).child("TeamName").getValue(String.class);
                    BookingDate=dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(user.getUid()).child("Date").getValue(String.class);
                    BookingTime=dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(user.getUid()).child("Time").getValue(String.class);
                    UserName=dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(user.getUid()).child("UserName").getValue(String.class);
                    if(dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(user.getUid()).child("Verified").getValue(String.class).equals("true")){

                        check_verify.setText("Verified");
                    }
                    else{
                        check_verify.setText("Not Verified");
                    }
                }


                UserNameTV.setText(": "+UserName);
                DateandTime.setText(": "+BookingDate+" at "+BookingTime);

                OrderID.setText(": "+OrderIDZ);
                FeePaid.setText(": ₹ "+FeePaidZ);
                TeamName.setText(": "+TeamNameZ);


                if(dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("TEAM_CHECK").getValue(String.class).equals("Individual")){
                    pl_team.setVisibility(View.GONE);
                    pl_invite.setVisibility(View.GONE);
                    LL_TeamName.setVisibility(View.GONE);
                }
               String StartDate=dataSnapshot.child(EventKey).child("StartDate").getValue(String.class);
               String EndDate=dataSnapshot.child(EventKey).child("EndDate").getValue(String.class);


               if(StartDate.equals(EndDate)){
                   Date.setText(": "+StartDate);
               }
               else{
                   Date.setText(": "+StartDate+" - "+ EndDate);
               }

                TicketName.setText(": "+dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("Name").getValue(String.class));
                getSupportActionBar().setTitle(dataSnapshot.child(EventKey).child("EventName").getValue(String.class));
                getSupportActionBar().setSubtitle(dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("Name").getValue(String.class));
                WebLink.setText(dataSnapshot.child(EventKey).child("EventURL").getValue(String.class));
                TicketDescription.setText(": "+dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("Description").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final ArrayList<String> records=new ArrayList<>();


        if(Key!=null)  {


            getInviteURL();

         //  eventsReference.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(Key).addChildEventListener(new )

            eventsReference.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(Key).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    if(dataSnapshot.child("UserName").getValue(String.class)!=null){
                        mobileArray.add (dataSnapshot.child("UserName").getValue(String.class));
                    }


                    Log.e("mobilesize","h"+mobileArray.size());

                    adapter.notifyDataSetChanged();
                    justifyListViewHeightBasedOnChildren(listView);


                   Log.e("eee",""+String.valueOf(mobileArray)) ;

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
            pl_team.setVisibility(View.GONE);
            pl_invite.setVisibility(View.GONE);
        }





        send_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, inviteurl.getText().toString());
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");
                startActivity(sendIntent);
            }
        });




        ImageView imageView = (ImageView) findViewById(R.id.qrCode);
        try {
            JSONObject student1 = new JSONObject();
            try {
                student1.put("UserUID",UserUID);
                student1.put("EventKey", EventKey);
                Log.e("KeyM",""+Key);
                student1.put("AllotedKey", Key);
                student1.put("TicketKey", TicketKey);



            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String student1s=student1.toString();
            Toast.makeText(TicketActivity.this,""+student1s,Toast.LENGTH_SHORT).show();
            Log.e("KeyX",""+student1s);
            Bitmap bitmap = encodeAsBitmap(student1s);
            imageView.setImageBitmap(bitmap);
            /*try {
                JSONObject obj = new JSONObject(student1s);
                Toast.makeText(MainActivity.this,""+obj.getString("id")+obj.getString("id")+obj.getString("name"),Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                Log.e("error","Gotcha");
            }*/
        } catch (WriterException e) {
            Log.e("error2","Gotcha2");
        }
       /* try {
            Bitmap bitmap = encodeAsBitmap("KOMPTON");
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }*/
    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, HEIGHT, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            int color = Color.parseColor("#FF7F50");
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 320, 0, 0, w, h);
        return bitmap;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public static void justifyListViewHeightBasedOnChildren (ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }


    public class ListAdapterX extends BaseAdapter {

        Activity activity;
        ArrayList customListDataModelArrayList = new ArrayList<>();
        LayoutInflater layoutInflater = null;


        public ListAdapterX(Activity activity, ArrayList customListDataModelArray) {
            this.activity=activity;
            this.customListDataModelArrayList = customListDataModelArray;
            layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return customListDataModelArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return customListDataModelArrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        private  class ViewHolder{

            TextView tv_name;

        }
        ViewHolder viewHolder = null;


        // this method  is called each time for arraylist data size.
        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {

            View vi=view;
            final int pos = position;
            if(vi == null){

                // create  viewholder object for list_rowcell View.
                viewHolder = new ViewHolder();
                // inflate list_rowcell for each row
                vi = layoutInflater.inflate(R.layout.team_listview,null);

                viewHolder.tv_name = (TextView) vi.findViewById(R.id.text);

            /*We can use setTag() and getTag() to set and get custom objects as per our requirement.
            The setTag() method takes an argument of type Object, and getTag() returns an Object.*/
                vi.setTag(viewHolder);
            }else {

             /* We recycle a View that already exists */
                viewHolder= (ViewHolder) vi.getTag();
            }

           // viewHolder.image_view.setImageResource(customListDataModelArrayList.get(pos).getText());
            viewHolder.tv_name.setText(customListDataModelArrayList.get(pos).toString());
          //  viewHolder.tv_discription.setText(customListDataModelArrayList.get(pos).getImageDiscription());


            return vi;
        }



    }


    public void getInviteURL(){


        Log.e("EventKeyP",""+EventKey);


        String uri="http://hungryhunter96.github.io/inviteteam?Key="+Key+"&TicketKey="+TicketKey+"&EventKey="+EventKey+"&Sender="+user.getUid()+"&TeamName="+TeamNameX;

        Log.e("EventKeyY",""+uri);
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(uri))
                .setDomainUriPrefix("https://ddxr.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())

                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                           // progress_edit_delete.setVisibility(View.GONE);

                           // Toast.makeText(TicketActivity.this,"Link Copied",Toast.LENGTH_SHORT).show();
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();

                            inviteurl.setText(shortLink.toString());



                        } else {

                            Toast.makeText(TicketActivity.this,"Link Copy Failure",Toast.LENGTH_SHORT).show();

                        }
                    }
                });







    }


}
