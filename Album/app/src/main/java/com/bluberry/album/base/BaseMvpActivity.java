package com.bluberry.album.base;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by blueberry on 2016/3/16.
 *
 * Activity 需要实现 T接口
 */
public abstract class BaseMvpActivity<T,P extends BaseMvpPresentor<T>> extends Activity {

    protected  P mPresentor ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresentor =createPresentor() ;
        mPresentor.attachView((T)this);
    }

   protected abstract P createPresentor() ;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresentor.detach();
    }
}
