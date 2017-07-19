package com.openwudi.animal.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.utils.TimeUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.openwudi.animal.R;
import com.openwudi.animal.base.BaseActivity;
import com.openwudi.animal.contract.UpSaveContract;
import com.openwudi.animal.contract.model.UpSaveModel;
import com.openwudi.animal.contract.presenter.UpSavePresenter;
import com.openwudi.animal.db.UpEntity;
import com.openwudi.animal.db.manager.UpEntityManager;
import com.openwudi.animal.event.UpEvent;
import com.openwudi.animal.model.UpObject;
import com.openwudi.animal.view.TitleBarView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by diwu on 17/7/18.
 */

public class UpSaveActivity extends BaseActivity implements UpSaveContract.View {
    @BindView(R.id.titlebar)
    TitleBarView titlebar;
    @BindView(R.id.lv)
    ListView lv;

    private UpSaveAdapter adapter;
    private UpSavePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new UpSavePresenter();
        presenter.setVM(this, this, new UpSaveModel());
        setContentView(R.layout.activity_up_save);
        ButterKnife.bind(this);
        adapter = new UpSaveAdapter();
        lv.setAdapter(adapter);
        presenter.refresh();

        titlebar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titlebar.setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("确定上报吗?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<UpObject> list = adapter.getData();
                        if (list.size() > 0) {
                            presenter.report(list);
                        } else {
                            ToastUtils.showShortToast(mContext, "无上报数据");
                        }
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

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpEvent(UpEvent event) {
        presenter.refresh();
    }

    @Override
    public void setData(List<UpObject> list) {
        adapter.setData(list);
    }

    public class UpSaveAdapter extends BaseAdapter {

        private List<UpObject> data = new ArrayList<>();

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public UpObject getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.view_save_item, null);
                convertView.setTag(new ViewHolder(convertView));
            }
            final UpObject object = getItem(position);
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            Date date = TimeUtils.string2Date(object.getDataAcquisition().getCollectionTime());
            viewHolder.tvDate.setText(TimeUtils.date2String(date, new SimpleDateFormat("MM-dd HH时")));
            viewHolder.tvName.setText(object.getAnimal().getName());
            viewHolder.tvQixidi.setText(object.getQixidi().getName());
            viewHolder.total.setText(object.getDataAcquisition().getTotal() + "");

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startDetail(object);
                }
            });

            return convertView;
        }

        public class ViewHolder {
            @BindView(R.id.tv_date)
            TextView tvDate;
            @BindView(R.id.tv_name)
            TextView tvName;
            @BindView(R.id.tv_qixidi)
            TextView tvQixidi;
            @BindView(R.id.total)
            TextView total;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }

        public void setData(List<UpObject> newData) {
            data.clear();
            data.addAll(newData);
            notifyDataSetChanged();
        }

        public List<UpObject> getData() {
            return data;
        }

        private void startDetail(UpObject object) {
            Intent i = new Intent(mContext, UpDetailActivity.class);
            i.putExtra("id", object.getId().longValue());
            startActivity(i);
        }
    }
}
