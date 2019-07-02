package com.pd.config.pdonlineconfig.vies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoScrollViewPage extends ViewPager {
    private boolean noScroll =true;

    public NoScrollViewPage(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    public NoScrollViewPage(Context context) {
        super(context);
    }


    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }


    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }


    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (noScroll)
            return false;
        else
            return super.onTouchEvent(arg0);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (noScroll)
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }


    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }


    @Override


    public void setCurrentItem(int item) {


        super.setCurrentItem(item, false);//表示切换的时候，不需要切换时间。


    }


}
