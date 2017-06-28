package com.openwudi.animal;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.blankj.utilcode.utils.ToastUtils;
import com.openwudi.animal.activity.LoginActivity;
import com.openwudi.animal.activity.TraceActivity;
import com.openwudi.animal.utils.BitmapUtil;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by diwu on 17/6/27.
 */
public class SplashActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    public static final int DURATION = 2000;
    private static final int REQ_WRITE_SD_CARD_PERMISSION = 130;
    private static final int REQ_SETTING_RESULT = 201;

    /**
     * 所需权限
     */
    private String[] mPerms = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS
    };

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                startActivity(new Intent(SplashActivity.this, TraceActivity.class));
                finish();
                overridePendingTransition(0, 0);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BitmapUtil.init();
        hideNavigationBar();
        checkSDCardPermission();
    }

    public void hideNavigationBar() {
        int uiFlags = 0; // hide status bar
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
        }
        if (android.os.Build.VERSION.SDK_INT >= 19) {
            uiFlags |= 0x00001000;
        } else {
            uiFlags |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }
        getWindow().getDecorView().setSystemUiVisibility(uiFlags);
    }

    /**
     * 检查SD卡权限
     */
    private void checkSDCardPermission() {
        if (EasyPermissions.hasPermissions(this, mPerms)) {
            mHandler.sendEmptyMessageDelayed(0, DURATION);
        } else {
            EasyPermissions.requestPermissions(this, "需要相关权限, 否则无法运行",
                    REQ_WRITE_SD_CARD_PERMISSION, mPerms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == REQ_WRITE_SD_CARD_PERMISSION && perms.size() >= mPerms.length) {
            mHandler.sendEmptyMessageDelayed(0, DURATION);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        try {
            new AppSettingsDialog.Builder(SplashActivity.this, "需要相关权限, 否则无法运行")
                    .setPositiveButton("确定")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setRequestCode(REQ_SETTING_RESULT)
                    .build()
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showShortToast(SplashActivity.this, "需要相关权限, 否则无法运行");
        }
    }
}
