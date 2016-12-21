package com.appstore.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Created by stephen on 2016/11/22.
 */

public class DrawableUtil {
    public static GradientDrawable createShape(int color){
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(UIutil.dip2px(5));//设置4个角的弧度
        drawable.setColor(color);
        return drawable;
    }

    public static StateListDrawable createSelectorDrawable(Drawable pressedDrawable,Drawable normalDrawalbe){
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed},pressedDrawable);
        stateListDrawable.addState(new int[]{},normalDrawalbe);
        return stateListDrawable;
    }
}
