package com.appstore.fragment;

import android.view.View;
import com.appstore.adapter.AppAdapter;
import com.appstore.domain.AppInfo;
import com.appstore.holder.AppHolder;
import com.appstore.manager.DownloadManager;
import com.appstore.protocol.GameProtocol;
import com.appstore.utils.BaseListView;
import com.appstore.utils.UIutil;

import java.util.List;

public class GameFragment extends BaseFragment {

    private List<AppInfo> datas;

    private AppAdapter mAdapter;

    @Override
    public LoadingPage.LoadResult load() {
        GameProtocol protocol=new GameProtocol();
        datas = protocol.load(0);
        return checkData(datas);
    }

    @Override
    public View createSuccessView() {
        BaseListView listView=new BaseListView(UIutil.getContext());
        mAdapter = new AppAdapter(datas,listView) {
            @Override
            protected List<AppInfo> onLoadMore() {
                GameProtocol protocol = new GameProtocol();
                List<AppInfo> load = protocol.load(datas.size());
                datas.addAll(load);
                return load;
            }
        };
        listView.setAdapter(mAdapter);
        return listView;
    }

    @Override
    public void onResume() {
        // 重新添加监听
        if (mAdapter != null) {
            List<AppHolder> appItemHolders = mAdapter.getAppHolders();
            for (AppHolder appItemHolder : appItemHolders) {
                DownloadManager.getInstance().addObserver(appItemHolder);//重新添加
            }
            // 手动刷新-->重新获取状态,然后更新ui
            mAdapter.notifyDataSetChanged();
        }

        super.onResume();
    }

    @Override
    public void onPause() {
        // 移除监听
        if (mAdapter != null) {
            List<AppHolder> appItemHolders = mAdapter.getAppHolders();
            for (AppHolder appItemHolder : appItemHolders) {
                DownloadManager.getInstance().deleteObserver(appItemHolder);//删除
            }
        }
        super.onPause();
    }
}
