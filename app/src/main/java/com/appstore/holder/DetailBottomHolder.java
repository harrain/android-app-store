package com.appstore.holder;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.appstore.R;
import com.appstore.domain.AppInfo;
import com.appstore.utils.UIutil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by stephen on 2016/11/21.
 */

public class DetailBottomHolder extends BaseHolder<AppInfo> implements View.OnClickListener {

    @ViewInject(R.id.bottom_favorites)
    Button bottom_favorites;
    @ViewInject(R.id.bottom_share)
    Button bottom_share;
    @ViewInject(R.id.progress_btn)
    Button progress_btn;

    @Override
    public View initView() {
        View view = View.inflate(UIutil.getContext(), R.layout.detail_bottom,null);
        ViewUtils.inject(this,view);
        return view;
    }

    @Override
    public void refreshView(AppInfo appInfo) {
        bottom_favorites.setOnClickListener(this);
        bottom_share.setOnClickListener(this);
        progress_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_favorites:
                Toast.makeText(UIutil.getContext(), "收藏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bottom_share:
                Toast.makeText(UIutil.getContext(), "分享", Toast.LENGTH_SHORT).show();
                break;
            case R.id.progress_btn:
                Toast.makeText(UIutil.getContext(), "下载", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
