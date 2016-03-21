package com.bluberry.album.view;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

/**
 * Created by blueberry on 2016/3/18.
 */
public class StretchImageView extends ImageView {

    private static final String TAG = "StetchImageView";

    private int mode;
    private float baseValue;

    private ScaleGestureDetector scaleGestureDetector;

    public StretchImageView(Context context) {
        super(context);
        init();
    }

    public StretchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StretchImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private void init() {
        setScaleType(ScaleType.MATRIX);

          scaleGestureDetector = new ScaleGestureDetector(this.getContext(), new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float focusX = detector.getFocusX() ;
                float focusY =detector.getFocusY();
                float scaleFactor =detector.getScaleFactor() ;
                Matrix matrix = new Matrix();
                matrix.postScale(scaleFactor ,scaleFactor,focusX,focusY);
                setImageMatrix(matrix);
                Log.i(TAG,"scaleFactor:"+scaleFactor+" foucusX: "+focusX +"focusY: "+focusY);
//                imageScale(scaleFactor);
                return true;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {

            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return scaleGestureDetector.onTouchEvent(event);
//        switch (event.getAction() & MotionEvent.ACTION_MASK) {
//            case MotionEvent.ACTION_DOWN:
//                mode = 1;
//                baseValue = 0;
//                Log.i(TAG, "down--> mode=" + mode);
//                return true;
////                break;
//            case MotionEvent.ACTION_POINTER_DOWN:
//                mode += 1;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Log.i(TAG, " move--> mode = " + mode);
//                if (event.getPointerCount() == 2) {
//                    float x = event.getX(0) - event.getX(1);
//                    float y = event.getY(0) - event.getY(1);
//                    float value = (float) Math.sqrt(x * x + y * y);
//                    if (baseValue == 0) {
//                        baseValue = value;
//                    } else if (baseValue - value > 10 || baseValue - value < -10) {
//                        float scale = value / baseValue;
//                        imageScale(scale);
//                    }
//
//
//                }
//                break;
//            case MotionEvent.ACTION_POINTER_UP:
//                Log.i(TAG, "pointer up-->  mode=" + mode);
//                mode -= 1;
//                break;
//            case MotionEvent.ACTION_UP:
//                Log.i(TAG, "action up-->  mode=" + mode);
//                mode = 0;
//                break;
//
//        }
//        return true;
    }


    private void imageScale(float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale,scale);
        setImageMatrix(matrix);
        Log.i(TAG, "image scale.");
//        setImageMatrix();
    }
}
