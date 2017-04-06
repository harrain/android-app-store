package com.appstore.holder;

import android.view.View;
import android.widget.ImageView;

import com.appstore.R;
import com.appstore.domain.AppInfo;
import com.appstore.utils.BitmapUtil;
import com.appstore.utils.GetHttp;
import com.appstore.utils.LogUtils;
import com.appstore.utils.UIutil;

import java.util.List;

/**
 * Created by stephen on 2016/11/18.
 */

public class DetailScreenHolder extends BaseHolder<AppInfo> {

    private ImageView[] ivs;
    @Override
    public View initView() {
        View view= View.inflate(UIutil.getContext(),R.layout.detail_screen,null);
        ivs=new ImageView[5];
        ivs[0]=(ImageView) view.findViewById(R.id.screen_1);
        ivs[1]=(ImageView) view.findViewById(R.id.screen_2);
        ivs[2]=(ImageView) view.findViewById(R.id.screen_3);
        ivs[3]=(ImageView) view.findViewById(R.id.screen_4);
        ivs[4]=(ImageView) view.findViewById(R.id.screen_5);
        return view;
    }

    @Override
    public void refreshView(AppInfo data) {

        bitmapUtils = BitmapUtil.getBitmap(data.getPackageName());

        List<String> screen = data.getScreen(); // 集合的大小有可能小于5
        for(int i=0;i<5;i++){
            if(i<screen.size()){
                ivs[i].setVisibility(View.VISIBLE);
                bitmapUtils.display(ivs[i], GetHttp.URI+"image?name="+screen.get(i));
                System.out.println(screen.get(i));
            }else{
                ivs[i].setVisibility(View.GONE);
            }

        }
    }
}
