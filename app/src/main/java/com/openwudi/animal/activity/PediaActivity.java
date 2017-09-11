package com.openwudi.animal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.openwudi.animal.R;
import com.openwudi.animal.base.BaseActivity;
import com.openwudi.animal.db.manager.AnimalServerEntityManager;
import com.openwudi.animal.model.Animal;
import com.openwudi.animal.view.TitleBarView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by diwu on 2017/9/11.
 */

public class PediaActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title_bar_tbv)
    TitleBarView titleBarTbv;
    @BindView(R.id.bird)
    View bird;
    @BindView(R.id.cat)
    View cat;
    @BindView(R.id.snak)
    View snak;
    @BindView(R.id.frog)
    View frog;

    TextView tv1;
    TextView tv2;
    TextView tv3;
    TextView tv4;

    ImageView ivBird;
    ImageView ivCat;
    ImageView ivSnak;
    ImageView ivFrog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedia);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        titleBarTbv.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bird.setOnClickListener(this);
        cat.setOnClickListener(this);
        snak.setOnClickListener(this);
        frog.setOnClickListener(this);
        tv1 = (TextView) bird.findViewById(R.id.tv);
        tv2 = (TextView) cat.findViewById(R.id.tv);
        tv3 = (TextView) snak.findViewById(R.id.tv);
        tv4 = (TextView) frog.findViewById(R.id.tv);
        ivBird = (ImageView) bird.findViewById(R.id.iv);
        ivCat = (ImageView) cat.findViewById(R.id.iv);
        ivSnak = (ImageView) snak.findViewById(R.id.iv);
        ivFrog = (ImageView) frog.findViewById(R.id.iv);
        ivBird.setImageDrawable(getResources().getDrawable(R.drawable.niao));
        ivCat.setImageDrawable(getResources().getDrawable(R.drawable.mao));
        ivSnak.setImageDrawable(getResources().getDrawable(R.drawable.she));
        ivFrog.setImageDrawable(getResources().getDrawable(R.drawable.wa));
        tv1.setText("鸟纲");
        tv2.setText("哺乳纲");
        tv3.setText("爬行纲");
        tv4.setText("两栖纲");
    }

    @Override
    public void onClick(View v) {
        next(v.getId());
    }

    private void next(final int id){
        final Observable.OnSubscribe<List<Animal>> onSubscribe = new Observable.OnSubscribe<List<Animal>>() {
            @Override
            public void call(Subscriber<? super List<Animal>> subscriber) {
                List<Animal> animalList = AnimalServerEntityManager.getAnimalSelectList("1", "", "");
                subscriber.onNext(animalList);
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
                }).subscribe(new Subscriber<List<Animal>>() {
            @Override
            public void onCompleted() {
                hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                hideLoading();
                ToastUtils.showShortToast(mContext, e.getMessage());
            }

            @Override
            public void onNext(List<Animal> animalList) {
                String name = "";
                switch (id) {
                    case R.id.bird:
                        name = "鸟纲";
                        break;
                    case R.id.cat:
                        name = "哺乳纲";
                        break;
                    case R.id.snak:
                        name = "爬行纲";
                        break;
                    case R.id.frog:
                        name = "两栖纲";
                        break;
                }

                for (Animal animal : animalList){
                    if (name.equals(animal.getName())){
                        Intent i = new Intent(mContext, AnimalDetailSelectActivity.class);
                        i.putExtra(Animal.class.getSimpleName(), animal);
                        startActivity(i);
                        break;
                    }
                }
            }
        });
    }
}
