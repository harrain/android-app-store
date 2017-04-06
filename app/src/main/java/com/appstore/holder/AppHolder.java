package com.appstore.holder;

import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.appstore.R;
import com.appstore.domain.AppInfo;
import com.appstore.manager.DownLoadInfo;
import com.appstore.manager.DownloadManager;
import com.appstore.utils.BitmapUtil;
import com.appstore.utils.CommonUtils;
import com.appstore.utils.PrintDownLoadInfo;
import com.appstore.utils.UIutil;
import com.appstore.view.CircleProgressView;

import java.io.File;

import static com.appstore.utils.UIutil.getContext;

/**
 * Created by stephen on 2016/11/12.
 */

public class AppHolder extends BaseHolder<AppInfo> implements View.OnClickListener ,DownloadManager.DownLoadObserver{

    ImageView item_icon;
    TextView item_title,item_size,item_bottom;
    RatingBar item_rating;

    CircleProgressView progressView;
    AppInfo mData;

    public void refreshView(AppInfo data){

        // 清除复用convertView之后的progress效果
        progressView.setProgress(0);
        mData = data;

        this.item_title.setText(data.getName());// 设置应用程序的名字
        String size= Formatter.formatFileSize(UIutil.getContext(), data.getSize());
        this.item_size.setText(size);
        this.item_bottom.setText(data.getDes());
        float stars = data.getStars();
        this.item_rating.setRating(stars); // 设置ratingBar的值
        String iconUrl = data.getIconUrl();  //http://127.0.0.1:8090/image?name=app/com.youyuan.yyhl/icon.jpg

        // 显示图片的控件
        bitmapUtils = BitmapUtil.getBitmap(data.getPackageName());
        bitmapUtils.display(this.item_icon, "Http://127.0.0.1:8090/image?name="+iconUrl);

        /*=============== 根据不同的状态给用户提示 ===============*/
        DownLoadInfo info = DownloadManager.getInstance().getDownLoadInfo(data);
        refreshCircleProgressViewUI(info);
    }

    @Override
    public View initView() {
        View contentView=View.inflate(getContext(), R.layout.item_app, null);
        this.item_icon=(ImageView) contentView.findViewById(R.id.item_icon);
        this.item_title=(TextView) contentView.findViewById(R.id.item_title);
        this.item_size=(TextView) contentView.findViewById(R.id.item_size);
        this.item_bottom=(TextView) contentView.findViewById(R.id.item_bottom);
        this.item_rating=(RatingBar) contentView.findViewById(R.id.item_rating);

        progressView = (CircleProgressView) contentView.findViewById(R.id.item_action);
        progressView.setOnClickListener(this);
        return contentView;
    }

    public void refreshCircleProgressViewUI(DownLoadInfo info) {
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
                progressView.setNote("下载");
                progressView.setIcon(R.drawable.ic_download);
                break;
            case DownloadManager.STATE_DOWNLOADING:// 下载中
                progressView.setProgressEnable(true);
                progressView.setMax(info.max);
                progressView.setProgress(info.curProgress);
                int progress = (int) (info.curProgress * 100.f / info.max + .5f);
                progressView.setNote(progress + "%");
                progressView.setIcon(R.drawable.ic_pause);
                break;
            case DownloadManager.STATE_PAUSEDOWNLOAD:// 暂停下载
                progressView.setNote("继续下载");
                progressView.setIcon(R.drawable.ic_resume);
                break;
            case DownloadManager.STATE_WAITINGDOWNLOAD:// 等待下载
                progressView.setNote("等待中...");
                progressView.setIcon(R.drawable.ic_pause);
                break;
            case DownloadManager.STATE_DOWNLOADFAILED:// 下载失败
                progressView.setNote("重试");
                progressView.setIcon(R.drawable.ic_redownload);
                break;
            case DownloadManager.STATE_DOWNLOADED:// 下载完成
                progressView.setProgressEnable(false);
                progressView.setNote("安装");
                progressView.setIcon(R.drawable.ic_install);
                break;
            case DownloadManager.STATE_INSTALLED:// 已安装
                progressView.setNote("打开");
                progressView.setIcon(R.drawable.ic_install);
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_action:

                DownLoadInfo info = DownloadManager.getInstance().getDownLoadInfo(mData);

                switch (info.state) {
                    /**
                     状态(编程记录)     | 用户行为(触发操作)
                     ----------------| -----------------
                     未下载			| 去下载
                     下载中			| 暂停下载
                     暂停下载			| 断点继续下载
                     等待下载			| 取消下载
                     下载失败 			| 重试下载
                     下载完成 			| 安装应用
                     已安装 			| 打开应用
                     */
                    case DownloadManager.STATE_UNDOWNLOAD:// 未下载
                        doDownLoad(info);
                        break;
                    case DownloadManager.STATE_DOWNLOADING:// 下载中
                        pauseDownLoad(info);
                        break;
                    case DownloadManager.STATE_PAUSEDOWNLOAD:// 暂停下载
                        doDownLoad(info);
                        break;
                    case DownloadManager.STATE_WAITINGDOWNLOAD:// 等待下载
                        cancelDownLoad(info);
                        break;
                    case DownloadManager.STATE_DOWNLOADFAILED:// 下载失败
                        doDownLoad(info);
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

    /**
     * 打开应用
     * @param info
     */
    private void openApk(DownLoadInfo info) {
        CommonUtils.openApp(UIutil.getContext(), info.packageName);
    }

    /**
     * 安装应用
     * @param info
     */
    private void installApk(DownLoadInfo info) {
        File apkFile = new File(info.savePath);
        CommonUtils.installApp(UIutil.getContext(), apkFile);
    }

    /**
     * 取消下载
     * @param info
     */
    private void cancelDownLoad(DownLoadInfo info) {
        DownloadManager.getInstance().cancel(info);

    }

    /**
     * 暂停下载
     * @param info
     */
    private void pauseDownLoad(DownLoadInfo info) {
        DownloadManager.getInstance().pause(info);
    }

    /**
     * 开始下载
     * @param info
     */
    private void doDownLoad(DownLoadInfo info) {
		/*=============== 根据不同的状态触发不同的操作 ===============*/
		/*// 下载apk放置的目录
		String dir = FileUtils.getDir("download");// sdcard/android/data/包名/download
		File file = new File(dir, mData.packageName + ".apk");// sdcard/android/data/包名/download/com.itheima.www.apk
		// 保存路径
		String savePath = file.getAbsolutePath();// sdcard/android/data/包名/download/com.itheima.www.apk

		DownLoadInfo info = new DownLoadInfo();
		info.savePath = savePath;
		info.downloadUrl = mData.downloadUrl;
		info.packageName = mData.packageName;*/

        DownloadManager.getInstance().download(info);
    }

    /*=============== 收到数据改变,更新ui ===============*/
    @Override
    public void onDownLoadInfoChange(final DownLoadInfo info) {
        // 过滤DownLoadInfo
        if (!info.packageName.equals(mData.getPackageName())) {
            return;
        }
        PrintDownLoadInfo.printDownLoadInfo(info);
        UIutil.postTaskSafely(new Runnable() {
            @Override
            public void run() {
                refreshCircleProgressViewUI(info);
            }
        });
    }
}
