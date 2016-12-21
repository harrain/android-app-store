package com.appstore.protocol;


import com.appstore.domain.AppInfo;
import com.appstore.utils.FileUtil;
import com.appstore.utils.GetHttp;
import com.appstore.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by stephen on 2016/11/8.
 */

public class HomeProtocol extends BaseProtocol<List<AppInfo>>{

    private List<String> pictures;

    @Override
    public String getKey() {
        return "home";
    }

    @Override
    public  List<AppInfo> parseJson(String json) {
        List<AppInfo> appInfos = new ArrayList<AppInfo>();

        pictures=new ArrayList<String>();


        try{
            JSONObject jsonObject=new JSONObject(json);
            JSONArray jsonArray2 = jsonObject.getJSONArray("picture");
            for(int i=0;i<jsonArray2.length();i++){
                String str=jsonArray2.getString(i);
                pictures.add(str);
            }

            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0;i < jsonArray.length();i++){
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                long id = jsonObj.getLong("id");
                String name = jsonObj.getString("name");
                String packageName=jsonObj.getString("packageName");
                String iconUrl = jsonObj.getString("iconUrl");
                float stars=Float.parseFloat(jsonObj.getString("stars"));
                long size=jsonObj.getLong("size");
                String downloadUrl = jsonObj.getString("downloadUrl");
                String des = jsonObj.getString("des");
                AppInfo info=new AppInfo(id, name, packageName, iconUrl, stars, size, downloadUrl, des);
                appInfos.add(info);
            }
            return appInfos;
        }catch(JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getPictures(){ return pictures;}

}
