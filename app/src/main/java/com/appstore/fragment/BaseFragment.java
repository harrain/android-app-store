package com.appstore.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.appstore.fragment.LoadingPage.*;
import com.appstore.utils.VIewUtil;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {
    private LoadingPage loadingPage;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        if (loadingPage == null) {  // 之前的frameLayout 已经记录了一个爹了  爹是之前的ViewPager
            loadingPage = new LoadingPage(getActivity()){

                @Override
                public View createSuccessView() {
                    return BaseFragment.this.createSuccessView();
                }


                public LoadResult load() {
                    return BaseFragment.this.load();
                }
            };
        }else{
            VIewUtil.removeParent(loadingPage);// 移除frameLayout之前的爹
        }

        return loadingPage;  //  拿到当前viewPager 添加这个framelayout
    }

    public void show(){
        if(loadingPage!=null){
            loadingPage.show();
        }
    }

    /***
     *  创建成功的界面
     * @return
     */
    public  abstract View createSuccessView();
    /**
     * 请求服务器
     * @return
     */
    public abstract LoadingPage.LoadResult load();

    /**校验数据 */
    public LoadResult checkData(List datas) {
        if(datas==null){
            return LoadResult.error;//  请求服务器失败
        }else{
            if(datas.size()==0){
                return LoadResult.empty;
            }else{
                return LoadResult.success;
            }
        }

    }


    /*public static final int STATE_UNKNOWN = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_ERROR = 2;
    public static final int STATE_EMPTY = 3;
    public static final int STATE_SUCCESS = 4;
    public   int state = STATE_UNKNOWN;//状态state不能是static，
    // ，因为子类共用父类的静态变量，导致各子类state混淆

    private FrameLayout mFrameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 之前的frameLayout 已经记录了一个爹了  爹是之前的ViewPager
        if(mFrameLayout == null) {
            mFrameLayout = new FrameLayout(getContext());
            init();// 在FrameLayout中 添加4种不同的界面
        }else{
            VIewUtil.removeParent(mFrameLayout);// 移除frameLayout之前的爹
        }
        show();// 根据服务器的数据 切换状态
        return mFrameLayout;//  拿到当前viewPager 添加这个framelayout
    }*/

    /*private View loadingView;// 加载中的界面
    private View errorView;// 错误界面
    private View successView;// 加载成功的界面
    private View emptyView;// 空界面
    // 在FrameLayout中 添加几种不同的界面
    private void init() {
        loadingView = createLoadingView(); // 创建了加载中的界面
        if (loadingView != null) {
            mFrameLayout.addView(loadingView,
                    new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT));
        }
        errorView = createErrorView(); // 加载错误界面
        if (errorView != null) {
            mFrameLayout.addView(errorView,
                    new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT));
        }
        emptyView = createEmptyView(); // 加载空的界面
        if (emptyView != null) {
            mFrameLayout.addView(emptyView,
                    new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT));
        }
        successView = createSuccessView();
        if(successView != null) {
            mFrameLayout.addView(successView,
                    new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT));
        }
        showPage();
    }

    // 根据不同的状态显示不同的界面
    private void showPage() {
        if(loadingView != null){
            loadingView.setVisibility(state == STATE_UNKNOWN || state == STATE_LOADING ?
                    View.VISIBLE : View.INVISIBLE);
        }
        if(errorView != null){
            errorView.setVisibility(state == STATE_ERROR ?
                    View.VISIBLE : View.INVISIBLE);
        }
        if(emptyView != null){
            emptyView.setVisibility(state == STATE_EMPTY ?
                    View.VISIBLE : View.INVISIBLE);
        }

        if(successView != null){
            successView.setVisibility(state == STATE_SUCCESS ?
                    View.VISIBLE : View.INVISIBLE);
        }
    }

    protected enum LoadResult{
        error(2),empty(3),success(4);
        int value;
        LoadResult(int value){
            this.value = value;
        }
        public int getValue(){
            return value;
        }
    }

    // 根据服务器的数据 切换状态
    private void show() {
        if (state == STATE_ERROR || state == STATE_EMPTY) {
            state = STATE_LOADING;
        }
        // 请求服务器 获取服务器上数据 进行判断
        new AsyncTask<Void, LoadResult, LoadResult>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showPage();
            }

            @Override
            protected LoadResult doInBackground(Void... params) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LoadResult result = load();
                return result;
            }

            @Override
            protected void onPostExecute(LoadResult result) {
                if (result != null) {
                    state = result.getValue();
                    showPage();
                }
                super.onPostExecute(result);
            }
        }.execute();
    }

    protected abstract LoadResult load();
    protected abstract View createSuccessView();

    private View createErrorView() {
        View view = View.inflate(getContext(),R.layout.loadpage_error,null);
        Button page_bt = (Button) view.findViewById(R.id.page_bt);
        page_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });
        return view;
    }

    private View createEmptyView() {
        View view = View.inflate(getContext(),R.layout.loading_empty,null);
        return view;
    }

    private View createLoadingView() {
        View view = View.inflate(getContext(),R.layout.loading_page,null);
        return view;
    }*/

}
