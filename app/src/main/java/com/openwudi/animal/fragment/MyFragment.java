package com.openwudi.animal.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openwudi.animal.R;
import com.openwudi.animal.activity.LoginActivity;
import com.openwudi.animal.activity.TraceOptionsActivity;
import com.openwudi.animal.manager.AccountManager;
import com.openwudi.animal.view.TableCellView;
import com.openwudi.animal.view.TitleBarView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by diwu on 17/4/24.
 */

public class MyFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.title_bar_tbv)
    TitleBarView titleBarTbv;
    Unbinder unbinder;
    @BindView(R.id.gps)
    TableCellView gps;

    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_my, container, false);
        unbinder = ButterKnife.bind(this, view);
        titleBarTbv.setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("确定退出吗?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AccountManager.clearAccount();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
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

        gps.setOnClickListener(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gps:
                getActivity().startActivity(new Intent(getActivity(), TraceOptionsActivity.class));
                break;
        }
    }
}
