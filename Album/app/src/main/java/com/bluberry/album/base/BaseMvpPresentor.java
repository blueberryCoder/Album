package com.bluberry.album.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by blueberry on 2016/3/16.
 */
public abstract class BaseMvpPresentor <T>{
    protected Reference<T> mReference ;

    protected void attachView(T  view){
        mReference = new WeakReference<T>(view);
    }

    protected boolean isAttactedView(){
        return mReference!=null && mReference.get()!=null ;
    }

    protected  T getView(){
        if (isAttactedView()) return (T)mReference.get() ;
        return null ;
    }

    protected void detach(){
        if(isAttactedView()){
            mReference.clear();
            mReference= null ;
        }
    }
}
