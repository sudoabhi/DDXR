package com.example.androdev.ddxr.SearchFragments;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androdev.ddxr.R;
import com.example.androdev.ddxr.UserProfilePackage.PeopleClick;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Asus on 3/10/2019.
 */

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder> {

    private List<SearchPeopleObject> posts;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
  public static class PeopleViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        CircleImageView ProfileImage;
        CardView ParentCardView;
        TextView UserName;
        TextView UserSkill;
        TextView UserCollege;


        ProgressBar progressBar;
        //  LikeButton likeButton;

        public PeopleViewHolder(View v) {
            super(v);

            UserName = (TextView)v.findViewById(R.id.user_name);
            UserCollege=(TextView) v.findViewById(R.id.user_college);
            UserSkill=( TextView) v.findViewById(R.id.user_skill);
            ProfileImage=(CircleImageView) v.findViewById(R.id.profile_image);
            ParentCardView=(CardView)v.findViewById(R.id.parent_card_view);


        }


    }

    // Provide a suitable constructor (depends on the kind of dataset)
    private Context context;
    public PeopleAdapter(Context context,List<SearchPeopleObject> posts) {
        this.context=context;
        this.posts=posts;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PeopleAdapter.PeopleViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                         int viewType) {
        // create a new view
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_people_model,parent,false);
        PeopleViewHolder vh = new PeopleViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull final PeopleViewHolder holder, int position) {
         final SearchPeopleObject c = posts.get(position);

        holder.UserName.setText(c.getUserName());

        holder.UserSkill.setText(c.getUserSkill());

        holder.ParentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, PeopleClick.class);
                Log.e("Sending value",""+c.getUserUID());
                intent.putExtra("UserUID",c.getUserUID());
                context.startActivity(intent);
            }
        });

        holder.UserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   Intent intent=new Intent
            }
        });
        //  Toast.makeText(getActivity(),""+posts.getNumberOfLikes(),Toast.LENGTH_SHORT).show();
        holder.UserCollege.setText(c.getUserCollege());
        //     holder.UserSkill.setText(posts.getUserSkill());




        Picasso.get()
                .load(c.getImageUrl())
                .error(android.R.drawable.stat_notify_error)
                .resize(400,400)
                .centerCrop()
                // .transform(transformation)
                .into(holder.ProfileImage, new Callback() {
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
