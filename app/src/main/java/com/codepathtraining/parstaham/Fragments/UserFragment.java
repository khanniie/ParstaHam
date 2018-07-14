package com.codepathtraining.parstaham.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepathtraining.parstaham.Adapters.GridPostAdapter;
import com.codepathtraining.parstaham.Models.ImageHelper;
import com.codepathtraining.parstaham.Models.Post;
import com.codepathtraining.parstaham.R;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {

    public interface OnFragmentInteractionListener {
        void OnUserFragmentLogOut();
        void OnUserFragmentOpenGallery();
        void OnPostDetailViewClick(Post post);
    }

    private ParseUser mUser;
    private Button btn_logOut;
    private Button btn_prof_pic;
    private TextView tv_username;
    private TextView tv_bio;
    private Context context;
    private View view;
    private ArrayList<Post> mPosts;
    private GridPostAdapter adapter;

    private OnFragmentInteractionListener mListener;

    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance(ParseUser user) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putParcelable("USER", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = getArguments().getParcelable("USER");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view = v;
        tv_username = view.findViewById(R.id.tv_username);
        tv_bio = view.findViewById(R.id.tv_bio);
        btn_logOut = view.findViewById(R.id.btn_logout);

        btn_prof_pic = view.findViewById(R.id.btn_prof_pic);

        if(mUser != ParseUser.getCurrentUser()){
            btn_logOut.setVisibility(View.INVISIBLE);
            btn_prof_pic.setVisibility(View.INVISIBLE);
        }
        try {
            tv_username.setText(mUser.fetchIfNeeded().getUsername());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        btn_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Logging out now... please wait.", Toast.LENGTH_LONG);
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        mListener.OnUserFragmentLogOut();
                    }
                });
            }
        });
        btn_prof_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.OnUserFragmentOpenGallery();
            }
        });
        ImageHelper.setProfileImage((ImageView)view.findViewById(R.id.img_profile), mUser, context);

        GridView gridview = (GridView) v.findViewById(R.id.gv_posts);
        mPosts = new ArrayList<>();
        adapter = new GridPostAdapter(context, mPosts);
        gridview.setAdapter(adapter);
        fillTimeLine();

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.OnPostDetailViewClick(mPosts.get(position));
            }
        });
    }

    @Override
    public void onAttach(Context c) {
        context = c;
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

    public void RelaunchAfterGallery(String path){
        Log.i("workig?","pz;");
        final ImageView prof_pic = view.findViewById(R.id.img_profile);
        File file = new File(path);
        ParseFile pf = new ParseFile(file);
        final ParseUser user = mUser;
        user.put("prof_pic", pf);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.i("DONE","prof pic uploaded");
                String imageUrl = user.getParseFile("prof_pic").getUrl();
                //ImageHelper.loadIntoImgGlide(context, imageUrl, (ImageView)view.findViewById(R.id.img_profile));
                ImageHelper.setProfileImage((ImageView)view.findViewById(R.id.img_profile), ParseUser.getCurrentUser(), context);
            }
        });

    }

    private void fillTimeLine(){

        ParseQuery<Post> pq = new ParseQuery(Post.class);
        pq.getQuery(Post.class).whereEqualTo("user", mUser).findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e == null){
                    mPosts.addAll(objects);
                    adapter.notifyDataSetChanged();
                } else{
                    e.printStackTrace();
                }
            }
        });
    }


}
