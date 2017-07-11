package com.openwudi.animal.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.openwudi.animal.R;
import com.openwudi.animal.base.BaseActivity;
import com.openwudi.animal.base.StatusBarCompat;
import com.openwudi.animal.fragment.HomeFragment;
import com.openwudi.animal.fragment.MyFragment;
import com.openwudi.animal.view.MainTabViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by diwu on 17/7/11.
 */

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final int TAB_HOME = 0;
    private static final int TAB_READ = 1;
    private static final int TAB_MY = 2;
    @BindView(R.id.main_vp)
    MainTabViewPager mainVp;
    @BindView(R.id.tab_home_iv)
    ImageView tabHomeIv;
    @BindView(R.id.tab_home_tv)
    TextView tabHomeTv;
    @BindView(R.id.tab_home)
    LinearLayout tabHome;
    @BindView(R.id.tab_read_iv)
    ImageView tabReadIv;
    @BindView(R.id.tab_read_tv)
    TextView tabReadTv;
    @BindView(R.id.tab_read)
    LinearLayout tabRead;
    @BindView(R.id.tab_my_iv)
    ImageView tabMyIv;
    @BindView(R.id.tab_my_tv)
    TextView tabMyTv;
    @BindView(R.id.tab_my)
    LinearLayout tabMy;
    @BindView(R.id.root)
    FrameLayout root;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        mainVp.setPagingEnabled(false);
        mainVp.setAdapter(new MainTabsAdapter(getSupportFragmentManager()));
        mainVp.setOffscreenPageLimit(3);

        tabHome.setOnClickListener(this);
        tabRead.setOnClickListener(this);
        tabMy.setOnClickListener(this);

        selectMainTab(TAB_HOME);
        mainVp.setCurrentItem(TAB_HOME, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_home:
                selectMainTab(TAB_HOME);
                mainVp.setCurrentItem(TAB_HOME, false);
                break;
            case R.id.tab_read:
                selectMainTab(TAB_READ);
                mainVp.setCurrentItem(TAB_READ, false);
                break;
            case R.id.tab_my:
                selectMainTab(TAB_MY);
                mainVp.setCurrentItem(TAB_MY, false);
                break;
        }
    }

    private void selectMainTab(int id) {
        Drawable homeDrawable = ContextCompat.getDrawable(this, R.drawable.icons);
        Drawable helpDrawable = ContextCompat.getDrawable(this, R.drawable.icon_msg);
        Drawable myDrawable = ContextCompat.getDrawable(this, R.drawable.settings);
        int homeTextColor = ContextCompat.getColor(this, R.color.main_tab_text_normal);
        int helpTextColor = ContextCompat.getColor(this, R.color.main_tab_text_normal);
        int myTextColor = ContextCompat.getColor(this, R.color.main_tab_text_normal);

        switch (id) {
            case TAB_HOME:
                homeDrawable = ContextCompat.getDrawable(this, R.drawable.icons);
                homeTextColor = ContextCompat.getColor(this, R.color.color6);
                break;
            case TAB_READ:
                helpDrawable = ContextCompat.getDrawable(this, R.drawable.icon_msg);
                helpTextColor = ContextCompat.getColor(this, R.color.color6);
                break;
            case TAB_MY:
                myDrawable = ContextCompat.getDrawable(this, R.drawable.settings);
                myTextColor = ContextCompat.getColor(this, R.color.color6);
                break;
            default:
                homeDrawable = ContextCompat.getDrawable(this, R.drawable.icons);
                homeTextColor = ContextCompat.getColor(this, R.color.color6);
                break;
        }
        tabHomeIv.setImageDrawable(homeDrawable);
        tabHomeTv.setTextColor(homeTextColor);
        tabReadIv.setImageDrawable(helpDrawable);
        tabReadTv.setTextColor(helpTextColor);
        tabMyIv.setImageDrawable(myDrawable);
        tabMyTv.setTextColor(myTextColor);
    }

    private class MainTabsAdapter extends FragmentPagerAdapter {
        private Fragment[] mFragmentTabs = new Fragment[3];

        MainTabsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = mFragmentTabs[position];
            if (fragment == null) {
                switch (position) {
                    case TAB_HOME:
                        fragment = HomeFragment.newInstance();
                        break;
                    case TAB_READ:
                        fragment = MyFragment.newInstance();
                        break;
                    case TAB_MY:
                        fragment = MyFragment.newInstance();
                        break;
                }
                mFragmentTabs[position] = fragment;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return mFragmentTabs.length;
        }
    }
}
