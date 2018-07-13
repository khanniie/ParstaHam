package com.codepathtraining.parstaham.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codepathtraining.parstaham.Models.ImageHelper;
import com.codepathtraining.parstaham.Models.Post;
import com.codepathtraining.parstaham.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

public class MakePostFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private FrameLayout frame;
    private TextView tv_notify;
    private ImageView iv_post;
    private EditText description_et;
    private Context context;
    private ImageButton btn_camera;
    private ImageButton btn_gallery;
    private Button btn_post;

    private ParseFile pf;
    private ProgressBar pb;

    public MakePostFragment() {
    }

    public static MakePostFragment newInstance() {
        MakePostFragment fragment = new MakePostFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_make_post, container, false);
        frame = v.findViewById(R.id.img_frame);
        tv_notify = v.findViewById(R.id.tv_notify);
        iv_post = v.findViewById(R.id.iv_post);
        description_et = v.findViewById(R.id.description_et);
        btn_camera = v.findViewById(R.id.btn_camera);
        btn_gallery = v.findViewById(R.id.btn_gallery);
        btn_post = v.findViewById(R.id.btn_post);

        pb = (ProgressBar) v.findViewById(R.id.pbLoading);

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onMakeFragmentOpenCamera();
                }
            }
        });
        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onMakeFragmentOpenGallery();
                }
            }
        });
        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pf == null){
                    Toast.makeText(context, "Take a picture first!", Toast.LENGTH_LONG).show();
                    return;
                }
                pb.setVisibility(ProgressBar.VISIBLE);
                Post post = new Post();
                post.setDescription(description_et.getText().toString());
                post.setImage(pf);
                post.setUser(ParseUser.getCurrentUser());
                post.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.i("POST","DONE!");
                    }
                });
                pb.setVisibility(ProgressBar.INVISIBLE);
                mListener.onMakeFragmentAfterPost();
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context c) {
        super.onAttach(c);
        context = c;
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
        void onMakeFragmentAfterPost();
        void onMakeFragmentOpenCamera();
        void onMakeFragmentOpenGallery();
    }

    public void RelaunchAfterCamera(String path) {
        Log.i("path for image is", path);
        File file = new File(path);
        ImageHelper.setImageIntoView(file, iv_post);
        pf = new ParseFile(file);
    }





}
