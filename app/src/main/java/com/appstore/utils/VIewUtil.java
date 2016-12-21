package com.appstore.utils;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * Created by stephen on 2016/11/4.
 */

public class VIewUtil {
    public static void removeParent(View view){
        //  先找到爹 在通过爹去移除孩子
        ViewParent viewParent = view.getParent();
        //所有的控件 都有爹  爹一般情况下 就是ViewGoup,ViewGroup的最上层爹是整个屏幕
        if(viewParent instanceof ViewGroup){
            ViewGroup viewGroup = (ViewGroup) viewParent;
            viewGroup.removeView(view);
        }
    }
}
