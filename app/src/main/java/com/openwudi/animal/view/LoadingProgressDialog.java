package com.openwudi.animal.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.openwudi.animal.R;

/**
 * Created by haoyonglong on 2017/2/8.
 * 说明：
 */
public class LoadingProgressDialog extends ProgressDialog {
    public LoadingProgressDialog(Context context) {
        super(context);
    }

    public LoadingProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init(getContext());
    }

    private void init(Context context) {
        //设置不可取消，点击其他区域不能取消，实际中可以抽出去封装供外包设置
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.view_loading_progress_dialog);
        AVLoadingIndicatorView loading = (AVLoadingIndicatorView) findViewById(R.id.loading);
        LineSpinFadeLoaderIndicator indicator =new LineSpinFadeLoaderIndicator();
        loading.setIndicator(indicator);
        loading.show();
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }
    }

    @Override
    public void show() {
        super.show();
    }

    public static ProgressDialog createAndShowLoading(Context context) {
        final LoadingProgressDialog progressDialog = new LoadingProgressDialog(context, R.style.LoadingProgressDialog);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        return progressDialog;
    }
}
