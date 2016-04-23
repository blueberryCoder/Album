package com.blueberry.indicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;

import com.blueberry.indicator.view.ViewIndicator;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    /*ViewPager*/
    private ViewPager mViewPager;
    private ViewIndicator mViewIndicator;

    String[] datas = {"数据", "模型", "页面"
            ,"控制器","监听器","拦截器"
            ,"控制器","监听器","拦截器"
            ,"控制器","监听器","拦截器"
            ,"控制器","监听器","拦截器"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewIndicator = (ViewIndicator) findViewById(R.id.view_indicator);
        mViewIndicator.setTitles(Arrays.asList(datas));
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return SimpleFragment.newInstance(datas[position]);
            }

            @Override
            public int getCount() {
                return datas.length;
            }
        });

        mViewIndicator.setViewPager(mViewPager);
        mViewIndicator.setOnTabSelectedListener(new ViewIndicator.OnTabSlelectedListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.d(TAG, "onPageScrolled() called with: " + "position = [" + position + "], positionOffset = [" + positionOffset + "], positionOffsetPixels = [" + positionOffsetPixels + "]");
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected: "+position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                Log.d(TAG, "onPageScrollStateChanged: state "+state);
            }
        });
    }
}
