package com.openwudi.animal.contract.presenter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.blankj.utilcode.utils.EmptyUtils;
import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.NetworkUtils;
import com.blankj.utilcode.utils.ThreadPoolUtils;
import com.blankj.utilcode.utils.TimeUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.openwudi.animal.R;
import com.openwudi.animal.activity.MapQueryActivity;
import com.openwudi.animal.base.BaseActivity;
import com.openwudi.animal.contract.TraceContract;
import com.openwudi.animal.contract.model.TraceModel;
import com.openwudi.animal.db.GPSData;
import com.openwudi.animal.exception.AnimalException;
import com.openwudi.animal.manager.ApiManager;
import com.openwudi.animal.model.GPSDataModel;
import com.openwudi.animal.utils.Utils;
import com.openwudi.animal.view.pickerview.TimePickerDialog;
import com.openwudi.animal.view.pickerview.data.Type;
import com.openwudi.animal.view.pickerview.listener.OnDateSetListener;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

import static com.openwudi.animal.exception.RES_STATUS.RESP_FAIL_ERROR;

/**
 * Created by diwu on 17/7/24.
 */

public class TracePresenter extends TraceContract.Presenter implements OnDateSetListener {

    static String uuid;
    ThreadPoolUtils poolUtils;

    @Override
    public void onStart() {
        poolUtils = new ThreadPoolUtils(ThreadPoolUtils.Type.SingleThread, 1);
        if (EmptyUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
        }
    }

    public void saveGps(final double lat, final double lng) {
        poolUtils.execute(new Runnable() {
            @Override
            public void run() {
                GPSData data = mModel.save2Db(lat, lng, uuid);
                if (mModel.getTimes() % 2 == 0) {
                    LogUtils.d(mModel.getTimes() % 10);
                    upAll();
                }
            }
        });
    }

    public void up() {
        final Observable.OnSubscribe<String> onSubscribe = new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                upAll();
                String result = "";
                subscriber.onNext(result);
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
                }).subscribe(new Subscriber<String>() {
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
            public void onNext(String string) {
//                ToastUtils.showShortToast(mContext, "上报成功");
            }
        });
    }

    private void upAll() {
        if (!NetworkUtils.isConnected(mContext)) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showShortToast(mContext, "暂无网络");
                }
            });
            return;
        }
        List<GPSData> list = mModel.list();
        while (EmptyUtils.isNotEmpty(list)) {
            for (GPSData data : list) {
                try {
                    ApiManager.saveGpsData(new GPSDataModel(data));
                    mModel.deleteById(data.getId());
                } catch (Exception e) {
                    if (e instanceof AnimalException) {
                        if (e.getMessage().contains("重复键值")) {
                            mModel.deleteById(data.getId());
                        }
                    }
                    e.printStackTrace();
                }
            }
            list.clear();
            list = mModel.list();
        }
    }

    public void startGpsHistory() {
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
        Intent i = new Intent(mContext, MapQueryActivity.class);
        i.putExtra("onlyShow", true);
        i.putExtra("date", date);
        mContext.startActivity(i);
    }
}
