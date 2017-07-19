package com.openwudi.animal.contract.presenter;

import com.blankj.utilcode.utils.ToastUtils;
import com.openwudi.animal.contract.UpSaveContract;
import com.openwudi.animal.db.manager.UpEntityManager;
import com.openwudi.animal.manager.ApiManager;
import com.openwudi.animal.model.UpObject;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by diwu on 17/7/19.
 */

public class UpSavePresenter extends UpSaveContract.Presenter {
    @Override
    public void onStart() {

    }

    public void report(final List<UpObject> list){
        final List<Long> ids = new ArrayList<>();
        final Observable.OnSubscribe<String> onSubscribe = new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                for (UpObject o : list){
                    ApiManager.saveDataAcquisition(o.getDataAcquisition());
                    ids.add(o.getId());
                }
                UpEntityManager.deleteById(ids);
                subscriber.onNext("");
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
                UpEntityManager.deleteById(ids);
                mView.hideLoading();
                ToastUtils.showShortToast(mContext, e.getMessage());
            }

            @Override
            public void onNext(String upObjects) {
                refresh();
            }
        });
    }

    public void refresh() {
        final Observable.OnSubscribe<List<UpObject>> onSubscribe = new Observable.OnSubscribe<List<UpObject>>() {
            @Override
            public void call(Subscriber<? super List<UpObject>> subscriber) {
                List<UpObject> list = UpEntityManager.listAll();
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
                }).subscribe(new Subscriber<List<UpObject>>() {
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
            public void onNext(List<UpObject> upObjects) {
                if (upObjects.size() == 0){
                    ToastUtils.showShortToast(mContext, "无数据");
                    mView.setData(new ArrayList<UpObject>());
                } else {
                    mView.setData(upObjects);
                }
            }
        });
    }
}
