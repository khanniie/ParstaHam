package com.codepathtraining.parstaham.Models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestOptions;
import com.codepathtraining.parstaham.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageHelper {

    public static File getOutputMediaFile() {

        File mediaStorageDir = new File(
                Environment
                        .getExternalStorageDirectory(),
                "ParsaHam");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");
        return mediaFile;
    }

    public static void loadIntoImgGlide(Context context, String url, ImageView iv){
        GlideApp.with(context).load(url).apply(RequestOptions.circleCropTransform()).into(iv);
    }
    public static void setProfileImage(ImageView v, ParseUser user, Context context){
        String profUrl = null;
        try {
            ParseFile img_file = user.fetchIfNeeded().getParseFile("prof_pic");
            if(img_file == null){
                GlideApp.with(context).load(context.getDrawable(R.drawable.anon_user)).apply(RequestOptions.circleCropTransform()).into(v);
            } else {
                profUrl = img_file.getUrl();
                ImageHelper.loadIntoImgGlide(context, profUrl, v);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void setImageIntoView(File imgFile, ImageView view){
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
            //TODO fix this rotation thing
            matrix.postRotate(90);
            Bitmap rotatedBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true);
            view.setImageBitmap(rotatedBitmap);
        }
    }
}
