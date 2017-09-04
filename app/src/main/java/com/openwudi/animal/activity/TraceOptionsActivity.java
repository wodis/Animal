package com.openwudi.animal.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.utils.ConstUtils;
import com.blankj.utilcode.utils.RegexUtils;
import com.blankj.utilcode.utils.SPUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.openwudi.animal.R;
import com.openwudi.animal.base.AnimalApplication;
import com.openwudi.animal.base.BaseActivity;
import com.openwudi.animal.view.TableCellView;
import com.openwudi.animal.view.TitleBarView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by diwu on 17/7/24.
 */

public class TraceOptionsActivity extends BaseActivity implements View.OnClickListener {

    public static final String GPS_OPTIONS = "gps_options";

    public static final String GATHER = "gather";

    public static final String PACK = "pack";

    @BindView(R.id.title_bar_tbv)
    TitleBarView titleBarTbv;
    @BindView(R.id.caijipinlv)
    TableCellView caijipinlv;
    @BindView(R.id.dabaopinlv)
    TableCellView dabaopinlv;
    @BindView(R.id.save_tv)
    TextView saveTv;

    SPUtils spUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trace_options);
        ButterKnife.bind(this);

        spUtils = new SPUtils(mContext, GPS_OPTIONS);
        int gater = spUtils.getInt(GATHER, 5);
        int pack = spUtils.getInt(PACK, 30);

        caijipinlv.setRightText(gater + "");
        dabaopinlv.setRightText(pack + "");

        caijipinlv.setInputType(InputType.TYPE_CLASS_PHONE);
        dabaopinlv.setInputType(InputType.TYPE_CLASS_PHONE);

        caijipinlv.setInputMaxLength(5);
        dabaopinlv.setInputMaxLength(5);

        titleBarTbv.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveTv.setOnClickListener(this);

    }

    private boolean check() {
        String caiji = caijipinlv.getRightRealString();
        boolean check = false;
        if (RegexUtils.isMatch(ConstUtils.REGEX_POSITIVE_INTEGER, caiji)) {
            check = true;
        } else {
            return false;
        }

        String dabao = dabaopinlv.getRightRealString();
        if (RegexUtils.isMatch(ConstUtils.REGEX_POSITIVE_INTEGER, dabao)) {
            check = true;
        } else {
            return false;
        }

        return check;
    }

    @Override
    public void onClick(View v) {
        if (check()) {
            int gatherInterval = Integer.parseInt(caijipinlv.getRightRealString());
            int packInterval = Integer.parseInt(dabaopinlv.getRightRealString());
            AnimalApplication.INSTANCE.mClient.setInterval(gatherInterval, packInterval);
            spUtils.putInt(GATHER, gatherInterval);
            spUtils.putInt(PACK, packInterval);
            ToastUtils.showShortToast(mContext, "设置成功");
            finish();
        } else {
            ToastUtils.showShortToast(mContext, "请输入正确的参数");
        }
    }
}
