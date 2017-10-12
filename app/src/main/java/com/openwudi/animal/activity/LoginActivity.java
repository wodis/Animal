package com.openwudi.animal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.utils.RegexUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.openwudi.animal.R;
import com.openwudi.animal.SplashActivity;
import com.openwudi.animal.base.BaseActivity;
import com.openwudi.animal.base.StatusBarCompat;
import com.openwudi.animal.manager.AccountManager;
import com.openwudi.animal.manager.ApiManager;
import com.openwudi.animal.model.Account;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by diwu on 17/6/27.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.account)
    EditText account;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.reg_btn)
    TextView regBtn;

    @Override
    protected void setStatusBarColor() {
        StatusBarCompat.translucentStatusBar(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, RegisterActivity.class));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(account.getText().toString())) {
                    ToastUtils.showShortToast(mContext, "请输入正确的用户名");
                    return;
                }

                if (TextUtils.isEmpty(password.getText().toString())) {
                    ToastUtils.showShortToast(mContext, "请输入正确的密码格式");
                    return;
                }

                final Observable.OnSubscribe<Account> onSubscribe = new Observable.OnSubscribe<Account>() {
                    @Override
                    public void call(Subscriber<? super Account> subscriber) {
                        Account result = ApiManager.login(account.getText().toString(), password.getText().toString());
                        AccountManager.setAccount(result);
                        subscriber.onNext(result);
                        subscriber.onCompleted();
                    }
                };

                Observable.create(onSubscribe)
                        .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                button.setEnabled(false);
                                showLoading();
                            }
                        }).subscribe(new Subscriber<Account>() {
                    @Override
                    public void onCompleted() {
                        button.setEnabled(true);
                        hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        button.setEnabled(true);
                        hideLoading();
                        ToastUtils.showShortToast(mContext, e.getMessage());
                    }

                    @Override
                    public void onNext(Account account) {
                        ToastUtils.showShortToast(mContext, account.getUserCode() + "登录成功");
                        startActivities(new Intent[]{new Intent(mContext, MainActivity.class), new Intent(mContext, TraceActivity.class)});
                        finish();
                    }
                });
            }
        });
    }
}
