package com.openwudi.animal.contract.presenter;

import com.blankj.utilcode.utils.EmptyUtils;
import com.blankj.utilcode.utils.ThreadPoolUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.openwudi.animal.contract.TraceContract;
import com.openwudi.animal.db.GPSData;
import com.openwudi.animal.exception.AnimalException;
import com.openwudi.animal.manager.ApiManager;
import com.openwudi.animal.model.GPSDataModel;

import java.util.List;
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

public class TracePresenter extends TraceContract.Presenter {

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
                mModel.save2Db(lat, lng, uuid);
            }
        });
    }

    public void up() {
        final Observable.OnSubscribe<String> onSubscribe = new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                List<GPSData> list = mModel.list();
                while (EmptyUtils.isNotEmpty(list)) {
                    for (GPSData data : list) {
                        try {
                            ApiManager.saveGpsData(new GPSDataModel(data));
                        } catch (AnimalException e) {
                            if (e.getErrorCode() == RESP_FAIL_ERROR.code || e.getMessage().contains("重复键值")) {

                            } else {
                                throw e;
                            }
                            e.printStackTrace();
                        }
                        mModel.deleteById(data.getId());
                    }
                    list.clear();
                    list = mModel.list();
                }
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
                ToastUtils.showShortToast(mContext, "上报成功");
            }
        });
    }
}
