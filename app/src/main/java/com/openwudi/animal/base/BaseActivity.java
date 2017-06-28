package com.openwudi.animal.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.openwudi.animal.R;
import com.openwudi.animal.view.LoadingProgressDialog;

/**
 * Created by diwu on 17/6/27.
 */

public class BaseActivity extends AppCompatActivity implements BaseView{
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

    @Override
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

    @Override
    public void hideLoading() {
        if (mLoadingDialog == null) {
            return;
        }
        if (mLoadingDialog.isShowing() && !isFinishing()) {
            mLoadingDialog.dismiss();
        }
    }

    /**
     * 设置点击监听器
     *
     * @param listener
     */
    public void setOnClickListener(View.OnClickListener listener) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout_top);
        LinearLayout optionsButton = (LinearLayout) layout.findViewById(R.id.btn_activity_options);
        optionsButton.setOnClickListener(listener);
    }

    /**
     * 回退事件
     *
     * @param v
     */
    public void onBack(View v) {
        super.onBackPressed();
    }
}
