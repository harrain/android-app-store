package com.appstore.protocol;

import com.appstore.domain.CategoryInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stephen on 2016/11/22.
 */

public class CategoryProtocol extends BaseProtocol<List<CategoryInfo>> {
    @Override
    public String getKey() {
        return "category";
    }

    @Override
    public List<CategoryInfo> parseJson(String json) {
        List<CategoryInfo> categoryInfos=new ArrayList<CategoryInfo>();
        try {
            JSONArray array=new JSONArray(json);
            for(int i=0;i<array.length();i++){
                JSONObject jsonObject = array.getJSONObject(i);
                String title=jsonObject.getString("title");
                CategoryInfo categoryInfo=new CategoryInfo();
                categoryInfo.setTitle(title);
                categoryInfo.setIsTitle(true);
                categoryInfos.add(categoryInfo);

                JSONArray jsonArray = jsonObject.getJSONArray("infos");
                for(int j=0;j<jsonArray.length();j++){
                    JSONObject jsonObject2 = jsonArray.getJSONObject(j);
                    String url1=jsonObject2.getString("url1");
                    String url2=jsonObject2.getString("url2");
                    String url3=jsonObject2.getString("url3");
                    String name1=jsonObject2.getString("name1");
                    String name2=jsonObject2.getString("name2");
                    String name3=jsonObject2.getString("name3");
                    CategoryInfo categoryInfo2=new CategoryInfo(title, url1, url2, url3, name1, name2, name3,false);
                    categoryInfos.add(categoryInfo2);
                }

            }
            return categoryInfos;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
