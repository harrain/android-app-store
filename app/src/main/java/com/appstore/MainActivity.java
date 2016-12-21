package com.appstore;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import com.appstore.fragment.BaseFragment;
import com.appstore.fragment.FragmentFactory;

import com.appstore.holder.MenuHolder;
import com.appstore.utils.UIutil;

public class MainActivity extends BaseActivity implements SearchView.OnQueryTextListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ViewPager mViewPager;
    private PagerTabStrip mPagerTabStrip;
    private String[] tabNames;
    private FrameLayout fl_menu;

    protected void initView() {

        setContentView(R.layout.activity_main);
        tabNames = UIutil.getStringArray(R.array.tab_names);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl);

        mViewPager = (ViewPager) findViewById(R.id.vp);
        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new MainAdapter(fm));

        mPagerTabStrip = (PagerTabStrip) findViewById(R.id.pager_tab_strip);
        mPagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.tab_indicator));



        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                BaseFragment createFragment = (BaseFragment) FragmentFactory.createFragment(position);
                createFragment.show();//  当切换界面的时候 重新请求服务器
            }
        });
        fl_menu = (FrameLayout) findViewById(R.id.fl_menu);
        MenuHolder menuHolder = new MenuHolder();
        fl_menu.addView(menuHolder.getContentView());

    }

    protected void initActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        /*actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);

        ActionBar.Tab tab = actionBar.newTab()
                .setText(R.string.entertain)
                .setTabListener(new MyTabListener());
        actionBar.addTab(tab);

        tab = actionBar.newTab()
                .setText(R.string.game)
                .setTabListener(new MyTabListener());
        actionBar.addTab(tab);

        tab = actionBar.newTab()
                .setText(R.string.chat)
                .setTabListener(new MyTabListener());
        actionBar.addTab(tab);

        tab = actionBar.newTab()
                .setText(R.string.life)
                .setTabListener(new MyTabListener());
        actionBar.addTab(tab);*/
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        mDrawerToggle =  new ActionBarDrawerToggle(
                this, mDrawerLayout, R.drawable.ic_drawer_am, R.string.open_drawer,
                R.string.close_drawer){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
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
            return tabNames.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabNames[position];
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.actions,menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.ic_search).getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.ic_search){
            Toast.makeText(this, "now search", Toast.LENGTH_SHORT).show();

        }
        return mDrawerToggle.onOptionsItemSelected(item)|super.onOptionsItemSelected(item);
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

    /*@Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.click_me:
                Intent detailIntent = new Intent(this,DetailActivity.class);
                startActivity(detailIntent);
                break;

            default: break;

        }
    }*/

    /*private class MyTabListener implements ActionBar.TabListener{

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            mViewPager.setCurrentItem(tab.getPosition());

        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {


        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }
    }*/
}
