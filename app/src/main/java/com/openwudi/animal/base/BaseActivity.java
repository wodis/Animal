package com.openwudi.animal.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.openwudi.animal.R;
import com.openwudi.animal.view.LoadingProgressDialog;

/**
 * Created by diwu on 17/6/27.
 */

public class BaseActivity extends AppCompatActivity {
    protected BaseActivity mContext;
    private ProgressDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        this.setStatusBarColor();
    }

    @Override
    protected void onDestroy() {
        //加载Loading对话框释放
        if (mLoadingDialog != null) {
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
            mLoadingDialog = null;
        }
        super.onDestroy();
    }

    protected void setStatusBarColor() {
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.color7), 0);
    }

    public void showLoading() {
        if (isFinishing()) {
            return;
        }
        if (mLoadingDialog != null) {
            if (!mLoadingDialog.isShowing()) {
                mLoadingDialog.show();
            }
        } else {
            mLoadingDialog = LoadingProgressDialog.createAndShowLoading(this);
        }
    }

    public void hideLoading() {
        if (mLoadingDialog == null) {
            return;
        }
        if (mLoadingDialog.isShowing() && !isFinishing()) {
            mLoadingDialog.dismiss();
        }
    }
}
