package com.appstore.fragment;

import android.view.View;
import com.appstore.adapter.AppAdapter;
import com.appstore.domain.AppInfo;
import com.appstore.protocol.GameProtocol;
import com.appstore.utils.BaseListView;
import com.appstore.utils.UIutil;

import java.util.List;

public class GameFragment extends BaseFragment {

    private List<AppInfo> datas;

    @Override
    public LoadingPage.LoadResult load() {
        GameProtocol protocol=new GameProtocol();
        datas = protocol.load(0);
        return checkData(datas);
    }

    @Override
    public View createSuccessView() {
        BaseListView listView=new BaseListView(UIutil.getContext());
        listView.setAdapter(new AppAdapter(datas,listView) {
            @Override
            protected List<AppInfo> onLoadMore() {
                GameProtocol protocol = new GameProtocol();
                List<AppInfo> load = protocol.load(datas.size());
                datas.addAll(load);
                return load;
            }
        });
        return listView;
    }
}
