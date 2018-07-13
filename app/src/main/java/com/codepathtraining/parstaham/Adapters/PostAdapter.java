package com.codepathtraining.parstaham.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepathtraining.parstaham.Fragments.HomeFragment;
import com.codepathtraining.parstaham.Models.GlideApp;
import com.codepathtraining.parstaham.Models.ImageHelper;
import com.codepathtraining.parstaham.Models.Post;
import com.codepathtraining.parstaham.R;
import com.parse.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    //list of posts
    ArrayList<Post> posts;
    //context
    Context context;
    private SimpleDateFormat formatter;
    HomeFragment.OnFragmentInteractionListener mListener;

    //initialize with list
    public PostAdapter(ArrayList<Post> posts, HomeFragment.OnFragmentInteractionListener listener){
        this.posts = posts;
        this.mListener = listener;
        String pattern = "MM/dd/Y";
        this.formatter = new SimpleDateFormat(pattern);
    }

    //creates and inflates a new view
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //get context and create the inflater
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //create the view using the item_Post layout
        //return a new ViewHolder
        View postView = inflater.inflate(R.layout.item_post, viewGroup, false);
        return new ViewHolder(postView);
    }

    //binds an inflated view to a new item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        //get the Post data at the specified position
        Post post = posts.get(i);
        //populate the view with the Post data
        holder.tvDescription.setText(post.getDescription());
        try {
            holder.tvUser.setText(post.getUser().fetchIfNeeded().getUsername());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date d = post.getCreatedAt();
        String formattedTime = formatter.format(post.getCreatedAt());
        holder.tvTime.setText(formattedTime);

        //build url for poster image
        String imageUrl = post.getImage().getUrl();

        //load image using glide
        GlideApp.with(context).load(imageUrl).centerCrop().into(holder.imgPost);

        ImageHelper.setProfileImage(holder.imgProfile, post.getUser(), context);
    }

    //returns the total number of items in the list
    @Override
    public int getItemCount() {
        return posts.size();

    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    //create the viewholder as a static inner class
    public class ViewHolder extends RecyclerView.ViewHolder{

        //track view objects
        @BindView(R.id.img_post)ImageView imgPost;
        @BindView(R.id.tv_user) TextView tvUser;
        @BindView(R.id.tv_description) TextView tvDescription;
        @BindView(R.id.tvTime) TextView tvTime;
        @BindView(R.id.iv_profile) ImageView imgProfile;


        public ViewHolder(View itemView){
            super(itemView);
            //lookup view objects by id
            ButterKnife.bind(this, itemView);
            imgPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onHomeFragmentInteraction(posts.get(getAdapterPosition()));
                }
            });
            imgProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onUserProfClicked(posts.get(getAdapterPosition()).getUser());
                }
            });
        }
    }
}
