package com.appstore.holder;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.util.TypedValue;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.appstore.R;
import com.appstore.domain.AppInfo;
import com.appstore.utils.UIutil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by stephen on 2016/11/20.
 */

public class DetailDesHolder extends BaseHolder<AppInfo> implements View.OnClickListener {

    @ViewInject(R.id.des_content)
    private TextView des_content;
    @ViewInject(R.id.des_author)
    private TextView des_author;
    @ViewInject(R.id.des_arrow)
    private ImageView des_arrow;
    @ViewInject(R.id.des_layout)
    private RelativeLayout des_layout;

    @Override
    public View initView() {
        View view=View.inflate(UIutil.getContext(),R.layout.detail_des,null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void refreshView(AppInfo data) {
        des_content.setText(data.getDes());
        des_author.setText("作者:"+data.getAuthor());
        des_layout.setOnClickListener(this);

        /**des_content 起始高度7行的高度*/
        ViewGroup.LayoutParams layoutParams = des_content.getLayoutParams();
        layoutParams.height=getShortMeasureHeight();
        des_content.setLayoutParams(layoutParams);
        des_arrow.setImageResource(R.drawable.arrow_down);

    }

    /**
     * 获取7行的高度
     * @return
     */
    private int getShortMeasureHeight() {
         /**复制一个新的TextView 用来测量,最好不要在之前的TextView测量 有可能影响其它代码执行*/
        TextView textView=new TextView(UIutil.getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
        textView.setMaxLines(7);
        textView.setLines(7);// 强制有7行
        int width=des_content.getMeasuredWidth(); // 开始宽度

        int widthMeasureSpec= MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int heightMeasureSpec= MeasureSpec.makeMeasureSpec(1000, MeasureSpec.AT_MOST);
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        return textView.getMeasuredHeight();
        /**measure(0,0) 等价于 measure(MeasureSpec.makeMeasureSpec(MeasureSpec.UNSPECIFIED,0));*/
    }

    /**
     * 获取TextView 自己本身的高度
     * @return
     */
    public int getLongMeasureHeight(){
        int width=des_content.getMeasuredWidth(); // 开始宽度
        des_content.getLayoutParams().height= ViewGroup.LayoutParams.WRAP_CONTENT;// 高度包裹内容


        int widthMeasureSpec= MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int heightMeasureSpec= MeasureSpec.makeMeasureSpec(1000, MeasureSpec.AT_MOST);
        des_content.measure(widthMeasureSpec,heightMeasureSpec);//
        return des_content.getMeasuredHeight();
    }

    boolean flag;// true展开了 false 没有展开
    @Override
    public void onClick(View v) {
        expand();
    }

    ScrollView scrollView;
    /**
     * 获取到界面的ScollView
     */
    private ScrollView getScrollView(View view){
        ViewParent parent = view.getParent();
        if(parent instanceof ViewGroup){
            ViewGroup group = (ViewGroup) parent;
            if(group instanceof ScrollView){
                return (ScrollView)group;
            }else{
                return getScrollView(group);
            }
        }else {
            return null;
        }
    }

    private void expand() {
        int startHeight;
        int targetHeight;
        if(!flag){
            flag=true;
            startHeight=getShortMeasureHeight();
            targetHeight=getLongMeasureHeight();
        }else{
            flag=false;
            startHeight=getLongMeasureHeight();
            targetHeight=getShortMeasureHeight();
        }

        scrollView = getScrollView(des_layout);

         final ViewGroup.LayoutParams layoutParams = des_content.getLayoutParams();
        ValueAnimator animator=ValueAnimator.ofInt(startHeight,targetHeight);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value=(Integer) animation.getAnimatedValue();
                layoutParams.height=value;
                des_content.setLayoutParams(layoutParams);
                scrollView.scrollTo(0,scrollView.getMeasuredHeight());
            }
        });
        animator.addListener(new Animator.AnimatorListener() {  // 监听动画执行
            //当动画开始执行的时候调用
            @Override
            public void onAnimationStart(Animator arg0) {
                // TODO Auto-generated method stub

            }
            @Override
            public void onAnimationRepeat(Animator arg0) {

            }
            @Override
            public void onAnimationEnd(Animator arg0) {
                if(flag){
                    des_arrow.setImageResource(R.drawable.arrow_up);
                }else{
                    des_arrow.setImageResource(R.drawable.arrow_down);
                }
            }
            @Override
            public void onAnimationCancel(Animator arg0) {

            }
        });
        animator.setDuration(500);//设置动画持续时间
        animator.start();
    }


}
