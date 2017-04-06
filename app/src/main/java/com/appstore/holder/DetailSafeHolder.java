package com.appstore.holder;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appstore.R;
import com.appstore.domain.AppInfo;
import com.appstore.utils.BitmapUtil;
import com.appstore.utils.GetHttp;
import com.appstore.utils.UIutil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by stephen on 2016/11/19.
 */

public class DetailSafeHolder extends BaseHolder<AppInfo> implements View.OnClickListener {

    @ViewInject(R.id.safe_layout)
    private RelativeLayout safe_layout;

    @ViewInject(R.id.safe_content)
    private LinearLayout safe_content;

    @ViewInject(R.id.safe_arrow)
    private ImageView safe_arrow;

    private ImageView[] ivs,iv_des;
    private TextView[] tv_des;
    private LinearLayout[] des_layout;
    @Override
    public View initView() {
        View view = View.inflate(UIutil.getContext(),R.layout.detail_safe,null);
        ViewUtils.inject(this,view);

        ivs = new ImageView[4]; /** 初始化标题栏的图片*/
        ivs[0] = (ImageView) view.findViewById(R.id.iv_1);
        ivs[1] = (ImageView) view.findViewById(R.id.iv_2);
        ivs[2] = (ImageView) view.findViewById(R.id.iv_3);
        ivs[3] = (ImageView) view.findViewById(R.id.iv_4);

        iv_des = new ImageView[4];  /**初始化每个条目描述的图片*/
        iv_des[0] = (ImageView) view.findViewById(R.id.des_iv_1);
        iv_des[1] = (ImageView) view.findViewById(R.id.des_iv_2);
        iv_des[2] = (ImageView) view.findViewById(R.id.des_iv_3);
        iv_des[3] = (ImageView) view.findViewById(R.id.des_iv_4);

        tv_des = new TextView[4];  /**初始化每个条目描述的文本*/
        tv_des[0] = (TextView) view.findViewById(R.id.des_tv_1);
        tv_des[1] = (TextView) view.findViewById(R.id.des_tv_2);
        tv_des[2] = (TextView) view.findViewById(R.id.des_tv_3);
        tv_des[3] = (TextView) view.findViewById(R.id.des_tv_4);

        des_layout = new LinearLayout[4];  /**初始化条目线性布局*/
        des_layout[0] = (LinearLayout) view.findViewById(R.id.des_layout_1);
        des_layout[1] = (LinearLayout) view.findViewById(R.id.des_layout_2);
        des_layout[2] = (LinearLayout) view.findViewById(R.id.des_layout_3);
        des_layout[3] = (LinearLayout) view.findViewById(R.id.des_layout_4);

        ViewGroup.LayoutParams layoutParams = safe_content.getLayoutParams();
        layoutParams.height = 0;
        safe_content.setLayoutParams(layoutParams);
        safe_arrow.setImageResource(R.drawable.arrow_down);

        return view;
    }

    @Override
    public void refreshView(AppInfo data) {

        bitmapUtils = BitmapUtil.getBitmap(data.getPackageName());

        List<String> safeUrl = data.getSafeUrl();
        List<String> safeDesUrl = data.getSafeDesUrl();
        List<String> safeDes = data.getSafeDes();
        List<Integer> safeDesColor = data.getSafeDesColor(); // 0 1 2 3

        for (int i = 0; i < 4 ; i++) {
            if(i < safeUrl.size() && i < safeDesUrl.size()
                    && i < safeDes.size() && i < safeDesColor.size()){
                ivs[i].setVisibility(View.VISIBLE);
                des_layout[i].setVisibility(View.VISIBLE);
                bitmapUtils.display(ivs[i], GetHttp.URI+"image?name="+safeUrl.get(i));
                System.out.println(safeDesUrl.get(i));
                System.out.println(safeDes.get(i));
                bitmapUtils.display(iv_des[i],GetHttp.URI+"image?name="+safeDesUrl.get(i));
                tv_des[i].setText(safeDes.get(i));
                /** 根据服务器数据显示不同的颜色*/
                int color;
                int colorType = safeDesColor.get(i);
                if(colorType >= 1 && colorType <= 3){
                    color = Color.rgb(255, 153, 0); // 00 00 00
                } else if (colorType == 4) {
                    color = Color.rgb(0, 177, 62);
                } else {
                    color = Color.rgb(122, 122, 122);
                }
                tv_des[i].setTextColor(color);
            }else {
                ivs[i].setVisibility(View.GONE);
                des_layout[i].setVisibility(View.GONE);
            }

            safe_layout.setOnClickListener(this);

        }

    }

    boolean flag = false;
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.safe_layout){
            int startHeight,targetHeight;
            if(!flag){/**展开的动画**/
                startHeight = 0;
                targetHeight = getMeasureHeight();
                flag = true;
            }else {
                targetHeight = 0;
                startHeight = getMeasureHeight();
                flag = false;
            }
            /**值动画*/
            ValueAnimator animator = ValueAnimator.ofInt(startHeight,targetHeight);
            final RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) safe_content.getLayoutParams();
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                /**监听值的变化*/
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer value = (Integer) animation.getAnimatedValue();/**运行当前时间点的一个值*/
                    layoutParams.height = value;
                    safe_content.setLayoutParams(layoutParams);/**刷新界面*/
                }
            });
            animator.addListener(new Animator.AnimatorListener() {
                /**监听动画执行,当动画开始执行的时候调用*/


                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if(flag){
                        safe_arrow.setImageResource(R.drawable.arrow_up);
                    }else {
                        safe_arrow.setImageResource(R.drawable.arrow_down);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }

            });
            animator.setDuration(500);
            animator.start();
        }

    }

    private int getMeasureHeight() {
        int width = safe_content.getMeasuredWidth();/**由于宽度不会发生变化  宽度的值取出来*/
        safe_content.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        /**参数1  测量控件mode    参数2  大小*/
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(250, MeasureSpec.AT_MOST);
        /** 测量规则 宽度是一个精确的值width, 高度最大是1000,以实际为准*/
        safe_content.measure(widthMeasureSpec,heightMeasureSpec);
        return safe_content.getMeasuredHeight();
    }
}
