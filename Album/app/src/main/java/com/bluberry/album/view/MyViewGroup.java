package com.bluberry.album.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by 易善祥 on 2016/3/24.
 */
public class MyViewGroup extends RelativeLayout{


    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewGroup(Context context) {
        super(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        Log.e("dispatchTouchEvent","dispatchTouchEvent");
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("onTouchEvent","onTouchEvent");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("ACTION_DOWN___Group", "ACTION_DOWN___Group"+event.getX());
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("ACTION_DOWN___Group", "ACTION_MOVE___Group"+event.getX());
                break;
            case MotionEvent.ACTION_UP:
                Log.e("ACTION_UP___Group", "ACTION_UP___Group");
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e("onInterceptTouchEvent", "onInterceptTouchEvent");
        return true;
    }
}
