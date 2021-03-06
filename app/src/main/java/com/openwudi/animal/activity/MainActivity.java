package com.openwudi.animal.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.EmptyUtils;
import com.blankj.utilcode.utils.SPUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.openwudi.animal.R;
import com.openwudi.animal.base.BaseActivity;
import com.openwudi.animal.db.AnimalServerEntity;
import com.openwudi.animal.db.manager.AnimalServerEntityManager;
import com.openwudi.animal.event.NewMessageEvent;
import com.openwudi.animal.event.RefreshMessageEvent;
import com.openwudi.animal.event.TabEvent;
import com.openwudi.animal.fragment.HomeFragment;
import com.openwudi.animal.fragment.MessageFragment;
import com.openwudi.animal.fragment.MyFragment;
import com.openwudi.animal.manager.ApiManager;
import com.openwudi.animal.model.Animal;
import com.openwudi.animal.model.ItemEncode;
import com.openwudi.animal.utils.Constants;
import com.openwudi.animal.view.AlertDialogFragment;
import com.openwudi.animal.view.MainTabViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

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
    @BindView(R.id.reminder)
    ImageView reminder;

    public static final int CLICK_BACK_INTERVAL = 2000;
    private long mLastClickTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - mLastClickTime) > CLICK_BACK_INTERVAL) {
                ToastUtils.showShortToast(this, "再按一次退出");
                mLastClickTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        EventBus.getDefault().register(this);
        download();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
        int homeTextColor = ContextCompat.getColor(this, R.color.color6);
        int helpTextColor = ContextCompat.getColor(this, R.color.color6);
        int myTextColor = ContextCompat.getColor(this, R.color.color6);

        switch (id) {
            case TAB_HOME:
                homeDrawable = ContextCompat.getDrawable(this, R.drawable.icons);
                homeTextColor = ContextCompat.getColor(this, R.color.color6);
                break;
            case TAB_READ:
                helpDrawable = ContextCompat.getDrawable(this, R.drawable.icon_msg);
                helpTextColor = ContextCompat.getColor(this, R.color.color6);
                reminder.setVisibility(View.GONE);
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
                        fragment = MessageFragment.newInstance();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTabEvent(
            TabEvent event) {
        selectMainTab(event.getPosition());
        mainVp.setCurrentItem(event.getPosition(), false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewMessageEvent(NewMessageEvent event) {
        reminder.setVisibility(event.same ? View.GONE : View.VISIBLE);
        if (!event.same && !showed) {
            showDialog();
        }
    }

    private boolean showed = false;

    private void showDialog() {
        showed = true;
        AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(mContext, getSupportFragmentManager());
        builder.setTag("showDialog");
        builder.setTitle("您有新的通知");
        builder.setNegativeButton("确定", new AlertDialogFragment.OnClickListener() {
            @Override
            public void onClick(View view) {
                showed = false;
                if (TAB_READ == mainVp.getCurrentItem()){
                    EventBus.getDefault().post(new RefreshMessageEvent());
                } else {
                    selectMainTab(TAB_READ);
                    mainVp.setCurrentItem(TAB_READ, false);
                }
            }
        });
        builder.setCancelable(true);
        builder.show();
    }

    private void download() {
        final SPUtils spUtils = new SPUtils(mContext, Constants.SP_CONFIG);
        final String version = spUtils.getString(Constants.SP_CONFIG_V, "0");
        final Observable.OnSubscribe<String> onSubscribe = new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String serverVersion = ApiManager.getInterfaceVersion();
                if (!version.equals(serverVersion)) {
                    List<Animal> all = ApiManager.getAnimalAll();
                    AnimalServerEntityManager.add(all);
                    spUtils.putString(Constants.SP_CONFIG_V, serverVersion);
                }

                //强制更新枚举
                ApiManager.getItemsListUpdateCache(ItemEncode.CJFW);
                ApiManager.getItemsListUpdateCache(ItemEncode.CJJL);
                ApiManager.getItemsListUpdateCache(ItemEncode.CJWZ);
                ApiManager.getItemsListUpdateCache(ItemEncode.DWZT);
                ApiManager.getItemsListUpdateCache(ItemEncode.SJTZ);
                ApiManager.getItemsListUpdateCache(ItemEncode.SYDX);

                subscriber.onNext("");
                subscriber.onCompleted();
            }
        };

        Observable.create(onSubscribe)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showLoading();
                    }
                }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                hideLoading();
            }

            @Override
            public void onNext(String s) {
            }
        });
    }
}
