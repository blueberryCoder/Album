package com.bluberry.album.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by blueberry on 2016/3/17.
 */
public class SquareView extends FrameLayout{
    public SquareView(Context context) {
        super(context);
    }
    public SquareView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public SquareView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
