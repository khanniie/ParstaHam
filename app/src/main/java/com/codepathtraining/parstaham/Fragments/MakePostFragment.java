package com.codepathtraining.parstaham.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepathtraining.parstaham.Models.Post;
import com.codepathtraining.parstaham.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;

public class MakePostFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private FrameLayout frame;
    private TextView tv_notify;
    private ImageView iv_post;
    private EditText description_et;
    private Context context;
    private Button btn_post;

    private ParseFile pf;

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
        btn_post = v.findViewById(R.id.btn_post);

        frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO - call activity and tell it to open camera
                if (mListener != null) {
                    mListener.onMakeFragmentOpenCamera();
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
                //TODO - go back to home page
                mListener.onMakeFragmentAfterPost();
            }
        });

        return v;
    }

    public void onButtonPressed(Uri uri) {

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
    }

    public void RelaunchAfterCamera(String path) {
        Log.i("path for image is", path);
        File file = new File(path);
        setImageIntoView(file, iv_post);
        pf = new ParseFile(file);
    }


    private void setImageIntoView(File imgFile, ImageView view){
        if(imgFile.exists()){
            int rotate = 0;
            try {
                ExifInterface exif = new ExifInterface(imgFile.getAbsolutePath());
                int orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);

                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotate = 270;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotate = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotate = 90;
                        break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap rotatedBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true);
            view.setImageBitmap(rotatedBitmap);
        }
    }


}
