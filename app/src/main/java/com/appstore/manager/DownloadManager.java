package com.appstore.manager;

import android.util.Log;

import com.appstore.domain.AppInfo;
import com.appstore.utils.CommonUtils;
import com.appstore.utils.FileUtil;
import com.appstore.utils.GetHttp;
import com.appstore.utils.LogUtils;
import com.appstore.utils.ThreadManager;
import com.appstore.utils.UIutil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



/**
 * Created by stephen on 2017/2/8.
 */
public class DownloadManager {

    public static final int				STATE_UNDOWNLOAD		= 0;									// 未下载
    public static final int				STATE_DOWNLOADING		= 1;									// 下载中
    public static final int				STATE_PAUSEDOWNLOAD		= 2;									// 暂停下载
    public static final int				STATE_WAITINGDOWNLOAD	= 3;									// 等待下载
    public static final int				STATE_DOWNLOADFAILED	= 4;									// 下载失败
    public static final int				STATE_DOWNLOADED		= 5;									// 下载完成
    public static final int				STATE_INSTALLED			= 6;									// 已安装

    private static DownloadManager instance;

    // 记录正在下载的一些downLoadInfo
    public Map<String, DownLoadInfo> mDownLoadInfoMaps		= new HashMap<String, DownLoadInfo>();

    public static DownloadManager getInstance() {
        if (instance == null) {
            synchronized (DownloadManager.class) {
                if (instance == null) {
                    instance = new DownloadManager();
                }
            }
        }
        return instance;
    }

    private DownloadManager() {
    }

    public void download(DownLoadInfo info) {
        mDownLoadInfoMaps.put(info.packageName, info);

		/*############### 当前状态: 未下载 ###############*/
        info.state = STATE_UNDOWNLOAD;
        notifyObservers(info);
		/*#######################################*/

		/*############### 当前状态: 等待状态 ###############*/
        info.state = STATE_WAITINGDOWNLOAD;
        notifyObservers(info);
		/*#######################################*/
        // 得到线程池,执行任务
        DownloadTask task = new DownloadTask(info);
        info.task = task;// downInfo身上的task赋值
        ThreadManager.getInstance().createShortPool().execute(new DownloadTask(info));

    }

    /**暂停下载*/
    public void pause(DownLoadInfo info) {
		/*############### 当前状态: 暂停 ###############*/
        info.state = STATE_PAUSEDOWNLOAD;
        notifyObservers(info);
		/*#######################################*/
    }

    /**取消下载*/
    public void cancel(DownLoadInfo info) {
        Runnable task = info.task;
        // 找到线程池,移除任务
        ThreadManager.getInstance().createShortPool().cancel(task);

		/*############### 当前状态: 未下载 ###############*/
        info.state = STATE_UNDOWNLOAD;
        notifyObservers(info);
		/*#######################################*/
    }

    /**
     * @des 暴露当前状态,也就是需要提供downLoadInfo
     * @call 外界需要知道最新的state的时候
     */
    public DownLoadInfo getDownLoadInfo(AppInfo data) {

        // 已安装
        if (CommonUtils.isInstalled(UIutil.getContext(), data.getPackageName())) {
            DownLoadInfo info = generateDownLoadInfo(data);
            info.state = STATE_INSTALLED;// 已安装
            return info;
        }
        // 下载完成
        DownLoadInfo info = generateDownLoadInfo(data);
        File saveApk = new File(info.savePath);
        if (saveApk.exists()) {// 如果存在我们的下载目录里面
            if (saveApk.length() == data.getSize()) {
                info.state = STATE_DOWNLOADED;// 下载完成
                return info;
            }
        }

        /**
         下载中
         暂停下载
         等待下载
         下载失败
         */
        DownLoadInfo downLoadInfo = mDownLoadInfoMaps.get(data.getPackageName());
        if (downLoadInfo != null) {
            return downLoadInfo;
        }

        // 未下载
        DownLoadInfo tempInfo = generateDownLoadInfo(data);
        tempInfo.state = STATE_UNDOWNLOAD;// 未下载
        return tempInfo;
    }

