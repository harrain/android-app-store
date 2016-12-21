package com.appstore.protocol;

import com.appstore.domain.AppInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stephen on 2016/11/18.
 */

public class DetailProtocol extends BaseProtocol<AppInfo> {

    private String packageName;

    public DetailProtocol(String packageName){
       this.packageName = packageName;
    }

    @Override
    public String getKey() {
        return "detail";
    }

    /*long id, String name, String packageName, String iconUrl,
    float stars, long size, String downloadUrl, String des,
    String downloadNum, String version, String date, String author,
    List<String> screen, List<String> safeUrl, List<String> safeDesUrl,
    List<String> safeDes, List<Integer> safeDesColor*/
    @Override
    public AppInfo parseJson(String json) {
        try {
            JSONObject jsonObj = new JSONObject(json);
            long id = jsonObj.getLong("id");
            String name = jsonObj.getString("name");
            String packageName = jsonObj.getString("packageName");
            String iconUrl = jsonObj.getString("iconUrl");
            float stars = Float.parseFloat(jsonObj.getString("stars"));
            long size = jsonObj.getLong("size");
            String downloadUrl = jsonObj.getString("downloadUrl");
            String des = jsonObj.getString("des");
            String downloadNum = jsonObj.getString("downloadNum");
            String version = jsonObj.getString("version");
            String date = jsonObj.getString("date");
            String author = jsonObj.getString("author");

            List<String> screen,safeUrl,safeDesUrl,safeDes;
            screen = new ArrayList<String>();
            JSONArray jsonArray = jsonObj.getJSONArray("screen");
            for (int i = 0; i < jsonArray.length(); i++) {
                String string = jsonArray.getString(i);
                screen.add(string);
            }

            safeUrl = new ArrayList<String>();
            safeDesUrl = new ArrayList<String>();
            safeDes = new ArrayList<String>();
            List<Integer> safeDesColor = new ArrayList<Integer>();
            JSONArray array = jsonObj.getJSONArray("safe");
            for(int i = 0;i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                String Url = object.getString("safeUrl");
                String DesUrl = object.getString("safeDesUrl");
                String Des = object.getString("safeDes");
                Integer DesColor = object.getInt("safeDesColor");
                safeUrl.add(Url);
                safeDesUrl.add(DesUrl);
                safeDes.add(Des);
                safeDesColor.add(DesColor);
            }

            AppInfo appInfo = new AppInfo(id, name, packageName, iconUrl,
                    stars, size, downloadUrl, des, downloadNum, version, date,
                    author, screen, safeUrl, safeDesUrl, safeDes, safeDesColor);
            return appInfo;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected String getParams() {
        return "&packageName="+packageName;
    }
}
