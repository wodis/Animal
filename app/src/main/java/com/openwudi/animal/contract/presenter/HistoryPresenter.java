package com.openwudi.animal.contract.presenter;

import com.blankj.utilcode.utils.ToastUtils;
import com.openwudi.animal.contract.HistoryContract;
import com.openwudi.animal.manager.ApiManager;
import com.openwudi.animal.model.DataAcquisition;
import com.openwudi.animal.model.Item;
import com.openwudi.animal.model.ItemEncode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by diwu on 17/7/20.
 */

public class HistoryPresenter extends HistoryContract.Presenter {
    private Map<String, String> map = new HashMap<>();

    @Override
    public void onStart() {
        init();
    }

    public void init(){
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
                for (Item item : itemList){
                    map.put(item.getCode(), item.getName());
                }
                refresh(0);
            }
        });
    }

    public String getQixidiName(String encode){
        return map.get(encode);
    }

    public void refresh(final int index) {
        final Observable.OnSubscribe<List<DataAcquisition>> onSubscribe = new Observable.OnSubscribe<List<DataAcquisition>>() {
            @Override
            public void call(Subscriber<? super List<DataAcquisition>> subscriber) {
                List<DataAcquisition> list = ApiManager.getDataAcquisitionList(index);
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
                if (index == 0){
                    if (list.size() == 0){
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
}
