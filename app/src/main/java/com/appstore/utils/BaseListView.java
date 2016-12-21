package com.appstore.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.appstore.R;

/**
 * Created by stephen on 2016/11/11.
 */

public class BaseListView extends ListView{
    public BaseListView(Context context) {
        super(context);
        init();
    }

    public BaseListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //		setSelector  点击显示的颜色
        //		setCacheColorHint  拖拽的颜色
        //		setDivider  每个条目的间隔的分割线
        this.setSelector(R.drawable.nothing);
        this.setCacheColorHint(UIutil.getColor(R.color.nothing));
        this.setDivider(UIutil.getDrawable(R.drawable.nothing));

    }
}
