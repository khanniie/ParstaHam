package com.codepathtraining.parstaham.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepathtraining.parstaham.Models.GlideApp;
import com.codepathtraining.parstaham.Models.Post;
import com.codepathtraining.parstaham.R;
import com.parse.ParseException;

import java.text.SimpleDateFormat;
import java.util.Date;


public class PostDetailFragment extends Fragment {
    public interface OnFragmentInteractionListener {
        void onDetailFragmentInteraction();
    }

    private Post post;
    private ImageView img_post;
    private TextView tv_user;
    private TextView tv_description;
    private TextView tv_time;
    private SimpleDateFormat formatter;
    private Context context;

    private OnFragmentInteractionListener mListener;

    public PostDetailFragment() {
        // Required empty public constructor
    }

    public static PostDetailFragment newInstance(Post post) {
        PostDetailFragment fragment = new PostDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("POST", post);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            post = getArguments().getParcelable("POST");
        }
        String pattern = "MM/dd/Y";
        formatter = new SimpleDateFormat(pattern);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        img_post = view.findViewById(R.id.img_post);
        tv_user = view.findViewById(R.id.tv_user);
        tv_description = view.findViewById(R.id.tv_description);
        tv_time = view.findViewById(R.id.tv_time);

        try {
            tv_user.setText(post.getUser().fetchIfNeeded().getUsername());
            tv_description.setText(post.getDescription());
            Date d = post.getCreatedAt();
            //Toast.makeText(context, post.getCreatedAt().toString(), Toast.LENGTH_LONG).show();
            String formattedTime = formatter.format(post.getCreatedAt());
            tv_time.setText(formattedTime);
            String imageUrl = post.getImage().getUrl();
            GlideApp.with(context).load(imageUrl).centerCrop().into(img_post);

        } catch (ParseException e) {
            e.printStackTrace();
        }
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


}
