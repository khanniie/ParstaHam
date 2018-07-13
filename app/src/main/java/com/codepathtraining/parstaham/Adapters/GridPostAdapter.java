package com.codepathtraining.parstaham.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.codepathtraining.parstaham.Models.GridSquare;
import com.codepathtraining.parstaham.Models.ImageHelper;
import com.codepathtraining.parstaham.Models.Post;
import com.parse.ParseException;

import java.util.ArrayList;

public class GridPostAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Post> mPosts;

    public GridPostAdapter(Context c, ArrayList<Post> mp) {
        mContext = c;
        mPosts = mp;
    }

    public int getCount() {
        return mPosts.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        GridSquare imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new GridSquare(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(2, 2, 2, 2);
        } else {
            imageView = (GridSquare) convertView;
        }
        Post post = mPosts.get(position);
        try {
            ImageHelper.setImageIntoView(post.getImage().getFile(), imageView);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return imageView;
    }


}