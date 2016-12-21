package com.appstore.holder;

import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.appstore.R;
import com.appstore.domain.AppInfo;
import com.appstore.utils.GetHttp;
import com.appstore.utils.LogUtils;
import com.appstore.utils.UIutil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by stephen on 2016/11/18.
 */

public class DetailInfoHolder extends BaseHolder<AppInfo> {

    @ViewInject(R.id.item_icon)
    private ImageView item_icon;
    @ViewInject(R.id.item_title)
    private TextView item_title;
    @ViewInject(R.id.item_rating)
    private RatingBar item_rating;
    @ViewInject(R.id.item_download)
    private TextView item_download;
    @ViewInject(R.id.item_version)
    private TextView item_version;
    @ViewInject(R.id.item_date)
    private TextView item_date;
    @ViewInject(R.id.item_size)
    private TextView item_size;
    /***
     * 实例化控件和布局
     */
    @Override
    public View initView() {
        View view=View.inflate(UIutil.getContext(),R.layout.detail_app_info,null);
        ViewUtils.inject(this, view);
        return view;
    }
    /**
     * 给控件设置数据
     */
    @Override
    public void refreshView(AppInfo data) {
        System.out.println(data.getIconUrl());
        bitmapUtils.display(item_icon, GetHttp.URI+"image?name="+data.getIconUrl());
        item_title.setText(data.getName());
        item_rating.setRating(data.getStars());
        item_download.setText("下载:"+data.getDownloadNum());
        item_version.setText("版本:"+data.getVersion());
        item_date.setText("时间:"+data.getDate());
        item_size.setText("大小:"+ Formatter.formatFileSize(UIutil.getContext(), data.getSize()));
    }
}
