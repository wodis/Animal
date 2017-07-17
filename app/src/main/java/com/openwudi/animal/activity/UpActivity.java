package com.openwudi.animal.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.openwudi.animal.R;
import com.openwudi.animal.base.BaseActivity;
import com.openwudi.animal.contract.UpContract;
import com.openwudi.animal.contract.model.UpModel;
import com.openwudi.animal.contract.presenter.UpPresenter;
import com.openwudi.animal.model.Animal;
import com.openwudi.animal.model.ItemEncode;
import com.openwudi.animal.view.TableCellView;
import com.openwudi.animal.view.TitleBarView;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by diwu on 17/7/14.
 */

public class UpActivity extends BaseActivity implements UpContract.View, View.OnClickListener {

    public static final int REQ_CODE_NAME = 101;
    public static final int REQ_CODE_HEALTH_PIC = 102;
    public static final int REQ_CODE_ILL_PIC = 103;
    public static final int REQ_CODE_DEATH_PIC = 104;

    @BindView(R.id.title_bar_tbv)
    TitleBarView titleBarTbv;
    @BindView(R.id.status)
    TableCellView status;
    @BindView(R.id.name)
    TableCellView name;
    @BindView(R.id.qixidi)
    TableCellView qixidi;
    @BindView(R.id.juli)
    TableCellView juli;
    @BindView(R.id.fangwei)
    TableCellView fangwei;
    @BindView(R.id.weizhi)
    TableCellView weizhi;
    @BindView(R.id.caijishuliang)
    TableCellView caijishuliang;
    @BindView(R.id.jiangkangshuliang)
    TableCellView jiangkangshuliang;
    @BindView(R.id.shengbingshuliang)
    TableCellView shengbingshuliang;
    @BindView(R.id.pic_iv)
    ImageView picIv;
    @BindView(R.id.health_left_iv)
    ImageView healthLeftIv;
    @BindView(R.id.health_layout)
    RelativeLayout healthLayout;
    @BindView(R.id.ill_left_iv)
    ImageView illLeftIv;
    @BindView(R.id.ill_layout)
    RelativeLayout illLayout;
    @BindView(R.id.death_left_iv)
    ImageView deathLeftIv;
    @BindView(R.id.death_layout)
    RelativeLayout deathLayout;

    private String pic;

    private UpPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up);
        ButterKnife.bind(this);
        presenter = new UpPresenter();
        presenter.setVM(this, this, new UpModel());

        caijishuliang.setInputType(InputType.TYPE_CLASS_PHONE);
        jiangkangshuliang.setInputType(InputType.TYPE_CLASS_PHONE);
        shengbingshuliang.setInputType(InputType.TYPE_CLASS_PHONE);

        titleBarTbv.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("确定退出吗?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
        name.setOnClickListener(this);
        picIv.setOnClickListener(this);
        status.setOnClickListener(this);
        qixidi.setOnClickListener(this);
        juli.setOnClickListener(this);
        fangwei.setOnClickListener(this);
        weizhi.setOnClickListener(this);
        healthLayout.setOnClickListener(this);
        illLayout.setOnClickListener(this);
        deathLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.name:
                startActivityForResult(new Intent(mContext, AnimalSelectActivity.class), REQ_CODE_NAME);
                break;
            case R.id.qixidi:
                qixidi.setEnabled(false);
                presenter.show(ItemEncode.SJTZ);
                qixidi.setEnabled(true);
                break;
            case R.id.status:
                status.setEnabled(false);
                presenter.show(ItemEncode.DWZT);
                status.setEnabled(true);
                break;
            case R.id.juli:
                juli.setEnabled(false);
                presenter.show(ItemEncode.CJJL);
                juli.setEnabled(true);
                break;
            case R.id.fangwei:
                fangwei.setEnabled(false);
                presenter.show(ItemEncode.CJFW);
                fangwei.setEnabled(true);
                break;
            case R.id.weizhi:
                weizhi.setEnabled(false);
                presenter.show(ItemEncode.CJWZ);
                weizhi.setEnabled(true);
                break;
            case R.id.pic_iv:
                Intent picIntent = new Intent(mContext, PhotoActivity.class);
                picIntent.putExtra("pic", pic);
                startActivity(picIntent);
                break;
            case R.id.health_layout:
                startAlbum(REQ_CODE_HEALTH_PIC);
                break;
            case R.id.ill_layout:
                startAlbum(REQ_CODE_ILL_PIC);
                break;
            case R.id.death_layout:
                startAlbum(REQ_CODE_DEATH_PIC);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQ_CODE_NAME == requestCode && resultCode == RESULT_OK) {
            Animal animal = (Animal) data.getSerializableExtra(Animal.class.getSimpleName());
            name.setRightText(animal.getName());
            if (!TextUtils.isEmpty(animal.getPhoto())) {
                picIv.setVisibility(View.VISIBLE);
                findViewById(R.id.pic_line).setVisibility(View.VISIBLE);
                pic = getString(R.string.PIC_URL) + animal.getPhoto();
                Glide.with(mContext).load(pic).into(picIv);
            } else {
                picIv.setVisibility(View.GONE);
                findViewById(R.id.pic_line).setVisibility(View.GONE);
            }
            presenter.setAnimal(animal);
        } else if (REQ_CODE_HEALTH_PIC == requestCode) {
            List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(data);
            if (localMedias != null && localMedias.size() > 0) {
                for (LocalMedia item : localMedias) {
                    String path = item.getPath();
                    Glide.with(mContext).load(new File(path)).into(healthLeftIv);
                    presenter.setHealth(item);
                }
            }
        } else if (REQ_CODE_ILL_PIC == requestCode) {
            List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(data);
            if (localMedias != null && localMedias.size() > 0) {
                for (LocalMedia item : localMedias) {
                    String path = item.getPath();
                    Glide.with(mContext).load(new File(path)).into(illLeftIv);
                    presenter.setIll(item);
                }
            }
        } else if (REQ_CODE_DEATH_PIC == requestCode) {
            List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(data);
            if (localMedias != null && localMedias.size() > 0) {
                for (LocalMedia item : localMedias) {
                    String path = item.getPath();
                    Glide.with(mContext).load(new File(path)).into(deathLeftIv);
                    presenter.setDeath(item);
                }
            }
        }
    }

    @Override
    public void setQixidi(String string) {
        qixidi.setRightText(string);
    }

    @Override
    public void setZhuangTai(String string) {
        status.setRightText(string);
    }

    @Override
    public void setJuli(String string) {
        juli.setRightText(string);
    }

    @Override
    public void setFangwei(String string) {
        fangwei.setRightText(string);
    }

    @Override
    public void setWeizhi(String string) {
        weizhi.setRightText(string);
    }

    public void startAlbum(int requestCode) {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                .maxSelectNum(1)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(
                        PictureConfig.SINGLE)// 多选 or 单选
                .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                .isCamera(true)// 是否显示拍照按钮
                .compressMode(PictureConfig.SYSTEM_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .openClickSound(false)// 是否开启点击声音
//                .selectionMedia(selectList)// 是否传入已选图片
                .forResult(requestCode);//结果回调onActivityResult code
    }
}
