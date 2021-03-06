package com.appstore.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appstore.R;
import com.appstore.adapter.AppAdapter;
import com.appstore.domain.AppInfo;
import com.appstore.protocol.AppProtocol;
import com.appstore.utils.BaseListView;
import com.appstore.utils.UIutil;

import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppFragment extends BaseFragment {

    List<AppInfo> datas;

    @Override
    public LoadingPage.LoadResult load() {
        /*int j = 2 + new Random().nextInt(2);//new Random().nextInt(2)产生的是【0,2】的整数值。
        switch(j){
            case 2 : return LoadingPage.LoadResult.error;
            case 3 : return LoadingPage.LoadResult.empty;
            case 4 : return LoadingPage.LoadResult.success;
        }
        return LoadingPage.LoadResult.error;*/
        AppProtocol appProtocol = new AppProtocol();
        datas = appProtocol.load(0);
        return checkData(datas);
    }

    @Override
    public View createSuccessView() {
        BaseListView mListView = new BaseListView(UIutil.getContext());
        mListView.setAdapter(new AppAdapter(datas,mListView) {
            @Override
            protected List<AppInfo> onLoadMore() {
                AppProtocol protocol=new AppProtocol();
                List<AppInfo> load = protocol.load(datas.size());
                datas.addAll(load);
                return load;
            }
        });
        return mListView;
    }

}
