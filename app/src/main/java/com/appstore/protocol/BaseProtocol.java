package com.appstore.protocol;


import com.appstore.utils.FileUtil;
import com.appstore.utils.GetHttp;
import com.appstore.utils.LogUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by stephen on 2016/11/11.
 */

public abstract class BaseProtocol <Data>{

    String str;

    public Data load(int index){
        //String json = null;
        String json = loadLocal(index);
        if(json == null){
            json = loadServer(index);
            //LogUtils.d(json);
            if(json != null){
                saveLocal(json,index);
            }
        }else{
            LogUtils.d("复用了本地缓存l1");
        }
        if(json != null){
            return parseJson(json);
        }else {
            return null;
        }
    }

    private String loadLocal(int index) {
        String dir = FileUtil.getCacheDir().toString();
        File file = new File(dir,getKey()+"_"+index+getParams());
        StringWriter sw ;
        String s;
        try{
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            long outOfDate = Long.parseLong(br.readLine());
            long time = System.currentTimeMillis();
            LogUtils.d(Long.toString(outOfDate));
            LogUtils.d(Long.toString(time));
            if(time > outOfDate){
                return null;
            }else{
                String str ;
                sw = new StringWriter();//写入内存流
                while((str = br.readLine())!= null){
                    sw.write(str);
                }
                s = sw.toString();
                LogUtils.d("读本地："+s.toString());
                sw.close();
                br.close();
                fr.close();
                return s;
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
            return null;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }

    }

    private String loadServer(int index) {

        str = GetHttp.RequstGetHttp("http://127.0.0.1:8090/"+getKey()+"?index="+index+getParams());
        if (str != null){

            return str;
        }else {
            return null;
        }
    }

    private void saveLocal(String json, int index) {
        LogUtils.d("保存本地"+json);
        BufferedWriter bw ;
        try{
            String dir = FileUtil.getCacheDir().toString();
            File file = new File(dir,getKey()+"_"+index+getParams());
            FileWriter fw = new FileWriter(file);
            bw =new BufferedWriter(fw);
            bw.write(Long.toString(System.currentTimeMillis() + 1000 * 100));
            bw.newLine();
            bw.write(json);
            bw.flush();
            bw.close();
            fw.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 额外带的参数
     * @return
     */
    protected String getParams() {
        return "";
    }

    /**
     * 说明了关键字
     *
     * @return
     */
    public abstract String getKey();
    /**
     * 说明了关键字
     *
     * @return
     */
    public abstract Data parseJson(String json);
}
