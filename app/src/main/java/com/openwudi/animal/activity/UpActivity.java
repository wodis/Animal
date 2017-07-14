package com.openwudi.animal.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.openwudi.animal.R;
import com.openwudi.animal.base.BaseActivity;
import com.openwudi.animal.contract.UpContract;
import com.openwudi.animal.contract.model.UpModel;
import com.openwudi.animal.contract.presenter.UpPresenter;
import com.openwudi.animal.model.ItemEncode;
import com.openwudi.animal.view.TableCellView;
import com.openwudi.animal.view.TitleBarView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by diwu on 17/7/14.
 */

public class UpActivity extends BaseActivity implements UpContract.View, View.OnClickListener {

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

    private UpPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up);
        ButterKnife.bind(this);
        presenter = new UpPresenter();
        presenter.setVM(this, this, new UpModel());

        titleBarTbv.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        status.setOnClickListener(this);
        qixidi.setOnClickListener(this);
        juli.setOnClickListener(this);
        fangwei.setOnClickListener(this);
        weizhi.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qixidi:
                qixidi.setEnabled(false);
                presenter.show(ItemEncode.DWZT);
                qixidi.setEnabled(true);
                break;
            case R.id.status:
                status.setEnabled(false);
                presenter.show(ItemEncode.SJTZ);
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
        }
    }
}
