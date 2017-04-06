package com.appstore.adapter;

import android.content.Intent;
import android.widget.ListView;
import android.widget.Toast;

import com.appstore.DetailActivity;
import com.appstore.domain.AppInfo;
import com.appstore.holder.AppHolder;
import com.appstore.holder.BaseHolder;
import com.appstore.manager.DownloadManager;
import com.appstore.utils.UIutil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stephen on 2016/11/13.
 */

public abstract class AppAdapter extends DefaultAdapter<AppInfo> {

    private List<AppHolder> mAppHolders = new ArrayList<>();
    public AppAdapter(List<AppInfo> appInfos, ListView listView) {

        super(appInfos,listView);
    }

    @Override
    public BaseHolder<AppInfo> getHolder() {
        AppHolder appHolder = new AppHolder();
        mAppHolders.add(appHolder);
        DownloadManager.getInstance().addObserver(appHolder);
        return appHolder;
    }

    public List<AppHolder> getAppHolders() {
        return mAppHolders;
    }

    @Override
    protected abstract List<AppInfo> onLoadMore();

    @Override
    public void onInnerItemClick(int position) {
        super.onInnerItemClick(position);
        Toast.makeText(UIutil.getContext(), "position:"+position, Toast.LENGTH_SHORT).show();
        AppInfo appInfo = datas.get(position);
        Intent intent=new Intent(UIutil.getContext(), DetailActivity.class);
        intent.putExtra("packageName", appInfo.getPackageName());
        UIutil.startActivity(intent);
    }
}
