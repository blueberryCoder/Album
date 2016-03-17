package com.bluberry.album.modle;

import android.content.ContentResolver;

import com.bluberry.album.entity.Image;

import java.util.List;

/**
 * Created by blueberry on 2016/3/16.
 */
public interface IAlbumModle {
    List<Image>  queryImageOfAlbum(ContentResolver resolver) ;
}
