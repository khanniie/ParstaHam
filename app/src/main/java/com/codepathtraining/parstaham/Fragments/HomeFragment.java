package com.codepathtraining.parstaham.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepathtraining.parstaham.Adapters.PostAdapter;
import com.codepathtraining.parstaham.Models.Post;
import com.codepathtraining.parstaham.R;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private ArrayList<Post> posts;
    private PostAdapter adapter;
    private RecyclerView rvPosts;
    private Context context;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("HOME_F", "Home fragment created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rvPosts = view.findViewById(R.id.rvPosts);
        posts = new ArrayList<>();
        adapter = new PostAdapter(posts);
        rvPosts.setLayoutManager(new LinearLayoutManager(context));
        rvPosts.setAdapter(adapter);
        FillTimeLine();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onHomeFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onHomeFragmentInteraction(Uri uri);
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
}
