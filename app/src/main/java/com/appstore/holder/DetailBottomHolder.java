package com.appstore.holder;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.appstore.R;
import com.appstore.domain.AppInfo;
import com.appstore.manager.DownLoadInfo;
import com.appstore.manager.DownloadManager;
import com.appstore.utils.CommonUtils;
import com.appstore.utils.FileUtil;
import com.appstore.utils.PrintDownLoadInfo;
import com.appstore.utils.ThreadManager;
import com.appstore.utils.UIutil;
import com.appstore.view.ProgressButton;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.File;

/**
 * Created by stephen on 2016/11/21.
 */

public class DetailBottomHolder extends BaseHolder<AppInfo> implements View.OnClickListener, DownloadManager.DownLoadObserver {

    @ViewInject(R.id.bottom_favorites)
    Button bottom_favorites;
    @ViewInject(R.id.bottom_share)
    Button bottom_share;
    @ViewInject(R.id.progress_btn)
    ProgressButton mProgressButton;
    private AppInfo mData;

    @Override
    public View initView() {
        View view = View.inflate(UIutil.getContext(), R.layout.detail_bottom,null);
        ViewUtils.inject(this,view);
        return view;
    }

    @Override
    public void refreshView(AppInfo appInfo) {
        mData = appInfo;
        bottom_favorites.setOnClickListener(this);
        bottom_share.setOnClickListener(this);
        mProgressButton.setOnClickListener(this);

        /*=============== 根据不同的状态给用户提示 ===============*/
        DownLoadInfo info = DownloadManager.getInstance().getDownLoadInfo(appInfo);
        refreshProgressBtnUI(info);

    }

    public void refreshProgressBtnUI(DownLoadInfo info) {

        mProgressButton.setBackgroundResource(R.drawable.selector_app_detail_bottom_normal);
        switch (info.state) {
            /**
             状态(编程记录)  	|  给用户的提示(ui展现)
             ----------------|----------------------
             未下载			|下载
             下载中			|显示进度条
             暂停下载			|继续下载
             等待下载			|等待中...
             下载失败 			|重试
             下载完成 			|安装
             已安装 			|打开
             */
            case DownloadManager.STATE_UNDOWNLOAD:// 未下载
                mProgressButton.setText("下载");
                break;
            case DownloadManager.STATE_DOWNLOADING:// 下载中
                mProgressButton.setBackgroundResource(R.drawable.selector_app_detail_bottom_downloading);
                mProgressButton.setProgressEnable(true);
                mProgressButton.setMax(info.max);
                mProgressButton.setProgress(info.curProgress);
                int progress = (int) (info.curProgress * 100.f / info.max + .5f);
                mProgressButton.setText(progress + "%");
                break;
            case DownloadManager.STATE_PAUSEDOWNLOAD:// 暂停下载
                mProgressButton.setText("继续下载");
                break;
            case DownloadManager.STATE_WAITINGDOWNLOAD:// 等待下载
                mProgressButton.setText("等待中...");
                break;
            case DownloadManager.STATE_DOWNLOADFAILED:// 下载失败
                mProgressButton.setText("重试");
                break;
            case DownloadManager.STATE_DOWNLOADED:// 下载完成
                mProgressButton.setProgressEnable(false);
                mProgressButton.setText("安装");
                break;
            case DownloadManager.STATE_INSTALLED:// 已安装
                mProgressButton.setText("打开");
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.progress_btn:

                DownLoadInfo info = DownloadManager.getInstance().getDownLoadInfo(mData);

                switch (info.state) {
                    /**
                     状态(编程记录)     | 用户行为(触发操作)
                     ----------------  | -----------------
                     未下载			   | 去下载
                     下载中			   | 暂停下载
                     暂停下载		   | 断点继续下载
                     等待下载		   | 取消下载
                     下载失败 		   | 重试下载
                     下载完成 		   | 安装应用
                     已安装 			   | 打开应用
                     */
                    case DownloadManager.STATE_UNDOWNLOAD:// 未下载
                        doDownload(info);
                        break;
                    case DownloadManager.STATE_DOWNLOADING:// 下载中
                        pauseDownLoad(info);
                        break;
                    case DownloadManager.STATE_PAUSEDOWNLOAD:// 暂停下载
                        doDownload(info);
                        break;
                    case DownloadManager.STATE_WAITINGDOWNLOAD:// 等待下载
                        cancelDownLoad(info);
                        break;
                    case DownloadManager.STATE_DOWNLOADFAILED:// 下载失败
                        doDownload(info);
                        break;
                    case DownloadManager.STATE_DOWNLOADED:// 下载完成
                        installApk(info);
                        break;
                    case DownloadManager.STATE_INSTALLED:// 已安装
                        openApk(info);
                        break;

                    default:
                        break;
                }

                break;
            default:
                break;
        }
    }

    private void doDownload(DownLoadInfo downloadInfo) {
        /*=============== 根据不同的状态触发不同的操作 ===============*/
        /*File dir = FileUtil.getDownloadDir();
        File file = new File(dir, mData.getPackageName() + ".apk");
        String savePath = file.getAbsolutePath();
        DownLoadInfo downloadInfo = new DownLoadInfo();
        downloadInfo.savePath = savePath;
        downloadInfo.downloadUrl = mData.getDownloadUrl();*/

        DownloadManager.getInstance().download(downloadInfo);
    }

    private void pauseDownLoad(DownLoadInfo info) {
        DownloadManager.getInstance().pause(info);
    }

    private void cancelDownLoad(DownLoadInfo info) {
        DownloadManager.getInstance().cancel(info);
    }

    private void installApk(DownLoadInfo info) {
        File apkFile = new File(info.savePath);
        CommonUtils.installApp(UIutil.getContext(),apkFile);
    }

    private void openApk(DownLoadInfo info){
        CommonUtils.openApp(UIutil.getContext(),info.getPackageName());
    }

    /*=============== 收到数据改变,更新ui ===============*/
    @Override
    public void onDownLoadInfoChange(final DownLoadInfo info) {
        // 过滤DownLoadInfo
        if (!info.packageName.equals(mData.getPackageName())) {
            return;
        }

        PrintDownLoadInfo.printDownLoadInfo(info);
        /**切换线程，要不然还在子线程通知观察者*/
        UIutil.postTaskSafely(new Runnable() {
            @Override
            public void run() {
                refreshProgressBtnUI(info);
            }
        });
    }

    public void addObserverAndRerefresh() {
        DownloadManager.getInstance().addObserver(this);
        // 手动刷新
        DownLoadInfo downLoadInfo = DownloadManager.getInstance().getDownLoadInfo(mData);
        DownloadManager.getInstance().notifyObservers(downLoadInfo);// 方式一
        // refreshProgressBtnUI(downLoadInfo);//方式二
    }

}
