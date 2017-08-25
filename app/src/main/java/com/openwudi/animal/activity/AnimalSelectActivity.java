package com.openwudi.animal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.utils.EmptyUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.openwudi.animal.R;
import com.openwudi.animal.base.BaseActivity;
import com.openwudi.animal.db.manager.AnimalEntityManager;
import com.openwudi.animal.manager.ApiManager;
import com.openwudi.animal.model.Animal;
import com.openwudi.animal.view.ClearEditText;
import com.openwudi.animal.view.TitleBarView;

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
 * Created by diwu on 17/7/17.
 */

public class AnimalSelectActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title_bar_tbv)
    TitleBarView titleBarTbv;
    @BindView(R.id.search_tv)
    TextView searchTv;
    @BindView(R.id.search_input_et)
    ClearEditText searchInputEt;
    @BindView(R.id.content_lv)
    ListView contentLv;

    private AnimalSelectAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_select);
        ButterKnife.bind(this);
        initView();
        search("");
    }

    private void initView() {
        titleBarTbv.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchTv.setOnClickListener(this);
        adapter = new AnimalSelectAdapter();
        contentLv.setAdapter(adapter);

        searchInputEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    String name = searchInputEt.getText().toString();
                    search(name);
                    return true;
                }
                return false;
            }
        });

        searchInputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (EmptyUtils.isNotEmpty(s.toString()) && s.length() > 1){
                    search(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_tv:
                String name = searchInputEt.getText().toString();
                search(name);
                break;
        }
    }

    private void search(final String name){
        final Observable.OnSubscribe<List<Animal>> onSubscribe = new Observable.OnSubscribe<List<Animal>>() {
            @Override
            public void call(Subscriber<? super List<Animal>> subscriber) {
                List<Animal> animalList;
                if (EmptyUtils.isEmpty(name)){
                    animalList = AnimalEntityManager.listAll();
                } else {
                    animalList = ApiManager.getAnimalList(name);
                }
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
                adapter.setData(animalList);
            }
        });
    }

    private void getAnimalById(final String id){
        final Observable.OnSubscribe<Animal> onSubscribe = new Observable.OnSubscribe<Animal>() {
            @Override
            public void call(Subscriber<? super Animal> subscriber) {
                Animal animal = ApiManager.getAnimalModel(id);
                if (animal != null){
                    AnimalEntityManager.update(animal);
                }
                subscriber.onNext(animal);
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
                }).subscribe(new Subscriber<Animal>() {
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
            public void onNext(Animal animal) {
                Intent i = new Intent();
                i.putExtra(Animal.class.getSimpleName(), animal);
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }


    public class AnimalSelectAdapter extends BaseAdapter {

        private List<Animal> data = new ArrayList<>();

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Animal getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_animal_select, null);
                convertView.setTag(new ViewHolder(convertView));
            }

            final Animal animal = getItem(position);
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.titleTv.setText(animal.getName());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAnimalById(animal.getId());
                }
            });
            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.title_tv)
            TextView titleTv;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }

        public void setData(List<Animal> newData){
            data.clear();
            data.addAll(newData);
            notifyDataSetChanged();
        }
    }
}
