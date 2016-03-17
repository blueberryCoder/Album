package com.bluberry.album.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by blueberry on 2016/3/17.
 */
public class IOUtils {
    public static void closeSafety(Closeable dst){
        if(null !=dst){
            try {
                dst.close();
                dst =null ;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
