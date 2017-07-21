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
import com.openwudi.animal.contract.UpDetailContract;
import com.openwudi.animal.contract.model.UpDetailModel;
import com.openwudi.animal.contract.presenter.UpDetailPresenter;
import com.openwudi.animal.db.manager.UpEntityManager;
import com.openwudi.animal.event.UpEvent;
import com.openwudi.animal.manager.ApiManager;
import com.openwudi.animal.model.Animal;
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

public class UpDetailActivity extends BaseActivity implements UpDetailContract.View, View.OnClickListener {

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

    private UpDetailPresenter presenter;

    private boolean isUp = true;

    private UpObject object;

    private DataAcquisition dataAcquisition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_detail);
        ButterKnife.bind(this);
        presenter = new UpDetailPresenter();
        presenter.setVM(this, this, new UpDetailModel());
        Long id = getIntent().getLongExtra("id", 0);
        dataAcquisition = (DataAcquisition) getIntent().getSerializableExtra(DataAcquisition.class.getSimpleName());

        if (dataAcquisition != null) {
            isUp = false;
        }

        if (isUp) {
            presenter.getObject(id);
        } else {
            presenter.getObjectByData(dataAcquisition);
        }
    }


    @Override
    public void initView() {
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
                        if (isUp) {
                            UpEntityManager.deleteById(object.getId());
                            refresh();
                            finish();
                        } else {
                            presenter.delete(dataAcquisition.getId());
                        }
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
        caijishuliang.setRightText(object.getDataAcquisition().getTotal() + "");
        if (EmptyUtils.isNotEmpty(object.getQixidi())) {
            qixidi.setRightText(object.getQixidi().getName());
        }
        if (EmptyUtils.isNotEmpty(object.getZhuangtai())) {
            status.setRightText(getAnimalState(object.getZhuangtai()));
            status.setOnClickListener(this);
        }
        jiangkangshuliang.setRightText(object.getDataAcquisition().getHealthNum() + "");

        if (EmptyUtils.isNotEmpty(object.getDataAcquisition().getHealthPic())) {
            Glide.with(mContext).load(Base64.decode(object.getDataAcquisition().getHealthPic(), Base64.DEFAULT)).into(healthLeftIv);
            healthLeftIv.setOnClickListener(this);
        }

        shengbingshuliang.setRightText(object.getDataAcquisition().getIllNum() + "");
        if (EmptyUtils.isNotEmpty(object.getDataAcquisition().getIllPic())) {
            Glide.with(mContext).load(Base64.decode(object.getDataAcquisition().getIllPic(), Base64.DEFAULT)).into(illLeftIv);
            illLeftIv.setOnClickListener(this);
        }
        shengbingEt.setText(object.getDataAcquisition().getIllDesc());

        siwangshuliang.setRightText(object.getDataAcquisition().getDeathNum() + "");
        if (EmptyUtils.isNotEmpty(object.getDataAcquisition().getDeathPic())) {
            Glide.with(mContext).load(Base64.decode(object.getDataAcquisition().getDeathPic(), Base64.DEFAULT)).into(deathLeftIv);
            deathLeftIv.setOnClickListener(this);
        }
        siwangEt.setText(object.getDataAcquisition().getDeathDesc());

        if (EmptyUtils.isNotEmpty(object.getJuli())) {
            juli.setRightText(object.getJuli().getName());
        } else {
            juli.setRightText("未选择");
        }

        if (EmptyUtils.isNotEmpty(object.getFangwei())) {
            fangwei.setRightText(object.getFangwei().getName());
        } else {
            fangwei.setRightText("未选择");
        }

        if (EmptyUtils.isNotEmpty(object.getWeizhi())) {
            weizhi.setRightText(object.getWeizhi().getName());
        } else {
            weizhi.setRightText("未选择");
        }

        gps.setRightText(object.getDataAcquisition().getLatitude().trim() + "," + object.getDataAcquisition().getLongtitude().trim());
        gps.setOnClickListener(this);

        time.setRightText(object.getDataAcquisition().getCollectionTime().replaceAll("T", " "));
        if (object.getDataAcquisition().getBubao() == 1) {
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

        if (object.getDataAcquisition().getHealthNum() == 0) {
            jiangkangtupian.setVisibility(View.GONE);
            healthLeftIv.setVisibility(View.GONE);
        }

        if (object.getDataAcquisition().getIllNum() == 0) {
            shengbingtupian.setVisibility(View.GONE);
            illLeftIv.setVisibility(View.GONE);
            shengbingmiaoshu.setVisibility(View.GONE);
            shengbingline.setVisibility(View.GONE);
        }

        if (object.getDataAcquisition().getDeathNum() == 0) {
            siwangtupian.setVisibility(View.GONE);
            deathLeftIv.setVisibility(View.GONE);
            siwangmiaoshu.setVisibility(View.GONE);
            siwangline.setVisibility(View.GONE);
        }

        if (EmptyUtils.isNotEmpty(object.getAnimal().getPhoto())) {
            picIv.setVisibility(View.VISIBLE);
            findViewById(R.id.pic_line).setVisibility(View.VISIBLE);
            String pic = getString(R.string.PIC_URL) + object.getAnimal().getPhoto();
            Glide.with(mContext).load(pic).into(picIv);
            picIv.setOnClickListener(this);
        }

        name.setRightDrawable(0);
        caijishuliang.setRightDrawable(0);
        qixidi.setRightDrawable(0);
        status.setRightDrawable(0);
        jiangkangshuliang.setRightDrawable(0);
        jiangkangtupian.setRightDrawable(0);
        shengbingshuliang.setRightDrawable(0);
        shengbingtupian.setRightDrawable(0);
        siwangshuliang.setRightDrawable(0);
        siwangtupian.setRightDrawable(0);
        juli.setRightDrawable(0);
        fangwei.setRightDrawable(0);
        weizhi.setRightDrawable(0);
        gps.setRightDrawable(0);
        time.setRightDrawable(0);

        if (!isUp) {
            submitTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void setObject(UpObject object) {
        this.object = object;
    }

    private String getAnimalState(Set<Item> zhuangtai) {
        StringBuilder sb = new StringBuilder();
        for (Item item : zhuangtai) {
            sb.append(item.getName()).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    private void submit() {
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

    private void refresh() {
        EventBus.getDefault().post(new UpEvent());
    }

    @Override
    public void onClick(View v) {
        Intent i = null;
        switch (v.getId()) {
            case R.id.health_left_iv:
                i = new Intent(mContext, PhotoActivity.class);
                i.putExtra(Base64.class.getSimpleName(), object.getDataAcquisition().getHealthPic());
                startActivity(i);
                break;
            case R.id.ill_left_iv:
                i = new Intent(mContext, PhotoActivity.class);
                i.putExtra(Base64.class.getSimpleName(), object.getDataAcquisition().getIllPic());
                startActivity(i);
                break;
            case R.id.death_left_iv:
                i = new Intent(mContext, PhotoActivity.class);
                i.putExtra(Base64.class.getSimpleName(), object.getDataAcquisition().getDeathPic());
                startActivity(i);
                break;
            case R.id.pic_iv:
                String pic = getString(R.string.PIC_URL) + object.getAnimal().getPhoto();
                i = new Intent(mContext, PhotoActivity.class);
                i.putExtra("pic", pic);
                startActivity(i);
                break;
            case R.id.status:
                ToastUtils.showShortToast(mContext, status.getRightText());
                break;
            case R.id.gps:
                if (EmptyUtils.isNotEmpty(object.getDataAcquisition().getLatitude().trim()) && EmptyUtils.isNotEmpty(object.getDataAcquisition().getLongtitude().trim())) {
                    i = new Intent(mContext, MapActivity.class);
                    i.putExtra("lat", object.getDataAcquisition().getLatitude().trim());
                    i.putExtra("lon", object.getDataAcquisition().getLongtitude().trim());
                    startActivity(i);
                }
                break;
        }
    }
}
