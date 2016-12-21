package com.appstore.protocol;

import com.appstore.domain.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by stephen on 2016/11/17.
 */

public class UserProtocol extends BaseProtocol<UserInfo>{
    @Override
    public String getKey() {
        return "user";
    }

    @Override
    public UserInfo parseJson(String json) {

            //"{name:'传智黄盖',email:'huanggai@itcast.cn',url:'image/user.png'}"
            try {
                JSONObject jsonObject=new JSONObject(json);
                String name=jsonObject.getString("name");
                String email=jsonObject.getString("email");
                String url=jsonObject.getString("url");
                UserInfo userInfo=new UserInfo(name, url, email);
                return userInfo;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

    }
}
