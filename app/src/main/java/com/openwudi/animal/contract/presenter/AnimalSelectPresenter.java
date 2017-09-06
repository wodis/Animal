package com.openwudi.animal.contract.presenter;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.blankj.utilcode.utils.EmptyUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.openwudi.animal.adapter.ListDropDownAdapter;
import com.openwudi.animal.contract.AnimalSelectContract;
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

    @Override
    public void onStart() {
        final DropDownMenu dropDownMenu = mView.getDropDownMenu();
        oneView = new ListView(mContext);
        twoView = new ListView(mContext);
        threeView = new ListView(mContext);
        fourView = new ListView(mContext);
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
//                if (position == 0) {
//                    return;
//                }
                Animal animal = one.get(position);
                oneAdapter.setCheckItem(position);
                dropDownMenu.setTabText(animal.getName());
                dropDownMenu.closeMenu();
                listItem("2", animal.getId(), "");
            }
        });

        twoView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0) {
//                    return;
//                }
                Animal animal = two.get(position);
                twoAdapter.setCheckItem(position);
                dropDownMenu.setTabText(animal.getName());
                dropDownMenu.closeMenu();
                listItem("3", animal.getId(), "");
            }
        });

        threeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0) {
//                    return;
//                }
                Animal animal = three.get(position);
                threeAdapter.setCheckItem(position);
                dropDownMenu.setTabText(animal.getName());
                dropDownMenu.closeMenu();
                listItem("4", animal.getId(), "");
            }
        });

        fourView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0) {
//                    return;
//                }
                Animal animal = four.get(position);
                threeAdapter.setCheckItem(position);
                dropDownMenu.setTabText(animal.getName());
                dropDownMenu.closeMenu();
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
                animalList = ApiManager.getAnimalSelectList(level, fid, keyword);
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
//                    Animal empty = new Animal();
//                    empty.setId("0");
//                    empty.setName("纲");
//                    one.add(0, empty);
                    oneAdapter.setList(one);
                    oneView.getOnItemClickListener().onItemClick(null, null, 0, 0);
                    mView.search("");
                } else if ("2".equals(level)) {
                    two = animalList;
//                    Animal empty = new Animal();
//                    empty.setId("0");
//                    empty.setName("目");
//                    two.add(0, empty);
                    twoAdapter.setList(two);
                    threeAdapter.setList(new ArrayList<Animal>());
                    fourAdapter.setList(new ArrayList<Animal>());
                    twoView.getOnItemClickListener().onItemClick(null, null, 0, 0);
                } else if ("3".equals(level)) {
                    three = animalList;
//                    Animal empty = new Animal();
//                    empty.setId("0");
//                    empty.setName("科");
//                    three.add(0, empty);
                    threeAdapter.setList(three);
                    fourAdapter.setList(new ArrayList<Animal>());
                    threeView.getOnItemClickListener().onItemClick(null, null, 0, 0);
                } else if ("4".equals(level)) {
                    four = animalList;
//                    Animal empty = new Animal();
//                    empty.setId("0");
//                    empty.setName("属");
//                    four.add(0, empty);
                    fourAdapter.setList(four);
                    fourView.getOnItemClickListener().onItemClick(null, null, 0, 0);
                }
            }
        });
    }
}
