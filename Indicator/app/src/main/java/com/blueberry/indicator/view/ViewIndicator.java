package com.blueberry.indicator.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blueberry.indicator.R;

import java.util.List;

/**
 * Created by blueberry on 2016/4/23.
 * 一个ViewPager 指示器
 */
public class ViewIndicator extends LinearLayout {

    private static final String TAG = "ViewIndicator";

    /**
     * 自定义属性
     */
    private int textColor = Color.BLACK;
    private int iconColor = Color.BLACK ;
    private float textSize = 14;

    /*可见的数量*/
    private int numberOfVisible = 5;
    private int tabWidth;
    private float triangleWidth;
    private float triangleHeight;
    private float ratioOfTabWidth = 1 / 6.0f;
    /*三角形开始时的位置*/
    private float triangleInitOffest;
    private float triangleOffset;
    /*画笔*/
    private Paint mPaint;
    /*三角形的path*/
    private Path mPath;

    /*ViewPage*/
    private ViewPager mViewPager;
    /*自定义监听器*/
    private OnTabSlelectedListener mOnTabSelectedListener ;
    /*PageChangeListener*/
    private PageChangleListener mPageChangeListener = new PageChangleListener();
    /*需要显示的数据*/
    private List<String> titles;


    public ViewIndicator(Context context) {
        super(context);
        init(context,null);
    }

    public ViewIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public ViewIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }
    private void init(Context context,AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(3));
        mPaint.setColor(iconColor);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ViewIndicator);
        textColor = typedArray.getColor(R.styleable.ViewIndicator_textColor,Color.BLACK);
        iconColor = typedArray.getColor(R.styleable.ViewIndicator_iconColor,Color.BLACK);

        textSize = typedArray.getDimensionPixelSize(R.styleable.ViewIndicator_textSize,14);
        numberOfVisible = typedArray.getInteger(R.styleable.ViewIndicator_numofvisiable,3);
        typedArray.recycle();

    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    /**
     * 设置viewPage
     * @return
     */
    public void setViewPager(ViewPager mViewPager) {
        this.mViewPager = mViewPager;
    }

    /**
     * 设置监听器
     * @param l
     */
    public void setOnTabSelectedListener(OnTabSlelectedListener l){
        this.mOnTabSelectedListener= l ;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        tabWidth = w / numberOfVisible;
        crateTriangle();
        if (titles == null) {
            return;
        }
        for (int i=0;i<titles.size();i++) {
            TextView textView =addTextView(titles.get(i));
            final int finali = i ;
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(finali);
                }
            });
        }
        if(mViewPager!=null &&mPageChangeListener!=null){
            mPageChangeListener = new PageChangleListener() ;
            mViewPager.addOnPageChangeListener(mPageChangeListener);
        }
    }

    /**
     * 根据tabWidth 计算三角形属性
     */
    private void crateTriangle() {
        triangleWidth = tabWidth * ratioOfTabWidth;
        triangleHeight = (float) (triangleWidth / 2 *Math.tan(Math.toRadians(30)));
        triangleInitOffest = tabWidth / 2 - triangleWidth / 2;
        mPath = new Path();
        mPath.lineTo(triangleWidth, 0);
        mPath.lineTo(triangleWidth / 2, -triangleHeight);
        mPath.close();
    }


    /**
     * 设置tabs
     *
     * @param text
     * @return
     */
    private TextView addTextView(String text) {
        TextView tempTextView = new TextView(getContext());
        LayoutParams params = new LayoutParams(tabWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        tempTextView.setLayoutParams(params);
        tempTextView.setText(text);
        tempTextView.setGravity(Gravity.CENTER);
        tempTextView.setTextSize( TypedValue.COMPLEX_UNIT_SP,textSize);
        tempTextView.setTextColor(textColor);
        addView(tempTextView);
      return tempTextView ;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.save();
        canvas.translate(triangleInitOffest + triangleOffset, getHeight());
        mPaint.setColor(iconColor);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
    }

    /**
     * 滑动
     *
     * @param position
     * @param positionOffset
     */
    public void scroll(int position, float positionOffset) {
        triangleOffset = (position + positionOffset) * tabWidth;

        /*如果超出了边界，则应滑动本身*/
        if (position >= numberOfVisible - 2
                && numberOfVisible < getChildCount()
                && position < getChildCount() - 2) {
            if (numberOfVisible != 1) {
                scrollTo(((position - (numberOfVisible - 2)) * tabWidth) + (int) (positionOffset * tabWidth), 0);
            } else {
                scrollTo((int) (position * tabWidth + positionOffset * tabWidth), 0);
            }
        }
        invalidate();
    }

    /**
     * 自定义监听器
     */
    public  interface OnTabSlelectedListener {
         void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) ;
         void onPageSelected(int position) ;
         void onPageScrollStateChanged(int state) ;
    }

    /**
     * ViewPage 的 OnPageChangeListener
     */
    private  class PageChangleListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            scroll(position,positionOffset);
            if(mOnTabSelectedListener!=null){
                mOnTabSelectedListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (mOnTabSelectedListener != null) {
                mOnTabSelectedListener.onPageSelected(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mOnTabSelectedListener != null) {
                mOnTabSelectedListener.onPageScrollStateChanged(state);
            }
        }
    }
}
