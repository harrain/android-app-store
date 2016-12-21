package com.appstore.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appstore.R;
import com.appstore.domain.CategoryInfo;
import com.appstore.utils.GetHttp;
import com.appstore.utils.UIutil;

/**
 * Created by stephen on 2016/11/21.
 */

public class CategoryContentHolder extends BaseHolder<CategoryInfo> {

    ImageView[] ivs;
    TextView[] tvs;
    @Override
    public View initView() {
        View view= View.inflate(UIutil.getContext(),R.layout.item_category_content,null);
        ivs=new ImageView[3];
        ivs[0]=(ImageView) view.findViewById(R.id.iv_1);
        ivs[1]=(ImageView) view.findViewById(R.id.iv_2);
        ivs[2]=(ImageView) view.findViewById(R.id.iv_3);

        tvs=new TextView[3];
        tvs[0]=(TextView) view.findViewById(R.id.tv_1);
        tvs[1]=(TextView) view.findViewById(R.id.tv_2);
        tvs[2]=(TextView) view.findViewById(R.id.tv_3);


        return view;
    }

    @Override
    public void refreshView(CategoryInfo data) {
        // 第一块
        if(!TextUtils.isEmpty(data.getName1())&&!TextUtils.isEmpty(data.getUrl1())){
            tvs[0].setText(data.getName1());
            bitmapUtils.display(ivs[0], GetHttp.URI+"image?name="+data.getUrl1());
            tvs[0].setVisibility(View.VISIBLE);
            ivs[0].setVisibility(View.VISIBLE);
        }else{
            tvs[0].setVisibility(View.INVISIBLE);
            ivs[0].setVisibility(View.INVISIBLE);
        }
        // 第二块
        if(!TextUtils.isEmpty(data.getName2())&&!TextUtils.isEmpty(data.getUrl2())){
            tvs[1].setText(data.getName2());
            bitmapUtils.display(ivs[1], GetHttp.URI+"image?name="+data.getUrl2());
            tvs[1].setVisibility(View.VISIBLE);
            ivs[1].setVisibility(View.VISIBLE);
        }else{
            tvs[1].setVisibility(View.INVISIBLE);
            ivs[1].setVisibility(View.INVISIBLE);
        }
        //第三块
        if(!TextUtils.isEmpty(data.getName3())&&!TextUtils.isEmpty(data.getUrl3())){
            tvs[2].setText(data.getName3());
            bitmapUtils.display(ivs[2], GetHttp.URI+"image?name="+data.getUrl3());
            tvs[2].setVisibility(View.VISIBLE);
            ivs[2].setVisibility(View.VISIBLE);
        }else{
            tvs[2].setVisibility(View.INVISIBLE);
            ivs[2].setVisibility(View.INVISIBLE);
        }
    }
}
