package com.appstore.holder;

import android.app.ActionBar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.appstore.R;
import com.appstore.utils.UIutil;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by stephen on 2016/11/15.
 */

public class HomePictureHolder extends BaseHolder<List<String>> {
    private ViewPager viewPager;
    private List<String> datas;

    private AutoRunTask autoRunTask;
    boolean flag;
    @Override
    public View initView() {
        viewPager = new ViewPager(UIutil.getContext());
        viewPager.setLayoutParams(new AbsListView.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                UIutil.getDimen(R.dimen.home_headItem_height)));
        return viewPager;
    }



    @Override
    public void refreshView(List<String> strings) {

        this.datas = strings;
        viewPager.setAdapter(new picAdapter());
        viewPager.setCurrentItem(Integer.MAX_VALUE / 2);

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        autoRunTask.stop();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        autoRunTask.start();
                        break;
                        
                }
                return false; // viewPager 触摸事件 返回值要是false
            }
        });

        autoRunTask = new AutoRunTask();
        autoRunTask.start();

    }

    class picAdapter extends PagerAdapter{
        // 当前viewPager里面有多少个条目
        LinkedList<ImageView> imageViews = new LinkedList<ImageView>();
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        /* 判断返回的对象和 加载view对象的关系 */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ImageView view = (ImageView) object;
            imageViews.add(view);// 把移除的对象 添加到缓存集合中
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int index = position % datas.size();
            ImageView imageView;
            if(imageViews.size() > 0){
                imageView = imageViews.remove(0);
            }else {
                imageView = new ImageView(UIutil.getContext());
            }
            bitmapUtils.display(imageView,"http://127.0.0.1:8090/image?name="+datas.get(index));
            container.addView(imageView);
            return imageView;
        }
    }

    /**自动轮询*/
    class AutoRunTask implements Runnable{

        @Override
        public void run() {
            if(flag){
                UIutil.cancel(this);
                int currentItem = viewPager.getCurrentItem();
                currentItem ++;
                viewPager.setCurrentItem(currentItem);
                UIutil.postDelayed(this,2000);
            }

        }

        public void start(){
            if(!flag){
                UIutil.cancel(this);  // 取消之前
                flag = true;
                UIutil.postDelayed(this, 2000);// 递归调用
            }
        }

        public void stop(){
            if(flag) {
                flag = false;
                UIutil.cancel(this);
            }
        }
    }
}
