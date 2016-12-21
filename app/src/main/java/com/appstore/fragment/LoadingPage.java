package com.appstore.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.appstore.R;
import com.appstore.utils.LogUtils;
import com.appstore.utils.ThreadManager;
import com.appstore.utils.UIutil;

/**
 * Created by stephen on 2016/11/5.
 */

public abstract class LoadingPage extends FrameLayout {

    public static final int STATE_UNKNOWN = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_ERROR = 2;
    public static final int STATE_EMPTY = 3;
    public static final int STATE_SUCCESS = 4;
    public   int state = STATE_UNKNOWN;//状态state不能是static，
    // ，因为子类共用父类的静态变量，导致各子类state混淆
    private View loadingView;// 加载中的界面
    private View errorView;// 错误界面
    private View successView;// 加载成功的界面
    private View emptyView;// 空界面

    LoadResult result ;

    public LoadingPage(Context context) {
        super(context);
        init();
    }

    public LoadingPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    // 在FrameLayout中 添加几种不同的界面
    private void init() {
        loadingView = createLoadingView(); // 创建了加载中的界面
        if (loadingView != null) {
            this.addView(loadingView,
                    new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT));
        }
        errorView = createErrorView(); // 加载错误界面
        if (errorView != null) {
            this.addView(errorView,
                    new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT));
        }
        emptyView = createEmptyView(); // 加载空的界面
        if (emptyView != null) {
            this.addView(emptyView,
                    new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT));
        }
        /*successView = createSuccessView();
        if(successView != null) {
            this.addView(successView,
                    new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT));
        }*/
        showPage();// 根据不同的状态显示不同的界面
        //show();
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

        if (state == STATE_SUCCESS) {
            if (successView == null) {
                successView = createSuccessView();
                this.addView(successView, new FrameLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            }
            successView.setVisibility(View.VISIBLE);
        } else {
            if (successView != null) {
                successView.setVisibility(View.INVISIBLE);
            }
        }

        /*if (successView == null) {
            successView = createSuccessView();
            this.addView(successView, new FrameLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }

        if(successView != null){
            successView.setVisibility(state == STATE_SUCCESS ?
                    View.VISIBLE : View.INVISIBLE);
        }*/
    }

    public enum LoadResult{
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
    public void show() {
        if (state == STATE_ERROR || state == STATE_EMPTY ) {
            state = STATE_LOADING;
        }
        // 请求服务器 获取服务器上数据 进行判断
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {

            @Override
            public void run() {
                SystemClock.sleep(2000);
                result = load();
                UIutil.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (result != null) {
                            state = result.getValue();
                            LogUtils.d(Integer.toString(state));
                            showPage(); // 状态改变了,重新判断当前应该显示哪个界面
                        }
                    }
                });
            }
        });
         /*final AsyncTask asyncTask = new AsyncTask<Void, LoadResult, LoadResult>() {
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
                LoadResult result = load();//并不影响运行，AyncTask依然可以使用
                return result;
            }

            @Override
            protected void onPostExecute(LoadResult result) {
                if (result != null) {
                    state = result.getValue();
                    LogUtils.d(Integer.toString(state));
                    showPage();
                }
                super.onPostExecute(result);
            }
        };
        asyncTask.execute();*/

        showPage();
    }

    public abstract LoadResult load() ;


    public abstract View createSuccessView();

    private View createErrorView() {
        View view = View.inflate(UIutil.getContext(), R.layout.loadpage_error,null);
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
        View view = View.inflate(UIutil.getContext(),R.layout.loading_empty,null);
        return view;
    }

    private View createLoadingView() {
        View view = View.inflate(UIutil.getContext(),R.layout.loading_page,null);
        return view;
    }
}
