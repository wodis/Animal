package com.openwudi.animal.base;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;

import com.openwudi.animal.view.LoadingProgressDialog;

/**
 * Created by diwu on 17/7/14.
 */

public class BaseFragment extends Fragment implements BaseView{
    private ProgressDialog mLoadingDialog;

    @Override
    public void showLoading() {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }
        if (mLoadingDialog != null) {
            if (!mLoadingDialog.isShowing()) {
                mLoadingDialog.show();
            }
        } else {
            mLoadingDialog = LoadingProgressDialog.createAndShowLoading(getActivity());
        }
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog == null) {
            return;
        }
        if (mLoadingDialog.isShowing() && !getActivity().isFinishing()) {
            mLoadingDialog.dismiss();
        }
    }
}
