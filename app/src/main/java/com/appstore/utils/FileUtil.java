package com.appstore.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by stephen on 2016/11/8.
 */

public class FileUtil {

    public static final String CACHE = "cache";
    public static final String ICON = "icon";
    public static final String DOWNLOAD = "download";
    public static final String ROOT = "AppStore";

    public static File getDir (String str){
        StringBuilder path = new StringBuilder();
        if(isSDAvailable()) {
            path.append(Environment.getExternalStorageDirectory().getAbsolutePath());
            path.append(File.separator);
            path.append(ROOT);
            path.append(File.separator);
            path.append(str);
        }else {
            File filesDir = UIutil.getContext().getCacheDir();
            path.append(filesDir.getAbsolutePath());
            path.append(File.separator);
            path.append(str);

        }
        LogUtils.d(path.toString());
            File file = new File(path.toString());
            if (!file.exists() || !file.isDirectory()) {
                file.mkdirs();
            }

        return file;
    }

    private static boolean isSDAvailable() {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            LogUtils.d("true");
            return true;
        }else{
            LogUtils.d("false");
            return false;
        }

    }

    public static File getCacheDir(){
        return getDir(CACHE);
    }
    public static File getIconDir(){
        return getDir(ICON);
    }

    public static File getDownloadDir() {
        return getDir(DOWNLOAD);
    }
}
