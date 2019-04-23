package com.openwudi.animal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.EmptyUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.bumptech.glide.Glide;
import com.openwudi.animal.R;
import com.openwudi.animal.base.BaseActivity;
import com.openwudi.animal.model.Animal;
import com.openwudi.animal.view.TableCellView;
import com.openwudi.animal.view.TitleBarView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by diwu on 17/7/24.
 */

public class AnimalDetailActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.title_bar_tbv)
    TitleBarView titleBarTbv;
    @BindView(R.id.name)
    TableCellView name;
    @BindView(R.id.pic_iv)
    ImageView picIv;
    @BindView(R.id.pic_line)
    View picLine;
    @BindView(R.id.fenbu)
    TextView fenbu;
    @BindView(R.id.timaotezheng)
    TextView timaotezheng;
    @BindView(R.id.shenghuohuanjing)
    TextView shenghuohuanjing;
    @BindView(R.id.ldname)
    TableCellView ldname;

    private Animal animal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_detail);
        ButterKnife.bind(this);
        animal = (Animal) getIntent().getSerializableExtra(Animal.class.getSimpleName());

        if (animal == null) {
            ToastUtils.showShortToast(mContext, "物种内容为空");
            finish();
            return;
        }

        titleBarTbv.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleBarTbv.setTitle(animal.getName());
        name.setRightText(animal.getName());
        ldname.setRightText(animal.getLdname());
        name.setRightDrawable(0);
        ldname.setRightDrawable(0);
        ldname.setOnClickListener(this);
        if (EmptyUtils.isNotEmpty(animal.getPhoto())) {
            picIv.setVisibility(View.VISIBLE);
            findViewById(R.id.pic_line).setVisibility(View.VISIBLE);
            String pic = getString(R.string.PIC_URL) + animal.getPhoto();
            Glide.with(mContext).load(pic).into(picIv);
            picIv.setOnClickListener(this);
        }

        if (EmptyUtils.isNotEmpty(animal.getDistribution())){
            fenbu.setText(animal.getDistribution());
        } else {
            fenbu.setText("未记录");
        }

        if (EmptyUtils.isNotEmpty(animal.getPhysicalFeatures())){
            timaotezheng.setText(animal.getPhysicalFeatures());
        } else {
            timaotezheng.setText("未记录");
        }

        if (EmptyUtils.isNotEmpty(animal.getEnvironment())){
            shenghuohuanjing.setText(animal.getEnvironment());
        } else {
            shenghuohuanjing.setText("未记录");
        }

    }

    @Override
    public void onClick(View v) {
        Intent i = null;
        switch (v.getId()) {
            case R.id.pic_iv:
                String pic = getString(R.string.PIC_URL) + animal.getPhoto();
                i = new Intent(mContext, PhotoActivity.class);
                i.putExtra("pic", pic);
                startActivity(i);
                break;
            case R.id.ldname:
                ToastUtils.showShortToast(mContext, animal.getLdname());
                break;
        }
    }
}
