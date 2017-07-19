package com.openwudi.animal.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.ConvertUtils;
import com.blankj.utilcode.utils.EmptyUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.bumptech.glide.Glide;
import com.openwudi.animal.R;
import com.openwudi.animal.base.BaseActivity;
import com.openwudi.animal.db.manager.UpEntityManager;
import com.openwudi.animal.event.UpEvent;
import com.openwudi.animal.manager.ApiManager;
import com.openwudi.animal.model.DataAcquisition;
import com.openwudi.animal.model.Item;
import com.openwudi.animal.model.UpObject;
import com.openwudi.animal.view.TableCellView;
import com.openwudi.animal.view.TitleBarView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by diwu on 17/7/19.
 */

public class UpDetailActivity extends BaseActivity {

    @BindView(R.id.title_bar_tbv)
    TitleBarView titleBarTbv;
    @BindView(R.id.name)
    TableCellView name;
    @BindView(R.id.pic_iv)
    ImageView picIv;
    @BindView(R.id.pic_line)
    View picLine;
    @BindView(R.id.caijishuliang)
    TableCellView caijishuliang;
    @BindView(R.id.qixidi)
    TableCellView qixidi;
    @BindView(R.id.status)
    TableCellView status;
    @BindView(R.id.health_left_iv)
    ImageView healthLeftIv;
    @BindView(R.id.jiangkangshuliang)
    TableCellView jiangkangshuliang;
    @BindView(R.id.jiangkangtupian)
    TableCellView jiangkangtupian;
    @BindView(R.id.ill_left_iv)
    ImageView illLeftIv;
    @BindView(R.id.shengbingshuliang)
    TableCellView shengbingshuliang;
    @BindView(R.id.shengbingline)
    View shengbingline;
    @BindView(R.id.shengbingtupian)
    TableCellView shengbingtupian;
    @BindView(R.id.shengbingEt)
    EditText shengbingEt;
    @BindView(R.id.shengbingmiaoshu)
    LinearLayout shengbingmiaoshu;
    @BindView(R.id.death_left_iv)
    ImageView deathLeftIv;
    @BindView(R.id.siwangshuliang)
    TableCellView siwangshuliang;
    @BindView(R.id.siwangline)
    View siwangline;
    @BindView(R.id.siwangtupian)
    TableCellView siwangtupian;
    @BindView(R.id.siwangEt)
    EditText siwangEt;
    @BindView(R.id.siwangmiaoshu)
    LinearLayout siwangmiaoshu;
    @BindView(R.id.juli)
    TableCellView juli;
    @BindView(R.id.fangwei)
    TableCellView fangwei;
    @BindView(R.id.weizhi)
    TableCellView weizhi;
    @BindView(R.id.gps)
    TableCellView gps;
    @BindView(R.id.time)
    TableCellView time;
    @BindView(R.id.bubaoEt)
    EditText bubaoEt;
    @BindView(R.id.submit_tv)
    TextView submitTv;
    private UpObject object;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_detail);
        ButterKnife.bind(this);
        Long id = getIntent().getLongExtra("id", 0);
        getObject(id);
    }

    private void initView() {
        titleBarTbv.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBarTbv.setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("确定删除吗?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UpEntityManager.deleteById(object.getId());
                        refresh();
                        finish();
                    }
                });
                //    设置一个NegativeButton
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });
        titleBarTbv.setTitle(object.getAnimal().getName());

        name.setRightText(object.getAnimal().getName());
        caijishuliang.setRightText(object.getDataAcquisition().getTotal()+"");
        qixidi.setRightText(object.getQixidi().getName());
        status.setRightText(getAnimalState(object.getZhuangtai()));
        jiangkangshuliang.setRightText(object.getDataAcquisition().getHealthNum()+"");

        if (EmptyUtils.isNotEmpty(object.getDataAcquisition().getHealthPic())){
            Glide.with(mContext).load(Base64.decode(object.getDataAcquisition().getHealthPic(), Base64.DEFAULT)).into(healthLeftIv);
        }

        shengbingshuliang.setRightText(object.getDataAcquisition().getIllNum()+"");
        if (EmptyUtils.isNotEmpty(object.getDataAcquisition().getIllPic())){
            Glide.with(mContext).load(Base64.decode(object.getDataAcquisition().getIllPic(), Base64.DEFAULT)).into(illLeftIv);
        }
        shengbingEt.setText(object.getDataAcquisition().getIllDesc());

        siwangshuliang.setRightText(object.getDataAcquisition().getDeathNum()+"");
        if (EmptyUtils.isNotEmpty(object.getDataAcquisition().getDeathPic())){
            Glide.with(mContext).load(Base64.decode(object.getDataAcquisition().getDeathPic(), Base64.DEFAULT)).into(deathLeftIv);
        }
        siwangEt.setText(object.getDataAcquisition().getDeathDesc());

        juli.setRightText(object.getJuli().getName());
        fangwei.setRightText(object.getFangwei().getName());
        weizhi.setRightText(object.getWeizhi().getName());
        gps.setRightText(object.getDataAcquisition().getLatitude() + "," + object.getDataAcquisition().getLongtitude());
        time.setRightText(object.getDataAcquisition().getCollectionTime());
        if (object.getDataAcquisition().getBubao() == 1){
            bubaoEt.setText(object.getDataAcquisition().getBubaoDesc());
        } else {
            bubaoEt.setText("否");
        }

        submitTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        if (object.getDataAcquisition().getHealthNum() == 0){
            jiangkangtupian.setVisibility(View.GONE);
            healthLeftIv.setVisibility(View.GONE);
        }

        if (object.getDataAcquisition().getIllNum() == 0){
            shengbingtupian.setVisibility(View.GONE);
            illLeftIv.setVisibility(View.GONE);
            shengbingmiaoshu.setVisibility(View.GONE);
            shengbingline.setVisibility(View.GONE);
        }

        if (object.getDataAcquisition().getDeathNum() == 0){
            siwangtupian.setVisibility(View.GONE);
            deathLeftIv.setVisibility(View.GONE);
            siwangmiaoshu.setVisibility(View.GONE);
            siwangline.setVisibility(View.GONE);
        }
    }

    private void getObject(final Long id) {
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
                        showLoading();
                    }
                }).subscribe(new Subscriber<UpObject>() {
            @Override
            public void onCompleted() {
                hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                hideLoading();
                ToastUtils.showShortToast(mContext, e.getMessage());
                finish();
            }

            @Override
            public void onNext(UpObject upObjects) {
                object = upObjects;
                initView();
            }
        });
    }

    private String getAnimalState(Set<Item> zhuangtai) {
        StringBuilder sb = new StringBuilder();
        for (Item item : zhuangtai) {
            sb.append(item.getName()).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    private void submit(){
        final Observable.OnSubscribe<String> onSubscribe = new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                DataAcquisition dataAcquisition = object.getDataAcquisition();
                String result = ApiManager.saveDataAcquisition(dataAcquisition);
                UpEntityManager.deleteById(object.getId());
                subscriber.onNext(result);
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
                }).subscribe(new Subscriber<String>() {
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
            public void onNext(String string) {
                submitTv.setText("已上报");
                submitTv.setEnabled(false);
                titleBarTbv.setRightText("");
                refresh();
            }
        });
    }

    private void refresh(){
        EventBus.getDefault().post(new UpEvent());
    }
}
