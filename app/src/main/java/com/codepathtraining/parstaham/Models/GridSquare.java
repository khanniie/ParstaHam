package com.codepathtraining.parstaham.Models;

import android.content.Context;
import android.util.AttributeSet;

public class GridSquare extends android.support.v7.widget.AppCompatImageView{

    public GridSquare(Context context) {
        super(context);
    }

    public GridSquare(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridSquare(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec); // This is the key that will make the height equivalent to its width
    }
}