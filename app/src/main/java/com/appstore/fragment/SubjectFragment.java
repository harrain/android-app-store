package com.appstore.fragment;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appstore.R;
import com.appstore.adapter.DefaultAdapter;
import com.appstore.domain.AppInfo;
import com.appstore.domain.SubjectInfo;
import com.appstore.holder.BaseHolder;
import com.appstore.protocol.SubjectProtocol;
import com.appstore.utils.BaseListView;
import com.appstore.utils.BitmapUtil;
import com.appstore.utils.UIutil;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import static android.view.View.inflate;
import static com.appstore.utils.UIutil.getContext;


public class SubjectFragment extends BaseFragment {

    List<SubjectInfo> datas;
    BitmapUtils bitmapUtil ;

    @Override
    public LoadingPage.LoadResult load() {
        SubjectProtocol subjectProtocol = new SubjectProtocol();
        datas = subjectProtocol.load(0);
        return checkData(datas);
    }

    @Override
    public View createSuccessView() {
        BaseListView listView=new BaseListView(getContext());
        listView.setAdapter(new SubjectAdapter(datas,listView));
        bitmapUtil = BitmapUtil.getBitmap();
        return listView;
    }

    private class SubjectAdapter extends DefaultAdapter<SubjectInfo> {
        public SubjectAdapter(List<SubjectInfo> list, ListView listView) {
            super(list,listView);
        }

        @Override
        public void onInnerItemClick(int position) {
            super.onInnerItemClick(position);
            Toast.makeText(getContext(),datas.get(position).getDes(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public BaseHolder<SubjectInfo> getHolder() {
            return new SubjectHolder();
        }

        @Override
        protected List<SubjectInfo> onLoadMore() {
            SubjectProtocol protocol = new SubjectProtocol();
            List<SubjectInfo> load = protocol.load(datas.size());
            datas.addAll(load);
            return load;
        }

        class SubjectHolder extends BaseHolder<SubjectInfo>{
            ImageView item_icon;
            TextView item_txt;
            View contentView;
            @Override
            public View initView() {
                contentView = View.inflate(UIutil.getContext(),R.layout.item_subject,null);
                this.item_icon=(ImageView) contentView.findViewById(R.id.item_icon);
                this.item_txt=(TextView) contentView.findViewById(R.id.item_txt);
                return contentView;
            }

            @Override
            public void refreshView(SubjectInfo subjectInfo) {
                this.item_txt.setText(subjectInfo.getDes());
                bitmapUtils.display(this.item_icon, "http://127.0.0.1:8090/image?name="+subjectInfo.getUrl());
            }
        }
    }
}
