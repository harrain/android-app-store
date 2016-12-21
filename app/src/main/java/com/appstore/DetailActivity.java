package com.appstore;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import com.appstore.domain.AppInfo;
import com.appstore.fragment.LoadingPage;
import com.appstore.fragment.LoadingPage.*;
import com.appstore.holder.DetailBottomHolder;
import com.appstore.holder.DetailDesHolder;
import com.appstore.holder.DetailInfoHolder;
import com.appstore.holder.DetailSafeHolder;
import com.appstore.holder.DetailScreenHolder;
import com.appstore.protocol.DetailProtocol;
import com.appstore.utils.UIutil;


public class DetailActivity extends BaseActivity {

    private  AppInfo load;
    private  String packageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        packageName = intent.getStringExtra("packageName");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        LoadingPage detailPage = new LoadingPage(this) {
            @Override
            public LoadResult load() {
                return DetailActivity.this.load();
            }

            @Override
            public View createSuccessView() {
                return DetailActivity.this.createSuccessView();
            }
        };
        detailPage.show();
        setContentView(detailPage);
    }

    private FrameLayout bottom_layout,detail_info,detail_safe,detail_des;
    private HorizontalScrollView detail_screen;
    private DetailInfoHolder infoHolder;
    private DetailSafeHolder safeHolder;
    private DetailDesHolder desHolder;
    private View createSuccessView() {
        View view = View.inflate(UIutil.getContext(),R.layout.activity_detail,null);

        //  操作 应用程序信息
        detail_info = (FrameLayout) view.findViewById(R.id.detail_info);
        infoHolder = new DetailInfoHolder();
        infoHolder.setData(load);
        detail_info.addView(infoHolder.getContentView());

        detail_safe = (FrameLayout) view.findViewById(R.id.detail_safe);
        safeHolder = new DetailSafeHolder();
        safeHolder.setData(load);
        detail_safe.addView(safeHolder.getContentView());

        detail_screen = (HorizontalScrollView) view.findViewById(R.id.detail_screen);
        DetailScreenHolder screenHolder = new DetailScreenHolder();
        screenHolder.setData(load);
        detail_screen.addView(screenHolder.getContentView());

        detail_des=(FrameLayout) view.findViewById(R.id.detail_des);
        desHolder=new DetailDesHolder();
        desHolder.setData(load);
        detail_des.addView(desHolder.getContentView());

        // 添加信息区域
        bottom_layout=(FrameLayout) view.findViewById(R.id.bottom_layout);
        DetailBottomHolder bottomHolder = new DetailBottomHolder();
        bottomHolder.setData(load);
        bottom_layout.addView(bottomHolder.getContentView());

        return view;
    }

    private LoadResult load() {
        DetailProtocol protocol = new DetailProtocol(packageName);
        load = protocol.load(0);
        if(load == null){
            return LoadResult.error;
        }else{
            return LoadResult.success;
        }

    }

    @Override
    protected void initActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
