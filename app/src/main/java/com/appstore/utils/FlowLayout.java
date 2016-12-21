package com.appstore.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stephen on 2016/11/23.
 */

public class FlowLayout extends ViewGroup {

    private int horizontolSpacing=UIutil.dip2px(13);
    private int verticalSpacing=UIutil.dip2px(13);

    private Line currentline;// 当前的行
    private int useWidth=0;// 当前行使用的宽度
    private List<Line> mLines=new ArrayList<Line>();
    private int width;

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 测量 当前控件Flowlayout
    // 父类是有义务测量每个孩子的
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mLines.clear();//清空行集合
        currentline=null;
        useWidth=0;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        width = widthSize-getPaddingLeft()-getPaddingRight();
        int height = heightSize-getPaddingBottom()-getPaddingTop(); // 获取到宽和高

        int childeWidthMode;
        int childeHeightMode;
        //  为了测量每个孩子 需要指定每个孩子测量规则
        childeWidthMode = widthMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : widthMode;
        childeHeightMode = heightMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : heightMode;
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width,childeWidthMode);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height,childeHeightMode);

        currentline=new Line();// 创建了第一行
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            System.out.println("孩子的数量:"+getChildCount());
            child.measure(childWidthMeasureSpec,childHeightMeasureSpec);

            int measuredWidth = child.getMeasuredWidth();
            useWidth += measuredWidth;// 让当前行加上使用的长度
            if(useWidth <= width){
                currentline.addChild(child);//这时候证明当前的孩子是可以放进当前的行里,放进去
                useWidth += horizontolSpacing;
                if(useWidth>width){
                    //换行
                    newLine();
                }
            }else{
                //换行
                if(currentline.getChildCount() < 1){
                    currentline.addChild(child);// 保证当前行里面最少有一个孩子
                }
                newLine();
            }

        }
        if(!mLines.contains(currentline)){
            mLines.add(currentline);// 添加最后一行
        }
        int totalHeight = 0;
        for(Line line : mLines){
            totalHeight += line.getHeight();
        }
        totalHeight += verticalSpacing*(mLines.size() - 1) + getPaddingTop() + getPaddingBottom();
        System.out.println(totalHeight);
        setMeasuredDimension(width+getPaddingLeft()+getPaddingRight(),resolveSize(totalHeight, heightMeasureSpec));

    }

    private void newLine() {
        mLines.add(currentline);// 记录之前的行
        currentline=new Line(); // 创建新的一行
        useWidth=0;
    }

    // 分配每个孩子的位置
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        l+=getPaddingLeft();
        t+=getPaddingTop();
        for (int i = 0; i < mLines.size(); i++) {
            Line line = mLines.get(i);
            line.layout(l,t);//交给每一行去分配
            t+= line.getHeight() + verticalSpacing;
        }
    }

    private class Line {
        int height = 0; //当前行的高度
        int lineWidth = 0;
        private List<View> children = new ArrayList<View>();

        /**
         * 添加一个孩子
         *
         * @param child
         */
        public void addChild(View child) {
            children.add(child);
            if (child.getMeasuredHeight() > height) {
                height = child.getMeasuredHeight();
            }
            lineWidth += child.getMeasuredWidth();
        }

        public int getHeight() {
            return height;
        }

        /**
         * 返回孩子的数量
         *
         * @return
         */
        public int getChildCount() {
            return children.size();
        }

        public void layout(int l, int t) {
            lineWidth += horizontolSpacing*(children.size()-1);
            int surplusChild = 0;
            int surplus = width - lineWidth;
            if(surplus > 0 && children.size() > 0){

                    System.out.println("children:" + children.size());
                    surplusChild = surplus / children.size();

            }

            for (int i = 0; i < children.size(); i++) {
                View child = children.get(i);
                child.layout(l,t,l+child.getMeasuredWidth()+surplusChild,t+child.getMeasuredHeight());
                l+=child.getMeasuredWidth()+ surplusChild+ horizontolSpacing ;
            }
        }
    }
}
