package com.bluberry.album.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by blueberry on 2016/3/18.
 */
public class StretchImageView extends ImageView {
    private static final String TAG = "StetchImageView";

    public StretchImageView(Context context) {
        super(context);
    }

    public StretchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StretchImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int mode;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mode = 1;
//                return true ;
//                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mode += 1;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "mode = " + mode);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode -= 1;
                break;
            case MotionEvent.ACTION_UP:
                mode = 0;
                break;

        }
        return false;
    }
}
