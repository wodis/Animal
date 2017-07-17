package com.openwudi.animal.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.entity.LocalMedia;
import com.openwudi.animal.R;
import com.openwudi.animal.base.BaseActivity;
import com.openwudi.animal.model.Animal;
import com.openwudi.animal.view.TitleBarView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by diwu on 17/7/17.
 */

public class PhotoActivity extends BaseActivity {
    @BindView(R.id.photo_view)
    PhotoView photoView;
    @BindView(R.id.title_bar_tbv)
    TitleBarView titleBarTbv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);
        String pic = getIntent().getStringExtra("pic");
        LocalMedia animal = (LocalMedia) getIntent().getSerializableExtra(Animal.class.getSimpleName());
        if (!TextUtils.isEmpty(pic)){
            Glide.with(this).load(pic).into(photoView);
        } else if (animal != null){
            Glide.with(this).load(new File(animal.getPath())).into(photoView);
        }

        titleBarTbv.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
