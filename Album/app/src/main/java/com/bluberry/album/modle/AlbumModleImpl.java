package com.bluberry.album.modle;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

import com.bluberry.album.entity.Image;
import com.bluberry.album.utils.IOUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by blueberry on 2016/3/17.
 */
public class AlbumModleImpl implements IAlbumModle {

    /**
     * 获取相册中左右图片的信息
     * @param resolver
     * @return
     */
    @Override
    public List<Image> queryImageOfAlbum(ContentResolver resolver) {
        String[] projections = new String[]{
                MediaStore.Images.ImageColumns.DATA,
        };
        Cursor query = null;
        List<Image> images = new ArrayList<>();
        try {
            query = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projections, null, null, null);
            while(query.moveToNext()){
                String path = query.getString(0);
                images.add(new Image(path,false));
            }
            return images;
        } finally {
            IOUtils.closeSafety(query);
        }
    }
}
