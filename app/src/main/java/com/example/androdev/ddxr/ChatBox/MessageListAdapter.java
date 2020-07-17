package com.example.androdev.ddxr.ChatBox;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androdev.ddxr.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asus on 6/3/2019.
 */

public  class MessageListAdapter extends RecyclerView.Adapter {

    /*OnTopReachedListener onTopReachedListener;*/

    private Context mContext;
    private ArrayList<BaseMessage> mMessageList;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private static final int VIEW_TYPE_MESSAGE_SENT2 = 3;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED2 = 4;



    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, date;
        View view;
        ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            date=(TextView)itemView.findViewById(R.id.date);
            view=(View)itemView.findViewById(R.id.view);

           // profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
        }

        void bind(BaseMessage message) {
            messageText.setText(message.getMessage());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getTime());
            if(date!=null){
                date.setText(message.getDate());

            }
            else{


            }

        //    nameText.setText("");


            // Insert the profile image from the URL into the ImageView.
            //  Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, date;


        SentMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            date=(TextView)itemView.findViewById(R.id.date);
       //     nameText = (TextView) itemView.findViewById(R.id.text_message_name);

        }

        void bind(BaseMessage message) {
            messageText.setText(message.getMessage());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getTime());
            if(date!=null){
                date.setText(message.getDate());
            }

            // nameText.setText(message.getSender().getNickname());

            // Insert the profile image from the URL into the ImageView.
            //  Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
        }
    }

    public MessageListAdapter(Context context, ArrayList<BaseMessage> messageList) {
        mContext = context;
        mMessageList = messageList;
    }




    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        }  if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }
         if (viewType == VIEW_TYPE_MESSAGE_SENT2) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent2, parent, false);
            return new SentMessageHolder(view);

        }
        if (viewType == VIEW_TYPE_MESSAGE_RECEIVED2) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received2, parent, false);
            return new ReceivedMessageHolder(view);

        }

        return null;
    }
    @NonNull
    @Override
    public int getItemViewType(int position) {
        BaseMessage message = (BaseMessage) mMessageList.get(position);

        if (message.getUser() != null && message.getUser().equals("Sending") && !message.getDate_change()) {
            // If the current user is the sender of the message

            return VIEW_TYPE_MESSAGE_SENT;


        }
        if (message.getUser() != null && message.getUser().equals("Receiving") && !message.getDate_change()) {


            return  VIEW_TYPE_MESSAGE_RECEIVED;
        }

        if(message.getUser() != null  && message.getDate_change()){

            if(message.getUser().equals("Receiving")){
                return  VIEW_TYPE_MESSAGE_RECEIVED2;
            }
            else{
                return VIEW_TYPE_MESSAGE_SENT2;
            }


        }

        return 0;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BaseMessage message = (BaseMessage) mMessageList.get(position);

/*
        if (position == 0){

            onTopReachedListener.onTopReached(position);

        }*/

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED2:
                ((ReceivedMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_SENT2:
                ((SentMessageHolder) holder).bind(message);

        }
    }
    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    /*public void setOnTopReachedListener(OnTopReachedListener onTopReachedListener){*/

      /*  this.onTopReachedListener = onTopReachedListener;*/

}




