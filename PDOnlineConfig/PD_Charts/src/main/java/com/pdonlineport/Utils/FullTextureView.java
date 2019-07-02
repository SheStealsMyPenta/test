package com.pdonlineport.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;

/**
 * Created by SONG on 2019/5/5 17:07.
 * The final explanation right belongs to author
 */
public class FullTextureView extends TextureView {
    public FullTextureView(Context context) {
        super(context);
    }

    public FullTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FullTextureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultSize(0,widthMeasureSpec);
        int height = getDefaultSize(0,heightMeasureSpec);
        setMeasuredDimension(width,height);
    }
}
