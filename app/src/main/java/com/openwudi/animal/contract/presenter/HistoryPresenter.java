package com.openwudi.animal.contract.presenter;

import android.content.Intent;
import android.util.Log;

import com.blankj.utilcode.utils.TimeUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.openwudi.animal.R;
import com.openwudi.animal.activity.MapLineActivity;
import com.openwudi.animal.activity.MapQueryActivity;
import com.openwudi.animal.base.BaseActivity;
import com.openwudi.animal.contract.HistoryContract;
import com.openwudi.animal.manager.ApiManager;
import com.openwudi.animal.model.DataAcquisition;
import com.openwudi.animal.model.Item;
import com.openwudi.animal.model.ItemEncode;
import com.openwudi.animal.utils.Utils;
import com.openwudi.animal.view.pickerview.TimePickerDialog;
import com.openwudi.animal.view.pickerview.data.Type;
import com.openwudi.animal.view.pickerview.listener.OnDateSetListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by diwu on 17/7/20.
 */

public class HistoryPresenter extends HistoryContract.Presenter implements OnDateSetListener {
    private Map<String, String> map = new HashMap<>();
    private String date = "";

    @Override
    public void onStart() {
        init();
    }

    public void init() {
        final Observable.OnSubscribe<List<Item>> onSubscribe = new Observable.OnSubscribe<List<Item>>() {
            @Override
            public void call(Subscriber<? super List<Item>> subscriber) {
                List<Item> items = ApiManager.getItemsList(ItemEncode.SJTZ);
                subscriber.onNext(items);
                subscriber.onCompleted();
            }
        };

        Observable.create(onSubscribe)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                }).subscribe(new Subscriber<List<Item>>() {
            @Override
            public void onCompleted() {
//                mView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                mView.hideLoading();
                ToastUtils.showShortToast(mContext, e.getMessage());
            }

            @Override
            public void onNext(List<Item> itemList) {
                for (Item item : itemList) {
                    map.put(item.getCode(), item.getName());
                }
                refresh(0, date);
            }
        });
    }

    public String getQixidiName(String encode) {
        return map.get(encode);
    }

    public void refresh(int index) {
        refresh(index, date);
    }

    public void refresh(final int index, final String date) {
        final Observable.OnSubscribe<List<DataAcquisition>> onSubscribe = new Observable.OnSubscribe<List<DataAcquisition>>() {
            @Override
            public void call(Subscriber<? super List<DataAcquisition>> subscriber) {
                List<DataAcquisition> list = ApiManager.getDataAcquisitionList(index, date);
                subscriber.onNext(list);
                subscriber.onCompleted();
            }
        };

        Observable.create(onSubscribe)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                }).subscribe(new Subscriber<List<DataAcquisition>>() {
            @Override
            public void onCompleted() {
                mView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                mView.hideLoading();
                ToastUtils.showShortToast(mContext, e.getMessage());
            }

            @Override
            public void onNext(List<DataAcquisition> list) {
                if (index == 0) {
                    if (list.size() == 0) {
                        mView.setData(new ArrayList<DataAcquisition>());
                    } else {
                        mView.setData(list);
                    }
                } else {
                    mView.addData(list);
                }
            }
        });
    }

    public void startGps() {
        TimePickerDialog.Builder mBuilder = initBuilder();
        long currentTime = System.currentTimeMillis();
        mBuilder.setCurrentMillseconds(currentTime);
        mBuilder.build().showDialog(((BaseActivity) mContext).getSupportFragmentManager(), "year_month");
    }

    private static int currentYear = 2017;
    private static int maxYear = 2023;
    private static int minYear = 2010;
    private long maxMillSeconds;
    private long minMillSeconds;

    private TimePickerDialog.Builder initBuilder() {
        TimePickerDialog.Builder mBuilder;
        String timeMin = mContext.getString(R.string.graduation_time_limit, minYear, 1);
        String timeMax = mContext.getString(R.string.graduation_time_limit, maxYear, 12);
        maxMillSeconds = Utils.string2Long(timeMax, mContext.getString(R.string.time_format));
        minMillSeconds = Utils.string2Long(timeMin, mContext.getString(R.string.time_format));
        mBuilder = new TimePickerDialog.Builder()
                .setType(Type.YEAR_MONTH_DAY)
                .setThemeColor(mContext.getResources().getColor(R.color.colorPrimary))
                .setCallBack(this)
                .setTitleStringId("采集时间")
                .setWheelItemTextSize(14)
                .setMinMillseconds(minMillSeconds)
                .setMaxMillseconds(maxMillSeconds)
        ;
        return mBuilder;
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        String date = TimeUtils.milliseconds2String(millseconds, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()));
        Log.d(this.getClass().getSimpleName(), "onDateSet: " + date);
        this.date = date;
        refresh(0, date);
        mView.setTitle(date);
//        Intent i = new Intent(mContext, MapQueryActivity.class);
//        i.putExtra("onlyShow", true);
//        i.putExtra("date", date);
//        mContext.startActivity(i);
    }
}
