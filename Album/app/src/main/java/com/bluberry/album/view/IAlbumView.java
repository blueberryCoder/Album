package com.bluberry.album.view;

import com.bluberry.album.entity.Image;

import java.util.List;

/**
 * Created by blueberry on 2016/3/16.
 */
public interface IAlbumView {
    void showImages() ;
    void showProgressDialog();
    void cancelProgressDialog();
}
