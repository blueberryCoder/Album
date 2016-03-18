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

    private static final String TAG="ScrollViewGroup";

    private int screenWidthPixes, screenHeightPixes;
    private int lastX, lastY, lastInterceptX, lastInterceptY;
    private int itemWidht, itemHeith;
    private int mChildCount;
    private int padding = 0;
    private int mScrollDuration = 500;

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker ;

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
        mVelocityTracker = VelocityTracker.obtain() ;
        setBackgroundColor(Color.BLACK);
        setPadding(0,0,0,0);
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

        Log.d(TAG,"itemWidth="+itemWidht+" itemHeight="+itemHeith);
        //measure child
        for (int i = 0; i < mChildCount; i++) {
            View child = getChildAt(i);
            measureChild(child, MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST),
                    MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST));
        }

        itemHeith = height;
        itemWidht = width;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = l;
        for (int i = 0; i < mChildCount; i++) {
            View child = getChildAt(i);
            child.layout(left, t, left + itemWidht, b);
            Log.d(TAG,"layout left= "+left);
            left += (itemWidht+padding);
        }
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        boolean intercept = false;
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercept = false;
                if (mScroller != null) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final int delatX = x - lastInterceptX;
                final int dealtY = y - lastInterceptY;
                if (Math.abs(delatX) < Math.abs(dealtY)) {
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

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mScroller != null) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final int delatX = x - lastX;
                scrollBy(-delatX, 0);
                break;
            case MotionEvent.ACTION_UP:
                //获得当前的 item 位置
                Log.d(TAG,"当前srcollX:"+currentScrollX);
                int curPosition = currentScrollX / (itemWidht + padding);
                Log.d(TAG,"currentPosition:"+curPosition);
                // 当前的偏移
                int curOffsetX = currentScrollX - (curPosition * itemWidht) - (curPosition * padding);
                Log.d(TAG,"current offset :"+curOffsetX);
                if (Math.abs(curOffsetX) > itemWidht / 3 && curOffsetX>0) {
                    curPosition++;
                } else if(Math.abs(curOffsetX)>itemWidht/3 && curOffsetX<0){
                    curPosition--;
                }
                Log.d(TAG,"current position: "+curPosition);
                Log.d(TAG, "srcoll to X: " + curPosition * (itemWidht + padding));
                 int  dstScollX  =Math.max(0,Math.min(mChildCount-1,curPosition))*(itemWidht+padding) ;
                smoothScrollTo(dstScollX, 0);
                break;
        }
        lastX = x;
        lastY = y;
        return true;
    }

    protected  void smoothScrollTo(int x, int y){
        int startX = getScrollX() ;
        int startY = getScrollY();
        int dx = x-startX ;
        int dy = y-startY ;
        mScroller.startScroll(startX,startY,dx,dy,mScrollDuration);
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
}
