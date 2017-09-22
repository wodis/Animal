package com.openwudi.animal.contract.presenter;

import android.util.ArraySet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.blankj.utilcode.utils.EmptyUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.openwudi.animal.R;
import com.openwudi.animal.adapter.ListDropDownAdapter;
import com.openwudi.animal.contract.AnimalSelectContract;
import com.openwudi.animal.db.manager.AnimalServerEntityManager;
import com.openwudi.animal.manager.ApiManager;
import com.openwudi.animal.model.Animal;
import com.yyydjk.library.DropDownMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by diwu on 2017/9/6.
 */

public class AnimalSelectPresenter extends AnimalSelectContract.Presenter {
    private String headers[] = {"纲", "目", "科", "属"};

    private ListDropDownAdapter oneAdapter;
    private ListDropDownAdapter twoAdapter;
    private ListDropDownAdapter threeAdapter;
    private ListDropDownAdapter fourAdapter;
    private List<View> popupViews = new ArrayList<>();

    private List<Animal> one = new ArrayList<>();
    private List<Animal> two = new ArrayList<>();
    private List<Animal> three = new ArrayList<>();
    private List<Animal> four = new ArrayList<>();

    private ListView oneView = null;
    private ListView twoView = null;
    private ListView threeView = null;
    private ListView fourView = null;

    /**
     * 用于标识选中level=4
     */
    private Animal fourItem = null;

    @Override
    public void onStart() {
        final DropDownMenu dropDownMenu = mView.getDropDownMenu();
        oneView = new ListView(mContext);
        twoView = new ListView(mContext);
        threeView = new ListView(mContext);
        fourView = new ListView(mContext);

        oneView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        twoView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        threeView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        fourView.setBackgroundColor(mContext.getResources().getColor(R.color.white));

        oneAdapter = new ListDropDownAdapter(mContext, one);
        twoAdapter = new ListDropDownAdapter(mContext, two);
        threeAdapter = new ListDropDownAdapter(mContext, three);
        fourAdapter = new ListDropDownAdapter(mContext, four);
        oneView.setAdapter(oneAdapter);
        twoView.setAdapter(twoAdapter);
        threeView.setAdapter(threeAdapter);
        fourView.setAdapter(fourAdapter);
        //init popupViews
        popupViews.add(oneView);
        popupViews.add(twoView);
        popupViews.add(threeView);
        popupViews.add(fourView);

        oneView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Animal animal = one.get(position);
                oneAdapter.setCheckItem(position);
                dropDownMenu.setTabText(0, animal.getName());
                dropDownMenu.closeMenu();
                listItem("2", animal.getId(), "");
            }
        });

        twoView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Animal animal = two.get(position);
                twoAdapter.setCheckItem(position);
                dropDownMenu.setTabText(2, animal.getName());
                dropDownMenu.closeMenu();
                listItem("3", animal.getId(), "");
            }
        });

        threeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Animal animal = three.get(position);
                threeAdapter.setCheckItem(position);
                dropDownMenu.setTabText(4, animal.getName());
                dropDownMenu.closeMenu();
                listItem("4", animal.getId(), "");
                fourItem = null;
            }
        });

        fourView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Animal animal = four.get(position);
                fourAdapter.setCheckItem(position);
                dropDownMenu.setTabText(6, animal.getName());
                dropDownMenu.closeMenu();
                fourItem = animal;
                if (view != null) {
                    mergeSearch("");
                }
            }
        });


        dropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, mView.getContentView());

        listItem("1", "", "");
    }

    public void listItem(final String level, final String fid, final String keyword) {
        final Observable.OnSubscribe<List<Animal>> onSubscribe = new Observable.OnSubscribe<List<Animal>>() {
            @Override
            public void call(Subscriber<? super List<Animal>> subscriber) {
                List<Animal> animalList;
//                animalList = ApiManager.getAnimalSelectList(level, fid, keyword);
                animalList = AnimalServerEntityManager.getAnimalSelectList(level, fid, keyword);
                subscriber.onNext(animalList);
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
                }).subscribe(new Subscriber<List<Animal>>() {
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
            public void onNext(List<Animal> animalList) {
                if ("1".equals(level)) {
                    one = animalList;
                    oneAdapter.setList(one);
                    oneView.getOnItemClickListener().onItemClick(null, null, 0, 0);
                    //第一次初始化后,展示缓存
                    mView.search("");
                } else if ("2".equals(level)) {
                    two = animalList;
                    twoAdapter.setList(two);
                    //设置第2个列表数据后,清空第3第4列表
                    threeAdapter.setList(new ArrayList<Animal>());
                    fourAdapter.setList(new ArrayList<Animal>());
                    //模拟点击第2列表第1元素
                    twoView.getOnItemClickListener().onItemClick(null, null, 0, 0);
                } else if ("3".equals(level)) {
                    three = animalList;
                    threeAdapter.setList(three);
                    //设置第3个列表数据后,清空第4列表
                    fourAdapter.setList(new ArrayList<Animal>());
                    //模拟点击第3列表第1元素
                    threeView.getOnItemClickListener().onItemClick(null, null, 0, 0);
                } else if ("4".equals(level)) {
                    four = animalList;
                    fourAdapter.setList(four);
                    //设置第4个列表数据
                    //模拟点击第4列表第1元素
                    fourView.getOnItemClickListener().onItemClick(null, null, 0, 0);
                }
            }
        });
    }

    public void mergeSearch(final String keyword) {
        if (fourItem == null) {
            ToastUtils.showShortToast(mContext, "请先选择分类");
            return;
        }

        final Observable.OnSubscribe<List<Animal>> onSubscribe = new Observable.OnSubscribe<List<Animal>>() {
            @Override
            public void call(Subscriber<? super List<Animal>> subscriber) {
//                List<Animal> animalListClass = ApiManager.getAnimalSelectList("5", fourItem.getId(), keyword);
                List<Animal> animalListClass = AnimalServerEntityManager.getAnimalSelectList("5", fourItem.getId(), keyword);
                if (EmptyUtils.isNotEmpty(keyword)) {
                    List<Animal> animalList = AnimalServerEntityManager.getAnimalSelectList("5", "", keyword);
                    for (Animal an : animalList) {
                        //过滤已存在的,并且必须属于5级的动物
                        if (!animalListClass.contains(an) && an.getLevel() == 5) {
                            animalListClass.add(an);
                        }
                    }
                }
                subscriber.onNext(animalListClass);
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
                }).subscribe(new Subscriber<List<Animal>>() {
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
            public void onNext(List<Animal> animalList) {
                mView.setData(animalList);
            }
        });
    }
}
