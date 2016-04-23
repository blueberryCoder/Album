package com.blueberry.indicator;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by blueberry on 2016/4/23.
 */
public class SimpleFragment extends Fragment {
    private static final String TAG = "SimpleFragment";

    private static final String KEY = "NAME";

    public static SimpleFragment newInstance(String title) {

        Bundle args = new Bundle();
        args.putString(KEY,title);
        SimpleFragment fragment = new SimpleFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView tv = new TextView(getContext());
        tv.setText(getArguments().getString(KEY,"nothing"));
        tv.setGravity(Gravity.CENTER);
        return tv ;
    }
}
