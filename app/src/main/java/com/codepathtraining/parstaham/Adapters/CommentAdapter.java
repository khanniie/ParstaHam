package com.codepathtraining.parstaham.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepathtraining.parstaham.Fragments.CommentFragment;
import com.codepathtraining.parstaham.Models.Comment;
import com.codepathtraining.parstaham.R;
import com.parse.ParseException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    // list of posts
    ArrayList<Comment> mComments;
    // context
    Context context;
    CommentFragment.OnFragmentInteractionListener mListener;

    // initialize with list
    public CommentAdapter(ArrayList<Comment> comments) {
        this.mComments = comments;
    }

    // creates and inflates a new view
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.item_comment, viewGroup, false);
        return new ViewHolder(postView);
    }

    // binds an inflated view to a new item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        // get the Post data at the specified position
        Comment comment = mComments.get(i);
        holder.tv_comment.setText(comment.getCommentText());
        try {
            holder.tv_username.setText(comment.getOwner().fetchIfNeeded().getUsername());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // returns the total number of items in the list
    @Override
    public int getItemCount() {
        return mComments.size();
    }

    // create the viewholder as a static inner class
    public class ViewHolder extends RecyclerView.ViewHolder {

        // track view objects
        @BindView(R.id.tv_user)
        TextView tv_username;

        @BindView(R.id.tv_com)
        TextView tv_comment;


        public ViewHolder(View itemView) {
            super(itemView);
            // lookup view objects by id
            ButterKnife.bind(this, itemView);
        }
    }
}
