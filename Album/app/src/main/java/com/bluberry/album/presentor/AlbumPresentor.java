package com.bluberry.album.presentor;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.bluberry.album.base.BaseMvpPresentor;
import com.bluberry.album.entity.Image;
import com.bluberry.album.modle.AlbumModleImpl;
import com.bluberry.album.modle.IAlbumModle;
import com.bluberry.album.view.IAlbumView;

import java.util.List;

/**
 * Created by blueberry on 2016/3/16.
 */
public class AlbumPresentor<T> extends BaseMvpPresentor<T>{

    private IAlbumModle mAlbumModle ;

    private UIHandler uiHandler ;

    private List<Image> images ;

    public AlbumPresentor(){
        mAlbumModle = new AlbumModleImpl() ;
    }

    @Override
    protected void attachView(T view) {
        super.attachView(view);
        uiHandler = new UIHandler() ;
    }

    public void queryImagesOfAlbum(Context context){
       final  ContentResolver resolver = context.getApplicationContext().getContentResolver();
        uiHandler.obtainMessage(UIHandler.SHOW_PROGRESS_DIALOG_CODE).sendToTarget();
        new Thread(){
            @Override
            public void run() {
                images = mAlbumModle.queryImageOfAlbum(resolver);
                uiHandler.obtainMessage(UIHandler.SHOW_IMAGES_CODE).sendToTarget();
                uiHandler.obtainMessage(UIHandler.CANCEL_PROGRESS_DIALOG_CODE).sendToTarget();
            }
        }.start();
    }

    public List<Image> getImages(){
        return images ;
    }

    /**
     * 控制UI更新
     */
    private  class UIHandler extends  Handler{
        public static final int SHOW_IMAGES_CODE = 0x1 ;
        public static final int SHOW_PROGRESS_DIALOG_CODE=0x2;
        public static final int CANCEL_PROGRESS_DIALOG_CODE=0x3 ;
        private IAlbumView viewHolder ;

        public UIHandler(){
            super(Looper.getMainLooper());
            viewHolder = (IAlbumView) getView();
        }
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SHOW_IMAGES_CODE:
                    viewHolder.showImages();
                    break ;
                case SHOW_PROGRESS_DIALOG_CODE:
                    viewHolder.showProgressDialog();
                    break;
                case CANCEL_PROGRESS_DIALOG_CODE:
                    viewHolder.cancelProgressDialog();
                    break;
            }
        }
    }
}
