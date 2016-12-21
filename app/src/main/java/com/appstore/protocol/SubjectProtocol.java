package com.appstore.protocol;

import com.appstore.domain.SubjectInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stephen on 2016/11/11.
 */

public class SubjectProtocol extends BaseProtocol<List<SubjectInfo>>{
    @Override
    public String getKey() {
        return "subject";
    }

    @Override
    public List<SubjectInfo> parseJson(String json) {
        List<SubjectInfo> subjectList = new ArrayList<SubjectInfo>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                String des = jsonObj.getString("des");
                String url = jsonObj.getString("url");
                SubjectInfo subjectInfo = new SubjectInfo(des,url);
                subjectList.add(subjectInfo);
            }
            return subjectList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
