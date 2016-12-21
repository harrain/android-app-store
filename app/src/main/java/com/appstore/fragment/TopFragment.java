package com.appstore.fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.appstore.R;
import com.appstore.protocol.HotProtocol;
import com.appstore.utils.DrawableUtil;
import com.appstore.utils.FlowLayout;
import com.appstore.utils.UIutil;

import java.util.List;
import java.util.Random;

import static com.appstore.utils.UIutil.getContext;


public class TopFragment extends BaseFragment {

    private List<String> datas;

    @Override
    public LoadingPage.LoadResult load() {
        HotProtocol protocol=new HotProtocol();
        datas = protocol.load(0);
        return checkData(datas);
    }

    @Override
    public View createSuccessView() {
        ScrollView scrollView = new ScrollView(getContext());
        scrollView.setBackgroundResource(R.drawable.grid_item_bg_normal);

        FlowLayout layout= new FlowLayout(getContext());
        int padding=UIutil.dip2px(13);
        layout.setPadding(padding, padding, padding, padding);
        //layout.setOrientation(LinearLayout.VERTICAL);

        int backColor = 0xffcecece;
        Drawable pressedDrawable = DrawableUtil.createShape(backColor);
        for (int i = 0; i < datas.size(); i++) {
            TextView textView = new TextView(getContext());
            final String s = datas.get(i);
            textView.setText(s);

            Random random=new Random();   //创建随机
            int red = random.nextInt(220)+20;
            int green = random.nextInt(220)+20;
            int blue = random.nextInt(220)+20;
            int color = Color.rgb(red, green, blue);//范围 0-255
            GradientDrawable createShape = DrawableUtil.createShape(color);// 默认显示的图片
            StateListDrawable selectorDrawable = DrawableUtil.createSelectorDrawable(pressedDrawable, createShape);

            textView.setBackgroundDrawable(selectorDrawable);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(18);
            textView.setGravity(Gravity.CENTER);
            int textPaddingV = UIutil.dip2px(4);
            int textPaddingH = UIutil.dip2px(7);
            textView.setPadding(textPaddingH,textPaddingV,textPaddingH,textPaddingV);
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();
                }
            });

            layout.addView(textView,new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,-2));// -2 包裹内容

        }

        scrollView.addView(layout);

        return scrollView;
    }


}
