package com.codepathtraining.parstaham.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepathtraining.parstaham.Fragments.HomeFragment;
import com.codepathtraining.parstaham.Models.Comment;
import com.codepathtraining.parstaham.Models.GlideApp;
import com.codepathtraining.parstaham.Models.ImageHelper;
import com.codepathtraining.parstaham.Models.Like;
import com.codepathtraining.parstaham.Models.Post;
import com.codepathtraining.parstaham.R;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
  // list of posts
  ArrayList<Post> posts;
  // context
  Context context;
  private SimpleDateFormat formatter;
  HomeFragment.OnFragmentInteractionListener mListener;

  // initialize with list
  public PostAdapter(ArrayList<Post> posts, HomeFragment.OnFragmentInteractionListener listener) {
    this.posts = posts;
    this.mListener = listener;
    String pattern = "MM/dd/Y";
    this.formatter = new SimpleDateFormat(pattern);
  }

  // creates and inflates a new view
  @Override
  @NonNull
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    // get context and create the inflater
    context = viewGroup.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);
    // create the view using the item_Post layout
    // return a new ViewHolder
    View postView = inflater.inflate(R.layout.item_post, viewGroup, false);
    return new ViewHolder(postView);
  }

  // binds an inflated view to a new item
  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
    // get the Post data at the specified position
    Post post = posts.get(i);
    // populate the view with the Post data
    holder.tvDescription.setText(post.getDescription());
    try {
      holder.tvUser.setText(post.getUser().fetchIfNeeded().getUsername());
    } catch (ParseException e) {
      e.printStackTrace();
    }

    Date d = post.getCreatedAt();
    String formattedTime = formatter.format(post.getCreatedAt());
    holder.tvTime.setText(formattedTime);

    // build url for poster image
    String imageUrl = post.getImage().getUrl();

    // load image using glide
    GlideApp.with(context).load(imageUrl).centerCrop().into(holder.imgPost);

    ImageHelper.setProfileImage(holder.imgProfile, post.getUser(), context);
    final ViewHolder new_holder = holder;


    ParseQuery<Comment> cquery = ParseQuery.getQuery(Comment.class);

    cquery.getQuery(Comment.class).whereEqualTo("post", post).findInBackground(
          new FindCallback<Comment>() {
              @Override
              public void done(List<Comment> objects, ParseException e) {
                  new_holder.tv_commentCount.setText(objects.size() + " comments");
              }
          });

    ParseQuery<Like> query = ParseQuery.getQuery(Like.class);

    query.getQuery(Like.class).whereEqualTo("post", post).findInBackground(
            new FindCallback<Like>() {
              @Override
              public void done(List<Like> objects, ParseException e) {
                new_holder.tv_likeCount.setText(objects.size() + " likes");
              }
            });

    query.getQuery(Like.class).whereEqualTo("owner", ParseUser.getCurrentUser()).whereEqualTo("post", post)
        .findInBackground(
            new FindCallback<Like>() {
              @Override
              public void done(List<Like> objects, ParseException e) {
                if (e == null) {
                  // if not liked yet
                  if (objects == null || objects.size() < 1) {
                    new_holder.btnLike.setImageResource(R.drawable.ufi_heart);
                  } else {
                    new_holder.btnLike.setImageResource(R.drawable.ufi_heart_active);
                  }
                } else {
                  e.printStackTrace();
                }
              }
            });
  }

  // returns the total number of items in the list
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

  // create the viewholder as a static inner class
  public class ViewHolder extends RecyclerView.ViewHolder {

    // track view objects
    @BindView(R.id.img_post)
    ImageView imgPost;

    @BindView(R.id.tv_user)
    TextView tvUser;

    @BindView(R.id.tv_description)
    TextView tvDescription;

    @BindView(R.id.tvTime)
    TextView tvTime;

    @BindView(R.id.iv_profile)
    ImageView imgProfile;

    @BindView(R.id.btn_like)
    ImageButton btnLike;

    @BindView(R.id.btn_comment)
    ImageButton btnComment;

    @BindView(R.id.tv_likeCount)
    TextView tv_likeCount;

    @BindView(R.id.tv_commentCount)
      TextView tv_commentCount;

      public ViewHolder(View itemView) {
      super(itemView);
      // lookup view objects by id
      ButterKnife.bind(this, itemView);
      imgPost.setOnClickListener(
          new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              mListener.onHomeFragmentInteraction(posts.get(getAdapterPosition()));
            }
          });
      imgProfile.setOnClickListener(
          new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              mListener.onUserProfClicked(posts.get(getAdapterPosition()).getUser());
            }
          });
      btnLike.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

              ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
              query.getQuery(Like.class)
                  .whereEqualTo("owner", ParseUser.getCurrentUser())
                  .whereEqualTo("post", posts.get(getAdapterPosition()))
                  .findInBackground(
                      new FindCallback<Like>() {
                        @Override
                        public void done(List<Like> objects, ParseException e) {
                          if (e == null) {
                            // if not liked yet
                            if (objects == null || objects.size() < 1) {
                              Like like = new Like();
                              like.setOwner(ParseUser.getCurrentUser());
                              like.setPost(posts.get(getAdapterPosition()));
                              like.saveInBackground(
                                  new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                      if (e == null) {
                                        String text = tv_likeCount.getText().toString();
                                        int count = Integer.parseInt(text.substring(0, text.length() - 6)) + 1;
                                        tv_likeCount.setText(count + " likes");
                                        btnLike.setImageResource(R.drawable.ufi_heart_active);
                                      } else {
                                        e.printStackTrace();
                                      }
                                    }
                                  });
                            } else {
                              objects.get(0).deleteInBackground(
                                      new DeleteCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                          if (e == null) {
                                            String text = tv_likeCount.getText().toString();
                                            int count = Integer.parseInt(text.substring(0, text.length() - 6)) - 1;
                                            tv_likeCount.setText(count + " likes");
                                            btnLike.setImageResource(R.drawable.ufi_heart);
                                          } else {
                                            e.printStackTrace();
                                          }
                                        }
                                      });
                            }
                          } else {
                            e.printStackTrace();
                          }
                        }
                      });
            }
          });
      btnComment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mListener.onOpenCommentPage(posts.get(getAdapterPosition()), getAdapterPosition());
            }
          });
    }
  }
}
