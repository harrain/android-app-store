package com.appstore.holder;

import android.view.View;

import com.appstore.utils.BitmapUtil;
import com.lidroid.xutils.BitmapUtils;

/**
 * Created by stephen on 2016/11/12.
 */

public abstract class BaseHolder<Data> {

    private View contentView;
    private Data data;
    protected BitmapUtils bitmapUtils;
    public BaseHolder(){
        //bitmapUtils = BitmapUtil.getBitmap();
        contentView=initView();
        contentView.setTag(this);
    }
    /** 创建界面*/
    public  abstract View initView();
    public View getContentView() {
        return contentView;
    }
    public void setData(Data data){
        this.data=data;
        refreshView(data);
    }
    /** 根据数据刷新界面*/
    public abstract void refreshView(Data data);
}
