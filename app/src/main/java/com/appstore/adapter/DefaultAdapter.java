package com.appstore.adapter;

import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.appstore.holder.BaseHolder;
import com.appstore.holder.MoreHolder;
import com.appstore.utils.ThreadManager;
import com.appstore.utils.UIutil;

import java.util.List;



/**
 * Created by stephen on 2016/11/12.
 */

public abstract class DefaultAdapter<Data> extends BaseAdapter implements AdapterView.OnItemClickListener {

    private static final int MORE_ITEM = 1;
    private static final int DEFAULT_ITEM = 0;
    private MoreHolder moreHolder;
    List<Data> datas ;
    private ListView listView;

    public DefaultAdapter(List<Data> datas,ListView listView) {
        this.datas = datas;
        listView.setOnItemClickListener(this);
        this.listView = listView;
    }

    public List<Data> getDatas() {
        return datas;
    }

    public void setDatas(List<Data> datas) {
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /** 根据位置 判断当前条目是什么类型 */
    @Override
    public int getItemViewType(int position) {
        if(position == datas.size()){// 当前是最后一个条目
            return MORE_ITEM;
        }
        return getInnerItemViewType(position); // 如果不是最后一个条目 返回默认类型
    }

    protected int getInnerItemViewType(int position) {
        return DEFAULT_ITEM;
    }

    /** 当前ListView 有几种不同的条目类型 */
    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount()+1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseHolder holder = null;
        switch (getItemViewType(position)) {  // 判断当前条目时什么类型
            case MORE_ITEM:
                if (convertView == null) {
                    holder = getMoreHolder();
                } else {
                    holder = (BaseHolder) convertView.getTag();
                }
                break;
            default:
                if (convertView == null) {
                    holder = getHolder();
                } else {
                    holder = (BaseHolder) convertView.getTag();
                }
                if (position < datas.size()) {
                    holder.setData(datas.get(position));
                }
                break;
        }
        return holder.getContentView();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        position = position - listView.getHeaderViewsCount();// 获取到顶部条目的数量   位置去掉顶部view的数量
        onInnerItemClick(position);
    }
    /**在该方法去处理条目的点击事件*/
    public void onInnerItemClick(int position) {
    }

    /**
     * 是否有额外的数据
     * @return
     */
    protected boolean hasMore() {
        return true;
    }

    private BaseHolder getMoreHolder() {
        if(moreHolder!=null){
            return moreHolder;
        }else{
            moreHolder=new MoreHolder(this,hasMore());
            return moreHolder;
        }
    }

    public abstract BaseHolder<Data> getHolder();

    /**
     * 当加载更多条目显示的时候 调用该方法
     */
    public void loadMore() {
        ThreadManager.getInstance().createLongPool().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(2000);
                        final List<Data> newData = onLoadMore();
                        UIutil.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(newData == null){
                                    moreHolder.setData(moreHolder.LOAD_ERROR);
                                }else if(newData.size() == 0){
                                    moreHolder.setData(moreHolder.HAS_NO_MORE);
                                }else {
                                    moreHolder.setData(moreHolder.HAS_MORE);
                                    datas.addAll(newData);
                                    notifyDataSetChanged();
                                }
                            }
                        });
                    }
                }
        );
    }

    protected abstract List<Data> onLoadMore();
}
