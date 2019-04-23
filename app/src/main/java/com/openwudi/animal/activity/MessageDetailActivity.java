package com.openwudi.animal.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.utils.TimeUtils;
import com.openwudi.animal.R;
import com.openwudi.animal.base.BaseActivity;
import com.openwudi.animal.model.Message;
import com.openwudi.animal.view.TitleBarView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageDetailActivity extends BaseActivity {

    @BindView(R.id.title_bar_tbv)
    TitleBarView mTitleBarTbv;
    @BindView(R.id.message_tv)
    TextView mMessageTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        ButterKnife.bind(this);
        initData();

        mTitleBarTbv.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        String title;
        String message;
        Message m = new Message();
        if (intent.getData() != null) {
            Uri uri = intent.getData();
            title = uri.getQueryParameter("title");
            message = uri.getQueryParameter("desc");
        } else {
            title = intent.getStringExtra("title");
            message = intent.getStringExtra("desc");
            m = (Message) intent.getSerializableExtra(Message.class.getSimpleName());
        }

        setMessage(title, message + "\n\n\n" + m.getPostName() + "\n" + TimeUtils.date2String(TimeUtils.string2Date(m.getDate().replaceAll("T"," "))));
    }

    private void setMessage(String title, String message) {
        if (!TextUtils.isEmpty(title)) {
            mTitleBarTbv.setTitle(title);
        }
        if (!TextUtils.isEmpty(message)) {
            mMessageTv.setText(message);
            mMessageTv.setVisibility(View.VISIBLE);
        } else {
            mMessageTv.setVisibility(View.GONE);
        }
    }
}
