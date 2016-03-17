package com.bluberry.album.entity;

/**
 * Created by blueberry on 2016/3/16.
 */
public class Image {

    public boolean checked ;
    public String path ;

    public Image() {
    }

    public Image(String path, boolean checked) {
        this.path = path;
        this.checked = checked;
    }
}