    /**
     * 根据AppInfoBean生成一个DownLoadInfo,进行一些常规的赋值,也就是对一些常规属性赋值(除了state之外的属性)
     */
    public DownLoadInfo generateDownLoadInfo(AppInfo data) {
        String dir = FileUtil.getDir("download").toString();// sdcard/android/data/包名/download
        File file = new File(dir, data.getPackageName() + ".apk");// sdcard/android/data/包名/download/com.itheima.www.apk
        // 保存路径
        String savePath = file.getAbsolutePath();// sdcard/android/data/包名/download/com.itheima.www.apk

        // 初始化一个downLoadInfo
        DownLoadInfo info = new DownLoadInfo();
        // 相关赋值
        info.savePath = savePath;
        info.downloadUrl = data.getDownloadUrl();
        info.packageName = data.getPackageName();
        info.max = data.getSize();
        info.curProgress = 0;
        return info;
    }

    private class DownloadTask implements Runnable {
        private DownLoadInfo info;
        public DownloadTask(DownLoadInfo infos){
            info = infos;
        }
        @Override
        public void run() {

            try {
                /*############### 当前状态: 下载中 ###############*/
                info.state = STATE_DOWNLOADING;
                notifyObservers(info);
				/*#######################################*/

                long initRange = 0;
                File saveApk = new File(info.savePath);
                if (saveApk.exists()) {
                    initRange = saveApk.length();// 未下载完成的apk已有的长度
                }
                info.curProgress = initRange;// ③处理初始进度

                String url = GetHttp.URI + "download?name="+info.getDownloadUrl()+"&range="+ initRange + "";

                URL urls;
                urls = new URL(url);
                HttpURLConnection urlconn = (HttpURLConnection) urls.openConnection();
                urlconn.setRequestMethod("GET");// 链接服务器并发送消息
                urlconn.setConnectTimeout(10000);
                urlconn.setReadTimeout(15000);
                InputStream in = urlconn.getInputStream();
                LogUtils.i(String.valueOf(urlconn.getResponseCode()));
                if (urlconn.getResponseCode() == 200) {

                    FileOutputStream out = null;
                    boolean isPause = false;
                    try {

                        File saveFile = new File(info.savePath);
                        out = new FileOutputStream(saveFile,true);//从已有基础上继续写入

                        byte[] buffer = new byte[1024];
                        int len = -1;
                        while ((len = in.read(buffer)) != -1) {
                            if (info.state == STATE_PAUSEDOWNLOAD) {
                                isPause = true;
                                break;
                            }
                            out.write(buffer, 0, len);

                            info.curProgress += len;
							/*############### 当前状态: 下载中 ###############*/
                            info.state = STATE_DOWNLOADING;
                            notifyObservers(info);
							/*#######################################*/
                        }
                        if (isPause) {// 用户暂停了下载走到这里来了
							/*############### 当前状态: 暂停 ###############*/
                            info.state = STATE_PAUSEDOWNLOAD;
                            notifyObservers(info);
							/*#######################################*/
                        } else {// 下载完成走到这里来
							/*############### 当前状态: 下载完成 ###############*/
                            info.state = STATE_DOWNLOADED;
                            notifyObservers(info);
							/*#######################################*/

                        }
                    }finally {
                        out.close();
                        in.close();
                    }

                }else {
                    /*############### 当前状态: 下载失败 ###############*/
                    info.state = STATE_DOWNLOADFAILED;
                    notifyObservers(info);
					/*#######################################*/
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                /*############### 当前状态: 下载失败 ###############*/
                info.state = STATE_DOWNLOADFAILED;
                notifyObservers(info);
					/*#######################################*/
            } catch (IOException e) {
                e.printStackTrace();
                /*############### 当前状态: 下载失败 ###############*/
                info.state = STATE_DOWNLOADFAILED;
                notifyObservers(info);
					/*#######################################*/
            }

        }
    }

    /*=============== 自定义观察者设计模式  begin ===============*/
    public interface DownLoadObserver {
        void onDownLoadInfoChange(DownLoadInfo info);
    }

    List<DownLoadObserver> downLoadObservers	= new LinkedList<DownLoadObserver>();

    /**添加观察者*/
    public void addObserver(DownLoadObserver observer) {
        if (observer == null) {
            throw new NullPointerException("observer == null");
        }
        synchronized (this) {
            if (!downLoadObservers.contains(observer))
                downLoadObservers.add(observer);
        }
    }

    /**删除观察者*/
    public synchronized void deleteObserver(DownLoadObserver observer) {
        downLoadObservers.remove(observer);
    }

    /**通知观察者数据改变*/
    public void notifyObservers(DownLoadInfo info) {
        for (DownLoadObserver observer : downLoadObservers) {
            observer.onDownLoadInfoChange(info);
        }
    }

	/*=============== 自定义观察者设计模式  end ===============*/
}
