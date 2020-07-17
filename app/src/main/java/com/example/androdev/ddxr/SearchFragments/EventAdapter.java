package com.example.androdev.ddxr.SearchFragments;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androdev.ddxr.EventDetails.EventDetail;
import com.example.androdev.ddxr.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Asus on 3/17/2019.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.CollegeViewHolder> {

    private List<SearchEventsObject> posts;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class CollegeViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        CircleImageView ClubImageUrl;
        TextView ClubName;
        TextView CollegeName;
        TextView ClubFollowers;
        CardView parent_view;
        LinearLayout parent_view2;
        LinearLayout parent_view3;


        ProgressBar progressBar;
        //  LikeButton likeButton;

        public CollegeViewHolder(View v) {
            super(v);

            ClubName = (TextView)v.findViewById(R.id.club_name);
            CollegeName=(TextView) v.findViewById(R.id.college_name);
            ClubFollowers=( TextView) v.findViewById(R.id.follower_numbers);
            ClubImageUrl=(CircleImageView) v.findViewById(R.id.club_profile_image);
            parent_view=v.findViewById(R.id.parent_view);
            parent_view2=v.findViewById(R.id.parent_view2);
            parent_view3=v.findViewById(R.id.parent_view3);


        }


    }

    // Provide a suitable constructor (depends on the kind of dataset)
    private Context context;
    public EventAdapter(Context context, List<SearchEventsObject> posts) {
        this.context=context;
        this.posts=posts;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CollegeViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_organizer_model,parent,false);
        CollegeViewHolder vh = new CollegeViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull final CollegeViewHolder holder, final int position) {
        final SearchEventsObject c = posts.get(position);

        holder.ClubName.setText(c.getEventName());
        holder.ClubName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, EventDetail.class);
                intent.putExtra("Key",c.getEventKey());
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });
        holder.parent_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, EventDetail.class);
                intent.putExtra("Key",c.getEventKey());
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });
        holder.parent_view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, EventDetail.class);
                intent.putExtra("Key",c.getEventKey());
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });
        holder.parent_view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, EventDetail.class);
                intent.putExtra("Key",c.getEventKey());
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });




        //  Toast.makeText(getActivity(),""+posts.getNumberOfLikes(),Toast.LENGTH_SHORT).show();
        holder.CollegeName.setText(c.getCollegeName());
        //     holder.UserSkill.setText(posts.getUserSkill());




        Picasso.get()
                .load(c.getEventImageUrl())
                .error(android.R.drawable.stat_notify_error)
                .resize(400,400)
                .centerCrop()
                // .transform(transformation)
                .into(holder.ClubImageUrl, new Callback() {
                    @Override
                    public void onSuccess() {




                    }

                    @Override
                    public void onError(Exception e) {

                        Toast.makeText(context, "Failed to load new posts", Toast.LENGTH_SHORT).show();

                    }


                });







    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return posts.size();
    }

    public  int getCount(){

        return  posts.size();

    }
}
