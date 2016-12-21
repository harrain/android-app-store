package com.appstore.holder;

import android.view.View;
import android.widget.RelativeLayout;

import com.appstore.R;
import com.appstore.adapter.DefaultAdapter;
import com.appstore.utils.UIutil;

/**
 * Created by stephen on 2016/11/13.
 */

public class MoreHolder extends BaseHolder<Integer> {

    public static final int HAS_NO_MORE = 0;
    public static final int LOAD_ERROR = 1;
    public static final int HAS_MORE = 2;

    private RelativeLayout loading_more,loadMore_error;

    private DefaultAdapter adapter;

    private boolean  hasMore;

    public MoreHolder(DefaultAdapter adapter,boolean hasMore) {
        super();
        this.adapter=adapter;
        this.hasMore = hasMore;
        if(!hasMore){
            setData(0);
        }
    }

    @Override
    public View initView() {
        View view = View.inflate(UIutil.getContext(), R.layout.load_more,null);
        loading_more = (RelativeLayout) view.findViewById(R.id.rl_more_loading);
        loadMore_error = (RelativeLayout) view.findViewById(R.id.rl_more_error);
        return view;
    }

    @Override
    public void refreshView(Integer integer) {
        loading_more.setVisibility(integer == HAS_MORE ? View.VISIBLE : View.GONE);
        loadMore_error.setVisibility(integer == LOAD_ERROR ? View.VISIBLE : View.GONE);
    }

    @Override
    public View getContentView() {
        if(hasMore) {
            loadMore();
        }
        return super.getContentView();
    }

    private void loadMore() {
        // 请求服务器   加载下一批数据
        //  交给Adapter  让Adapter  加载更多数据
        adapter.loadMore();
    }
}
