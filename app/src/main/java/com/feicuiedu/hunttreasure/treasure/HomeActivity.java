package com.feicuiedu.hunttreasure.treasure;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.feicuiedu.hunttreasure.MainActivity;
import com.feicuiedu.hunttreasure.R;
import com.feicuiedu.hunttreasure.commons.ActivityUtils;
import com.feicuiedu.hunttreasure.user.UserPrefs;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.navigation)
    NavigationView mNavigation;
    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    private ImageView mIvIcon;

    private ActivityUtils mActivityUtils;

    /**
     * 1. 处理侧滑
     * 2. 搭建宝藏页面：百度地图的集成和基本功能实现
     */
    /**
     * 侧滑：
     * 1. 布局：删除布局，重新进行搭建，展示效果
     * 2. 代码处理
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);

        mActivityUtils = new ActivityUtils(this);

        TreasureRepo.getInstance().clear();


         // 1.处理toolbar
        setSupportActionBar(mToolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // 2. 设置DrawerLayout的监听
        /**
         1.改变android.R.id.home返回图标。
         2.Drawer拉出、隐藏，带有android.R.id.home动画效果。
         3.监听Drawer拉出、隐藏
         */
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        toggle.syncState();// 同步状态：左上角显示的图标和当前的抽屉展开和关闭同步
        mDrawerLayout.addDrawerListener(toggle);

        // 3. 设置Navigation的监听
        mNavigation.setNavigationItemSelectedListener(this);
        mIvIcon = (ImageView) mNavigation.getHeaderView(0).findViewById(R.id.iv_userIcon);
        mIvIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 17/1/2 更新头像
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        String photo = UserPrefs.getInstance().getPhoto();
        if (photo!=null){
            Log.i("start","photo"+photo);
            Glide.with(this)
                    .load(photo)
                    .error(getDrawable(R.mipmap.user_icon))
                    .placeholder(getDrawable(R.mipmap.user_icon))
                    .into(mIvIcon);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_hide:
                break;
            case R.id.menu_logout:// 退出登录
                UserPrefs.getInstance();
                mActivityUtils.startActivity(MainActivity.class);
                finish();
                break;
        }
        // 无论选择什么抽屉都会关闭
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;// 返回true，表示被消费，被选中
    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }

    }
}
