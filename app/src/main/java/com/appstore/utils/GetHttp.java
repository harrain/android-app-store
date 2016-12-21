package com.appstore.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetHttp {

    public static final String URI = "http://127.0.0.1:8090/";

    public static String RequstGetHttp(String strUrl) {
        URL url;
        String result = "";
        try {
            url = new URL(strUrl);
            HttpURLConnection urlconn = (HttpURLConnection) url.openConnection();
            urlconn.setRequestMethod("GET");// 链接服务器并发送消息
            urlconn.setConnectTimeout(10000);
            urlconn.setReadTimeout(15000);

            // 开始接收返回的数据
            InputStreamReader is = new InputStreamReader(urlconn.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(is);
            String readLine ;
            while ((readLine = bufferedReader.readLine()) != null) {
                result += readLine;
            }
            bufferedReader.close();
            is.close();
            urlconn.disconnect();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}