package com.example.androdev.ddxr.ChatBox;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.androdev.ddxr.R;
import com.example.androdev.ddxr.SearchFragments.PeopleAdapter;
import com.example.androdev.ddxr.SearchFragments.SearchPeopleObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Asus on 6/4/2019.
 */

public class HomeMessageAdapter extends RecyclerView.Adapter<HomeMessageAdapter.MessagesHolder> {

    private Context context;
    private ArrayList<HomeMessageObject> mMessageList;




    public class MessagesHolder extends RecyclerView.ViewHolder{

        TextView message_text;
        TextView other_party;
        CircleImageView image_view;
        TextView time;
        LinearLayout parent_layout;
        View view;

        public MessagesHolder(@NonNull View itemView) {
            super(itemView);

            image_view=(CircleImageView)itemView.findViewById(R.id.profile_image);
            other_party=(TextView)itemView.findViewById(R.id.other_party);
            message_text=(TextView)itemView.findViewById(R.id.recent_text);
            time=(TextView)itemView.findViewById(R.id.time);
            parent_layout=(LinearLayout)itemView.findViewById(R.id.parent_layout);
            view=itemView.findViewById(R.id.view);
        }


    }

    public HomeMessageAdapter(Context context,ArrayList<HomeMessageObject> mMessageList){

        this.context=context;
        this.mMessageList=mMessageList;
    }


    @Override
    public HomeMessageAdapter.MessagesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_messages_list,viewGroup,false);
       HomeMessageAdapter.MessagesHolder vh = new HomeMessageAdapter.MessagesHolder(v);
        return vh;

    }




    @Override
    public void onBindViewHolder(final HomeMessageAdapter.MessagesHolder viewHolder, int i) {

        final HomeMessageObject c = mMessageList.get(i);
        Picasso.get()
                .load(c.getImage_url())
                .into(viewHolder.image_view);
        viewHolder.other_party.setText(c.getSender_name());
        viewHolder.message_text.setText(c.recent_text);
        viewHolder.time.setText(c.getTime());

        if(i==0){
            viewHolder.view.setVisibility(View.INVISIBLE);
        }

        viewHolder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context,FirstActivity.class);
                intent.putExtra("ReceiverName",c.getSender_name());
                intent.putExtra("ReceiverUID",c.getOther_party());
                intent.putExtra("ReceiverImageUrl",c.getImage_url());
                intent.putExtra("SenderProfileImage",c.getSender_image_url());
                intent.putExtra("SenderName",c.getReceiver_name());
                intent.putExtra("ActivityName","HomeMessage");

              /*  Log.e("ReceiverName",c.getSender_name());
                Log.e("ReceiverUID",c.getOther_party());
                Log.e("ReceiverImageUrl",c.getImage_url());
                Log.e("SenderProfileImage",c.getSender_image_url());
                Log.e("SenderName",c.getReceiver_name());
                Log.e("ActivityName","HomeMessage");*/
                context.startActivity(intent);
                ((HomeMessage)context).finish();
            }
        });


    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}
