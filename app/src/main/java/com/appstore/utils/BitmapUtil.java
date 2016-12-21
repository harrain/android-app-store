package com.appstore.utils;

import com.lidroid.xutils.BitmapUtils;

/**
 * Created by stephen on 2016/11/10.
 */

public class BitmapUtil {
    BitmapUtil(){}
    private static BitmapUtils bitmapUtil;
    public static BitmapUtils getBitmap(){
        if(bitmapUtil == null){
            // 第二个参数 缓存图片的路径 // 加载图片 最多消耗多少比例的内存 0.05-0.8f
            bitmapUtil = new BitmapUtils(UIutil.getContext(),FileUtil.getIconDir().getAbsolutePath(),0.6f);
        }
        return bitmapUtil;
    }
}
