package com.appstore.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import com.appstore.R;
import com.appstore.adapter.AppAdapter;
import com.appstore.fragment.LoadingPage.*;//内部枚举，内部类引用
import com.appstore.domain.AppInfo;
import com.appstore.holder.AppHolder;
import com.appstore.holder.HomePictureHolder;
import com.appstore.manager.DownloadManager;
import com.appstore.protocol.HomeProtocol;
import com.appstore.utils.BaseListView;
import com.appstore.utils.BitmapUtil;
import com.appstore.utils.FileUtil;
import com.appstore.utils.UIutil;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.PauseOnScrollListener;

import java.io.File;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {

    List<AppInfo> datas;
    BitmapUtils bitmapUtils;
    List<String> pictures;

    AppAdapter mAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        show();
    }

    @Override
    public View createSuccessView() {
        BaseListView mListView = new BaseListView(UIutil.getContext());

        HomePictureHolder picHolder = new HomePictureHolder();
        picHolder.setData(pictures);
        View contentView = picHolder.getContentView();
        mListView.addHeaderView(contentView);

        mAdapter = new AppAdapter(datas,mListView) {
            @Override
            protected List<AppInfo> onLoadMore() {
                HomeProtocol protocol = new HomeProtocol();
                List<AppInfo> list = protocol.load(datas.size());
                datas.addAll(list);
                return list;
            }

            /*@Override
            public void onInnerItemClick(int position) {
                super.onInnerItemClick(position);
                Toast.makeText(getContext(),"position:"+position,Toast.LENGTH_SHORT);
                Intent intent = new Intent(getContext(), DetailActivity.class);
                AppInfo appInfo = datas.get(position);
                intent.putExtra("packageName",appInfo.getPackageName());
                startActivity(intent);
            }*/
        };
        mListView.setAdapter(mAdapter);

        bitmapUtils = BitmapUtil.getBitmap("home");
        // 第二个参数 慢慢滑动的时候是否加载图片 false  加载   true 不加载
        //  第三个参数  飞速滑动的时候是否加载图片  true 不加载
        mListView.setOnScrollListener(new PauseOnScrollListener(bitmapUtils,false,true));
        bitmapUtils.configDefaultLoadingImage(R.drawable.ic_default);
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_default);
        return mListView;
    }

    @Override
    public LoadResult load() {
        HomeProtocol mHomeProtocol = new HomeProtocol();
        datas = mHomeProtocol.load(0);
          pictures = mHomeProtocol.getPictures();

        //LogUtils.d("load到的服务器数据："+Integer.toString(datas.size()));
        return checkData(datas);
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
