package com.openwudi.animal.contract.presenter;

import com.blankj.utilcode.utils.EmptyUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.openwudi.animal.base.BaseActivity;
import com.openwudi.animal.contract.UpDetailContract;
import com.openwudi.animal.db.manager.UpEntityManager;
import com.openwudi.animal.event.IdEvent;
import com.openwudi.animal.manager.ApiManager;
import com.openwudi.animal.model.Animal;
import com.openwudi.animal.model.DataAcquisition;
import com.openwudi.animal.model.Item;
import com.openwudi.animal.model.ItemEncode;
import com.openwudi.animal.model.UpObject;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by diwu on 17/7/20.
 */

public class UpDetailPresenter extends UpDetailContract.Presenter {
    @Override
    public void onStart() {

    }

    public Item findByCode(String encode, String code) {
        List<Item> items = ApiManager.getItemsList(encode);
        for (Item item : items) {
            if (code.equals(item.getCode())) {
                return item;
            }
        }
        return null;
    }

    public Set<Item> findByCode(String code) {
        Set<Item> set = new HashSet<>();
        if (EmptyUtils.isEmpty(code)) {
            return set;
        }
        String[] codes = code.split(",");
        List<String> codeS = new ArrayList<>();
        Collections.addAll(codeS, codes);
        List<Item> items = ApiManager.getItemsList(ItemEncode.DWZT);
        for (Item item : items) {
            if (codeS.contains(item.getCode())) {
                set.add(item);
            }
        }
        return set;
    }

    public void getObject(final Long id) {
        if (id == 0) {
            return;
        }
        final Observable.OnSubscribe<UpObject> onSubscribe = new Observable.OnSubscribe<UpObject>() {
            @Override
            public void call(Subscriber<? super UpObject> subscriber) {
                UpObject list = UpEntityManager.getById(id);
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
                }).subscribe(new Subscriber<UpObject>() {
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
            public void onNext(UpObject upObjects) {
                mView.setObject(upObjects);
                mView.initView();
            }
        });
    }

    public void getObjectByData(final DataAcquisition dataAcquisition) {
        final Observable.OnSubscribe<UpObject> onSubscribe = new Observable.OnSubscribe<UpObject>() {
            @Override
            public void call(Subscriber<? super UpObject> subscriber) {
                DataAcquisition newData = ApiManager.getDataAcquisitionModel(dataAcquisition.getId());
                Animal animal = ApiManager.getAnimalModel(newData.getAnimalId());
                Item qixidi = findByCode(ItemEncode.SJTZ, newData.getHabitate());
                Set<Item> zhuangtai = findByCode(newData.getAnimaStateId());
                Item juli = findByCode(ItemEncode.CJJL, newData.getDistance());
                Item fangwei = findByCode(ItemEncode.CJFW, newData.getAzimuth());
                Item weizhi = findByCode(ItemEncode.CJWZ, newData.getPosition());
                UpObject object = new UpObject(0L, animal, newData, qixidi, zhuangtai, juli, fangwei, weizhi);
                subscriber.onNext(object);
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
                }).subscribe(new Subscriber<UpObject>() {
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
            public void onNext(UpObject upObjects) {
                mView.setObject(upObjects);
                mView.initView();
            }
        });
    }

    public void delete(final String id) {
        final Observable.OnSubscribe<String> onSubscribe = new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(ApiManager.deleteDataAcquisition(id));
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
                EventBus.getDefault().post(new IdEvent(id));
                ((BaseActivity) mContext).finish();
            }
        });
    }
}
