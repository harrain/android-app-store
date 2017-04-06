package com.appstore.fragment;

import android.support.v4.app.Fragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by stephen on 2016/10/30.
 */

public class FragmentFactory {
    private final static Map<Integer,Fragment> mFragmentFactory = new HashMap<>();

    public static Fragment createFragment(int position){
        Fragment fragment = null;
        fragment = mFragmentFactory.get(position);
        if(fragment == null){
            switch(position){
                case 0 : fragment = new HomeFragment(); break;
                case 1 : fragment = new AppFragment();  break;
                case 2 : fragment = new GameFragment(); break;
                case 3 : fragment = new SubjectFragment(); break;
                case 4 : fragment = new CategoryFragment(); break;
                case 5 : fragment = new TopFragment();   break;
                default : break;
            }
            if (fragment != null) {
                mFragmentFactory.put(position, fragment);// 把创建好的Fragment存放到集合中缓存起来
            }
        }

        return fragment;
    }
}
