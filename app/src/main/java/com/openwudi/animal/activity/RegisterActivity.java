package com.openwudi.animal.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.utils.EmptyUtils;
import com.blankj.utilcode.utils.RegexUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.openwudi.animal.R;
import com.openwudi.animal.base.BaseActivity;
import com.openwudi.animal.base.StatusBarCompat;
import com.openwudi.animal.exception.AnimalException;
import com.openwudi.animal.exception.RES_STATUS;
import com.openwudi.animal.manager.ApiManager;
import com.openwudi.animal.model.Area;
import com.openwudi.animal.model.Item;
import com.openwudi.animal.model.ItemEncode;
import com.openwudi.animal.model.MonitorStation;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by diwu on 17/7/13.
 */

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.account)
    EditText account;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.reg_btn)
    TextView regBtn;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.tv_monitor)
    TextView tvMonitor;
    @BindView(R.id.tv_tujing)
    TextView tvTujing;

    private String areaId = "110000";

    private Area area = null;
    private MonitorStation monitorStation = null;
    private Item tujing = null;

    @Override
    protected void setStatusBarColor() {
        StatusBarCompat.translucentStatusBar(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, LoginActivity.class));
            }
        });

        tvArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observable.OnSubscribe<List<Area>> onSubscribe = new Observable.OnSubscribe<List<Area>>() {
                    @Override
                    public void call(Subscriber<? super List<Area>> subscriber) {
                        List<Area> areaList = ApiManager.getAreaList(areaId);
                        if (EmptyUtils.isNotEmpty(areaList)) {
                            subscriber.onNext(areaList);
                        }
                        subscriber.onCompleted();
                    }
                };

                Observable.create(onSubscribe)
                        .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                tvArea.setEnabled(false);
                                showLoading();
                            }
                        }).
                        subscribe(new Subscriber<List<Area>>() {
                            @Override
                            public void onCompleted() {
                                tvArea.setEnabled(true);
                                hideLoading();
                            }

                            @Override
                            public void onError(Throwable e) {
                                tvArea.setEnabled(true);
                                hideLoading();
                                ToastUtils.showShortToast(mContext, e.getMessage());
                            }

                            @Override
                            public void onNext(List<Area> list) {
                                listArea(list);
                            }
                        });

            }
        });

        tvMonitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observable.OnSubscribe<List<Item>> onSubscribe = new Observable.OnSubscribe<List<Item>>() {
                    @Override
                    public void call(Subscriber<? super List<Item>> subscriber) {
                        List<Item> items = ApiManager.getItemsList(ItemEncode.SYDX);
                        subscriber.onNext(items);
                        subscriber.onCompleted();
                    }
                };

                Observable.create(onSubscribe)
                        .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                tvMonitor.setEnabled(false);
                                showLoading();
                            }
                        }).
                        subscribe(new Subscriber<List<Item>>() {
                            @Override
                            public void onCompleted() {
                                tvMonitor.setEnabled(true);
                                hideLoading();
                            }

                            @Override
                            public void onError(Throwable e) {
                                tvMonitor.setEnabled(true);
                                hideLoading();
                                ToastUtils.showShortToast(mContext, e.getMessage());
                            }

                            @Override
                            public void onNext(List<Item> list) {
                                if (EmptyUtils.isEmpty(list)) {
                                    ToastUtils.showShortToast(mContext, "无上报途径");
                                } else {
                                    listItem(list);
                                }
                            }
                        });
            }
        });

        tvMonitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observable.OnSubscribe<List<MonitorStation>> onSubscribe = new Observable.OnSubscribe<List<MonitorStation>>() {
                    @Override
                    public void call(Subscriber<? super List<MonitorStation>> subscriber) {
                        if (area == null) {
                            throw new AnimalException("请先选择地区", RES_STATUS.SERVER_ERROR);
                        }

                        List<MonitorStation> areaList = ApiManager.getMonitorStationList(area.getId());
                        subscriber.onNext(areaList);
                        subscriber.onCompleted();
                    }
                };

                Observable.create(onSubscribe)
                        .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                tvMonitor.setEnabled(false);
                                showLoading();
                            }
                        }).
                        subscribe(new Subscriber<List<MonitorStation>>() {
                            @Override
                            public void onCompleted() {
                                tvMonitor.setEnabled(true);
                                hideLoading();
                            }

                            @Override
                            public void onError(Throwable e) {
                                tvMonitor.setEnabled(true);
                                hideLoading();
                                ToastUtils.showShortToast(mContext, e.getMessage());
                            }

                            @Override
                            public void onNext(List<MonitorStation> list) {
                                if (EmptyUtils.isEmpty(list)) {
                                    ToastUtils.showShortToast(mContext, "当前地区无监测站");
                                } else {
                                    listMonitor(list);
                                }
                            }
                        });
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void listItem(final List<Item> list) {
        final String[] items = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            items[i] = list.get(i).getName();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        //设置标题
        builder.setTitle("请选择上报途径");
        //设置图标
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(mContext, items[i], Toast.LENGTH_SHORT).show();
                tvTujing.setText(items[i]);
                tujing = list.get(i);
            }
        });
        builder.create();
        builder.show();
    }

    private void listArea(final List<Area> list) {
        final String[] items = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            items[i] = list.get(i).getFullname();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置标题
        builder.setTitle("请选择地区");
        //设置图标
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(mContext, items[i], Toast.LENGTH_SHORT).show();
                tvArea.setText(items[i]);
                area = list.get(i);
                monitorStation = null;
                tvMonitor.setText("");
            }
        });
        builder.create();
        builder.show();
    }

    private void listMonitor(final List<MonitorStation> list) {
        final String[] items = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            items[i] = list.get(i).getName();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置标题
        builder.setTitle("请选择监测站");
        //设置图标
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(mContext, items[i], Toast.LENGTH_SHORT).show();
                tvMonitor.setText(items[i]);
                monitorStation = list.get(i);
            }
        });
        builder.create();
        builder.show();
    }

    private void register() {
        if (!RegexUtils.isMobileSimple(account.getText().toString())) {
            ToastUtils.showShortToast(mContext, "请输入正确的手机号");
            return;
        }

        if (!RegexUtils.isUsername(password.getText().toString())) {
            ToastUtils.showShortToast(mContext, "请输入正确的密码格式");
            return;
        }

        final Observable.OnSubscribe<String> onSubscribe = new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String tid = ApiManager.getTerminalId(tujing.getCode());
                String uid = ApiManager.addUser(account.getText().toString(), tid, area.getId(), monitorStation.getId());
                String result = ApiManager.addPassword(uid, password.getText().toString());
                subscriber.onNext(result);
                subscriber.onCompleted();
            }
        };

        Observable.create(onSubscribe)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        button.setEnabled(false);
                        showLoading();
                    }
                }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                button.setEnabled(true);
                hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                button.setEnabled(true);
                hideLoading();
                ToastUtils.showShortToast(mContext, e.getMessage());
            }

            @Override
            public void onNext(String s) {
                ToastUtils.showShortToast(mContext, s);
            }
        });
    }
}
