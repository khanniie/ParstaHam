package com.codepathtraining.parstaham;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepathtraining.parstaham.Model.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;

public class MakePostActivity extends AppCompatActivity {

    private FrameLayout frame;
    private TextView tv_notify;
    private ImageView iv_post;
    private EditText description_et;
    private Context context;
    private Button btn_post;

    private ParseFile pf;

    private static final Integer REQUEST_CODE = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_post);

        frame = findViewById(R.id.img_frame);
        tv_notify = findViewById(R.id.tv_notify);
        iv_post = findViewById(R.id.iv_post);
        description_et = findViewById(R.id.description_et);
        btn_post = findViewById(R.id.btn_post);
        context = this;

        frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, CameraActivity.class);
                startActivityForResult(i, REQUEST_CODE);
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
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("returned", requestCode + "");
        if(requestCode == REQUEST_CODE){
            String path = data.getStringExtra("PATH");
            Log.i("path for image is", path);
            File file = new File(path);

            setImageIntoView(file, iv_post);

            pf = new ParseFile(file);
        }
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

    //https://stackoverflow.com/questions/7131930/get-the-file-size-in-android-sdk
    public static String getFolderSizeLabel(File file) {
        long size = getFolderSize(file) / 1024; // Get size and convert bytes into Kb.
        if (size >= 1024) {
            return (size / 1024) + " Mb";
        } else {
            return size + " Kb";
        }
    }

    public static long getFolderSize(File file) {
        long size = 0;
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                size += getFolderSize(child);
            }
        } else {
            size = file.length();
        }
        return size;
    }

}
//TODO - check for size and resize if applicable
//https://stackoverflow.com/questions/4837715/how-to-resize-a-bitmap-in-android
//https://stackoverflow.com/questions/18573774/how-to-reduce-an-image-file-size-before-uploading-to-a-server
