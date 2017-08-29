package com.openwudi.animal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blankj.utilcode.utils.EmptyUtils;
import com.blankj.utilcode.utils.TimeUtils;
import com.openwudi.animal.R;
import com.openwudi.animal.base.BaseActivity;
import com.openwudi.animal.contract.HistoryContract;
import com.openwudi.animal.contract.model.HistoryModel;
import com.openwudi.animal.contract.presenter.HistoryPresenter;
import com.openwudi.animal.event.IdEvent;
import com.openwudi.animal.model.DataAcquisition;
import com.openwudi.animal.view.EmptyView;
import com.openwudi.animal.view.MoreListView;
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
 * Created by diwu on 17/7/20.
 */

public class HistoryActivity extends BaseActivity implements HistoryContract.View, MoreListView.OnLoadMoreListener {
    @BindView(R.id.titlebar)
    TitleBarView titlebar;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_qixidi)
    TextView tvQixidi;
    @BindView(R.id.total)
    TextView total;
    @BindView(R.id.lv)
    MoreListView lv;
//    @BindView(R.id.srl)
//    SwipeRefreshLayout srl;
    @BindView(R.id.empty_view)
    EmptyView emptyView;

    private HistoryPresenter presenter;
    private HistoryAdapter adapter;

    private int index = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        presenter = new HistoryPresenter();
        presenter.setVM(this, this, new HistoryModel());
        adapter = new HistoryAdapter();
        lv.setAdapter(adapter);
        emptyView.setImage(R.drawable.tips_ghost);
        emptyView.setText("暂无已上报记录");
//        srl.setColorSchemeColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimary));
//        srl.setOnRefreshListener(this);
        lv.setOnLoadMoreListener(this);
        titlebar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titlebar.setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.startGps();
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
    public void onIdEvent(IdEvent event) {
        List<DataAcquisition> list = adapter.getData();
        DataAcquisition remove = null;
        for (DataAcquisition data : list) {
            if (data.getId().equals(event.getId())) {
                remove = data;
                break;
            }
        }
        list.remove(remove);
        adapter.notifyDataSetChanged();
    }

    public void onRefresh() {
        lv.onLoadMoreEnable();
        index = 0;
        presenter.refresh(index);
    }

    @Override
    public void setData(List<DataAcquisition> data) {
        adapter.setData(data);
//        srl.setRefreshing(false);
        lv.setEmptyView(emptyView);
    }

    @Override
    public void addData(List<DataAcquisition> data) {
        if (EmptyUtils.isNotEmpty(data)) {
            adapter.addData(data);
            lv.onLoadMoreComplete();
        } else {
            lv.onLoadMoreDisable();
            lv.onLoadMoreComplete();
        }
    }

    @Override
    public void onLoadMore() {
        index++;
        presenter.refresh(index);
    }


    public class HistoryAdapter extends BaseAdapter {

        private List<DataAcquisition> data = new ArrayList<>();

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public DataAcquisition getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.view_save_item, null);
                convertView.setTag(new ViewHolder(convertView));
            }
            final DataAcquisition object = getItem(position);
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            Date date = TimeUtils.string2Date(object.getCollectionTime().replaceAll("T", " "));
            viewHolder.tvDate.setText(TimeUtils.date2String(date, new SimpleDateFormat("MM-dd HH时")));
            viewHolder.tvName.setText(object.getAnimalName());
            viewHolder.tvQixidi.setText(presenter.getQixidiName(object.getHabitate()));
            viewHolder.total.setText(object.getTotal() + "");

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startDetail(getItem(position));
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

        public void setData(List<DataAcquisition> newData) {
            data.clear();
            data.addAll(newData);
            notifyDataSetChanged();
        }

        public List<DataAcquisition> getData() {
            return data;
        }

        public void addData(List<DataAcquisition> newData) {
            data.addAll(newData);
            notifyDataSetChanged();
        }

        private void startDetail(DataAcquisition data) {
            Intent i = new Intent(mContext, UpDetailActivity.class);
            i.putExtra(DataAcquisition.class.getSimpleName(), data);
            startActivity(i);
        }
    }
}
