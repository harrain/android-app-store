package com.appstore.holder;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appstore.AboutActivity;
import com.appstore.BaseActivity;
import com.appstore.MainActivity;
import com.appstore.R;
import com.appstore.domain.UserInfo;
import com.appstore.protocol.UserProtocol;
import com.appstore.utils.GetHttp;
import com.appstore.utils.ThreadManager;
import com.appstore.utils.UIutil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by stephen on 2016/11/17.
 */

public class MenuHolder extends BaseHolder<UserInfo> implements View.OnClickListener{

    @ViewInject(R.id.photo_layout)
    private RelativeLayout photo_layout;

    @ViewInject(R.id.image_photo)
    private ImageView image_photo;

    @ViewInject(R.id.user_name)
    private TextView user_name;

    @ViewInject(R.id.user_email)
    private TextView user_email;

    @ViewInject(R.id.home_layout)
    private RelativeLayout homeLayout;

    @ViewInject(R.id.about_layout)
    private RelativeLayout aboutLayout;

    @ViewInject(R.id.exit_layout)
    private RelativeLayout exitLayout;

    @Override
    public View initView() {
        View view = View.inflate(UIutil.getContext(),R.layout.menu_holder,null);
        ViewUtils.inject(this,view);

        photo_layout.setOnClickListener(this);
        homeLayout.setOnClickListener(this);
        aboutLayout.setOnClickListener(this);
        exitLayout.setOnClickListener(this);
        return view;
    }

    @Override
    public void refreshView(UserInfo userInfo) {
        user_name.setText(userInfo.getName());
        user_email.setText(userInfo.getEmail());
        String uri = userInfo.getUrl();
        bitmapUtils.display(image_photo, GetHttp.URI+"image?name="+uri);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.photo_layout:
                requestServerForLogIn();
                break;
            case R.id.home_layout:
                UIutil.startActivity(new Intent(UIutil.getContext(), MainActivity.class));
                break;
            case R.id.exit_layout:
                BaseActivity.killAll();
                break;
            case R.id.about_layout:
                UIutil.startActivity(new Intent(UIutil.getContext(), AboutActivity.class));
                break;
            default:break;
        }
    }

    private void requestServerForLogIn() {
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                UserProtocol protocol = new UserProtocol();
                final UserInfo load = protocol.load(0);

                UIutil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setData(load);
                    }
                });

            }
        });
    }
}
