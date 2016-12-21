package com.appstore.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.appstore.R;
import com.appstore.domain.CategoryInfo;
import com.appstore.utils.UIutil;

/**
 * Created by stephen on 2016/11/22.
 */

public class CategoryTitleHolder extends BaseHolder<CategoryInfo> {

    private TextView tv;

    @Override
    public View initView() {
        View view = View.inflate(UIutil.getContext(),R.layout.category_title,null);
        tv = (TextView) view.findViewById(R.id.categoryTitle);
        tv.setTextColor(Color.BLACK);
        /*int paddingV = UIutil.dip2px(4);
        int paddingH = UIutil.dip2px(7);
        tv.setPadding(paddingH,paddingV,paddingH,paddingV);*/
        tv.setBackgroundDrawable(UIutil.getDrawable(R.drawable.grid_item_bg));
        return view;
    }

    @Override
    public void refreshView(CategoryInfo categoryInfo) {
        tv.setText(categoryInfo.getTitle());
    }
}
