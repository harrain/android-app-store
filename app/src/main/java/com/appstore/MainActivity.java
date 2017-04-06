package com.appstore;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.SearchView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appstore.domain.UserInfo;
import com.appstore.fragment.BaseFragment;
import com.appstore.fragment.FragmentFactory;

import com.appstore.protocol.UserProtocol;
import com.appstore.utils.BitmapUtil;
import com.appstore.utils.GetHttp;
import com.appstore.utils.ThreadManager;
import com.appstore.utils.UIutil;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;


public class MainActivity extends BaseActivity implements SearchView.OnQueryTextListener, NavigationView.OnNavigationItemSelectedListener {

    @ViewInject(R.id.image_photo)
    private ImageView image_photo;

    @ViewInject(R.id.user_name)
    private TextView user_name;

    @ViewInject(R.id.user_email)
    private TextView user_email;

    BitmapUtils bitmapUtils;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ViewPager mViewPager;
    private PagerTabStrip pager_tab_strip;
    private String[] tab_names;
    private TabLayout mTabLayout;

    protected void initView() {

        setContentView(R.layout.activity_main);
        tab_names = UIutil.getStringArray(R.array.tab_names);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        for (int i = 0; i < tab_names.length; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(tab_names[i]));
        }

        mViewPager = (ViewPager) findViewById(R.id.vp);
        FragmentManager fm = getSupportFragmentManager();
        MainAdapter mainAdapter = new MainAdapter(fm);
        mViewPager.setAdapter(mainAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(mainAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                BaseFragment createFragment = (BaseFragment) FragmentFactory.createFragment(position);
                createFragment.show();//  当切换界面的时候 重新请求服务器
            }
        });

    }

    protected void initActionBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer_am);
        }

        mDrawerLayout= (DrawerLayout) findViewById(R.id.dl);

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        ViewUtils.inject(this);


    }


    private class MainAdapter extends FragmentStatePagerAdapter {

        public MainAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            /*switch(position){
                case 0 : return new HomeFragment();
                case 1 : return new AppFragment();
                case 2 : return new GameFragment();
                case 3 : return new SubjectFragment();
                case 4 : return new CategoryFragment();
                case 5 : return new TopFragment();
                default : return null;
            }*/
            return FragmentFactory.createFragment(position);
        }

        @Override
        public int getCount() {
            return tab_names.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tab_names[position];
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.actions,menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.ic_search).getActionView();
        searchView.setOnQueryTextListener(this);
        return true;//返回TRUE
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.ic_search:
                Toast.makeText(this, "now search", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;//返回TRUE
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.photo_layout:
                requestServerForLogIn();
            case R.id.home_layout:{
                Intent intent = new Intent(UIutil.getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                UIutil.startActivity(intent);
            }
                break;
            case R.id.about_layout:
                UIutil.startActivity(new Intent(UIutil.getContext(), AboutActivity.class));
                break;
            case R.id.exit_layout:
                BaseActivity.killAll();

                break;
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(this,query,Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Toast.makeText(this,newText,Toast.LENGTH_SHORT).show();;
        return true;
    }

    private void requestServerForLogIn() {
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                UserProtocol protocol = new UserProtocol();
                final UserInfo userInfo = protocol.load(0);

                UIutil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        user_name.setText(userInfo.getName());
                        user_email.setText(userInfo.getEmail());
                        String uri = userInfo.getUrl();

                        bitmapUtils = BitmapUtil.getBitmap("menu");

                        bitmapUtils.display(image_photo, GetHttp.URI+"image?name="+uri);
                    }
                });

            }
        });
    }

}
