package com.appstore.utils;

import com.lidroid.xutils.BitmapUtils;

import java.io.File;

/**
 * Created by stephen on 2016/11/10.
 */

public class BitmapUtil {
    BitmapUtil(){}
    private static BitmapUtils bitmapUtil;
    public static BitmapUtils getBitmap(String packageName){
        /*if(bitmapUtil == null){
        }*/
        String path = FileUtil.getIconDir().getAbsolutePath()+ File.separator+packageName;
        // 第二个参数 缓存图片的路径 // 加载图片 最多消耗多少比例的内存 0.05-0.8f
        bitmapUtil = new BitmapUtils(UIutil.getContext(),path,0.6f);
        return bitmapUtil;
    }

    public static BitmapUtils getBitmap(){
        if(bitmapUtil == null){
            String path = FileUtil.getIconDir().getAbsolutePath();
            // 第二个参数 缓存图片的路径 // 加载图片 最多消耗多少比例的内存 0.05-0.8f
            bitmapUtil = new BitmapUtils(UIutil.getContext(),path,0.6f);
        }
        return bitmapUtil;
    }
}
