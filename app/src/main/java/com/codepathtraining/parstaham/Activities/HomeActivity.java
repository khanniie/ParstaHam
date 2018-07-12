package com.codepathtraining.parstaham.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codepathtraining.parstaham.Models.Post;
import com.codepathtraining.parstaham.Adapters.PostAdapter;
import com.codepathtraining.parstaham.R;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    private ArrayList<Post> posts;
    private PostAdapter adapter;
    private RecyclerView rvPosts;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = this;

        rvPosts = findViewById(R.id.rvPosts);

        posts = new ArrayList<>();
        //initialize the adapter - movies array can't be reinitialized after
        adapter = new PostAdapter(posts);

        //resolve the recycler view and connect a layout manager and the adapter
        ButterKnife.bind(this);
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        rvPosts.setAdapter(adapter);

        findViewById(R.id.btn_makePost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePost();
            }
        });
        findViewById(R.id.btn_logOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Logging out now... please wait.", Toast.LENGTH_LONG);
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        Intent i = new Intent(context, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
            }
        });

        FillTimeLine();
    }

    private void FillTimeLine(){
        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser();

        postQuery.getQuery(Post.class).findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e == null){
                    for(int i = 0; i < objects.size(); i++){
                        try {
                            Log.d("HomeActivity", "Post[" + i + "] = " + objects.get(i).getDescription() +
                                    "  username = " + objects.get(i).getUser().fetchIfNeeded().getEmail());
                            Post post = new Post();
                            post.setUser(objects.get(i).getUser());
                            post.setDescription(objects.get(i).getDescription());
                            post.setImage(objects.get(i).getImage());

                            posts.add(post);

                            adapter.notifyItemInserted(posts.size() -1);

                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                else{
                    e.printStackTrace();
                }
            }
        });
    }

    private void makePost(){
        Intent i = new Intent(this, MakePostActivity.class);
        startActivity(i);
    }
}
