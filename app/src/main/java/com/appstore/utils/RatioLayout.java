package com.appstore.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by stephen on 2016/11/21.
 */

public class RatioLayout extends FrameLayout {

    // 按照宽高比例去显示
    private  float ratio = 1.43f; // 比例值

    public RatioLayout(Context context) {
        super(context);
    }

    public RatioLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RatioLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /**参数1 命名控件 参数2 属性的名字 参数3 默认的值*/
        float  ratio = attrs.getAttributeFloatValue(
                "http://schemas.android.com/apk/res-auto","ratio",1.43f);
        setRatio(ratio);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**widthMeasureSpec 宽度的规则 包含了两部分 模式 值*/
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);/**模式*/
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);/**宽度大小*/
        int width = widthSize - getPaddingLeft() - getPaddingRight();/**去掉左右的padding*/

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);/**模式*/
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);/**长度大小*/
        int height = heightSize - getPaddingTop() - getPaddingBottom();/**去掉上下的padding*/

        if(widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY){
            height = (int) (width / ratio + 0.5f);/**修正一下 高度的值 让高度=宽度/比例,+0.5f是确保四舍五入*/
        }else if (widthMode != MeasureSpec.EXACTLY
                && heightMode == MeasureSpec.EXACTLY) {
             /**由于高度是精确的值 ,宽度随着高度的变化而变化*/
            width = (int) ((height * ratio) + 0.5f);
        }
        /**重新制作了新的规则*/
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width + getPaddingLeft() + getPaddingRight(),MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height + getPaddingTop() + getPaddingBottom(),MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }
}
