package com.bluberry.album.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;


/**
 * Created by blueberry on 2016/3/18.
 */
public class ScrollViewGroup extends ViewGroup {

    private static final String TAG = "ScrollViewGroup";

    private int screenWidthPixes, screenHeightPixes;
    private int lastX, lastY, lastInterceptX, lastInterceptY;
    private int itemWidht, itemHeigth;
    private int mChildCount;
    private int padding = 0;
    private int mScrollDuration = 500;

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    public ScrollViewGroup(Context context) {
        super(context);
        init();
    }


    public ScrollViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScrollViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        screenHeightPixes = metrics.heightPixels;
        screenWidthPixes = metrics.widthPixels;
        mScroller = new Scroller(getContext());
        mVelocityTracker = VelocityTracker.obtain();
        setBackgroundColor(Color.BLACK);
        setPadding(0, 0, 0, 0);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mChildCount = getChildCount();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width, height;
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            width = screenWidthPixes;
            height = screenHeightPixes;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = screenWidthPixes;
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            width = widthSize;
            height = screenHeightPixes;
        } else {
            width = Math.min(widthSize, screenWidthPixes);
            height = Math.min(heightSize, screenHeightPixes);
        }
        setMeasuredDimension(width * mChildCount + padding * (mChildCount > 1 ? mChildCount - 1 : 0), height);

        Log.d(TAG, "itemWidth=" + itemWidht + " itemHeight=" + itemHeigth);
        //measure child
        for (int i = 0; i < mChildCount; i++) {
            View child = getChildAt(i);
            measureChild(child, MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST),
                    MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST));
        }

        itemHeigth = height;
        itemWidht = width;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = l;
        for (int i = 0; i < mChildCount; i++) {
            View child = getChildAt(i);
            child.layout(left, t, left + itemWidht, b);
            Log.d(TAG, "layout left= " + left);
            left += (itemWidht + padding);
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercept = false;
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercept = false;
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final int deltaX = x - lastInterceptX;
                final int deltaY = y - lastInterceptY;
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    intercept = true;
                } else {
                    intercept = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
        }
        lastInterceptX = x;
        lastInterceptY = y;
        return intercept;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int currentScrollX = getScrollX();
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                lastX = x ;
                lastY = y ;
                break;
            case MotionEvent.ACTION_MOVE:
                final int deltaX = x - lastX;
                int boundary = 100;//允超出的边界
                if (currentScrollX > -boundary && currentScrollX < ((mChildCount - 1) * (itemWidht + padding) + boundary)) {
                    scrollBy(-deltaX, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000, 8000);
                final float xVelocity = mVelocityTracker.getXVelocity();
                Log.d(TAG, "xVelocity: " + xVelocity);
                if (xVelocity > 50) {
                    //TODO fling
                }


                int curPosition = currentScrollX / (itemWidht + padding);
                int curOffsetX = currentScrollX - (curPosition * (itemWidht+padding));
                Log.i(TAG, "offset " + curOffsetX);
                if (curOffsetX > itemWidht / 2) {
                    curPosition++;
                }

                Log.d(TAG,"currentScrollX:"+currentScrollX);
                Log.d(TAG,"currentOffsetX:"+curOffsetX);
                Log.d(TAG,"currentPosition:"+curPosition);
                int dstScollX = Math.max(0, Math.min(mChildCount - 1, curPosition)) * (itemWidht + padding);
                smoothScrollTo(dstScollX, 0);
                break;
        }
        lastX = x;
        lastY = y;
        return false;
    }


    protected void smoothScrollTo(int x, int y) {
        int startX = getScrollX();
        int startY = getScrollY();
        int dx = x - startX;
        int dy = y - startY;
        mScroller.startScroll(startX, startY, dx, dy, mScrollDuration);
        postInvalidate();
    }

    protected void smoothScrollBy(int dx, int dy) {
        int startX = getScrollX();
        int startY = getScrollY();
        mScroller.startScroll(startX, startY, dx, dy, mScrollDuration);
        postInvalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int currentX = mScroller.getCurrX();
            int currentY = mScroller.getCurrY();
            scrollTo(currentX, currentY);
            postInvalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mVelocityTracker.recycle();
    }
}
