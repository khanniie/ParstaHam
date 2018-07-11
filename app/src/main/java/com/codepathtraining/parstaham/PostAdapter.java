package com.codepathtraining.parstaham;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepathtraining.parstaham.Model.GlideApp;
import com.codepathtraining.parstaham.Model.Post;
import com.parse.ParseException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    //list of posts
    ArrayList<Post> posts;
    //context
    Context context;

    //initialize with list
    public PostAdapter(ArrayList<Post> posts){
        this.posts = posts;
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

        //determine the current orientation
        //boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        //build url for poster image
        String imageUrl = post.getImage().getUrl();

        //load image using glide
        GlideApp.with(context).load(imageUrl).into(holder.imgPost);
    }

    //returns the total number of items in the list
    @Override
    public int getItemCount() {
        return posts.size();

    }

    //create the viewholder as a static inner class
    public class ViewHolder extends RecyclerView.ViewHolder{

        //track view objects
        @BindView(R.id.img_post)ImageView imgPost;
        @BindView(R.id.tv_user) TextView tvUser;
        @BindView(R.id.tv_description) TextView tvDescription;

        public ViewHolder(View itemView){
            super(itemView);
            //lookup view objects by id
            ButterKnife.bind(this, itemView);
        }

//        @Override
//        public void onClick(View v) {
//            // gets item position
//            int position = getAdapterPosition();
//            // make sure the position is valid, i.e. actually exists in the view
//            if (position != RecyclerView.NO_POSITION) {
//                // get the Post at the position, this won't work if the class is static
//                Post Post = posts.get(position);
//                // create intent for the new activity
//                Intent intent = new Intent(context, PostDetailsActivity.class);
//                // serialize the Post using parceler, use its short name as a key
//                intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(Post));
//                intent.putExtra(Config.class.getSimpleName(), Parcels.wrap(config));
//                // show the activity
//                context.startActivity(intent);
//            }
//        }
    }
}