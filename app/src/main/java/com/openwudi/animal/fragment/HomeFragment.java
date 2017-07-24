package com.openwudi.animal.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.utils.ConvertUtils;
import com.bumptech.glide.Glide;
import com.openwudi.animal.R;
import com.openwudi.animal.activity.AnimalDetailSelectActivity;
import com.openwudi.animal.activity.HistoryActivity;
import com.openwudi.animal.activity.LoginActivity;
import com.openwudi.animal.activity.TraceActivity;
import com.openwudi.animal.activity.TraceOptionsActivity;
import com.openwudi.animal.activity.UpActivity;
import com.openwudi.animal.activity.UpSaveActivity;
import com.openwudi.animal.event.TabEvent;
import com.openwudi.animal.model.SortItem;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by diwu on 17/4/24.
 */

public class HomeFragment extends Fragment {
    @BindView(R.id.gview)
    GridView gview;
    @BindView(R.id.sv)
    ScrollView sv;
    Unbinder unbinder;

    private static List<SortItem> data = new ArrayList<>();

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        SortAdapter adapter = new SortAdapter(getActivity());
        gview.setAdapter(adapter);

        if (data.isEmpty()) {
            data.add(new SortItem("信息采集", R.drawable.icon_xinxi));
            data.add(new SortItem("保存列表", R.drawable.icon_ip));
            data.add(new SortItem("历史记录", R.drawable.icon_lishi));
            data.add(new SortItem("路径信息", R.drawable.icon_lujing));
            data.add(new SortItem("通知信息", R.drawable.icon_tongzhi));
            data.add(new SortItem("紧急电话", R.drawable.icon_jinjidianhua));
            data.add(new SortItem("GPS频率", R.drawable.icon_gps));
            data.add(new SortItem("科普知识", R.drawable.icon_kepuzhishi));
        }
        adapter.set(data);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public static class SortAdapter extends BaseAdapter {
        private Context context;
        private List<SortItem> data = new ArrayList<>();
        private int dp2;
        private int dp1;

        public SortAdapter(Context context) {
            this.context = context;
            this.dp2 = ConvertUtils.dp2px(context, 2);
            this.dp1 = ConvertUtils.dp2px(context, 1);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public SortItem getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.view_sort_item, null);
                convertView.setTag(new ViewHolder(convertView));
            }

            final SortItem sortItem = getItem(position);
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.tv.setText(sortItem.getText());
            Glide.with(context).load(sortItem.getPic()).centerCrop().into(viewHolder.iv);
            if (position % 2 == 0) {
                if (position == 0) {
                    convertView.setPadding(dp2, dp2, dp1, dp1);
                } else {
                    convertView.setPadding(dp2, dp1, dp1, dp1);
                }
            } else {
                if (position == 1) {
                    convertView.setPadding(dp1, dp2, dp2, dp1);
                } else {
                    convertView.setPadding(dp1, dp1, dp2, dp1);
                }
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    start(position);
                }
            });

            return convertView;
        }

        static class ViewHolder {
            @BindView(R.id.iv)
            ImageView iv;
            @BindView(R.id.tv)
            TextView tv;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }

        public void set(List<SortItem> newData) {
            data.clear();
            data.addAll(newData);
            notifyDataSetChanged();
        }

        private void start(int position) {
            switch (position) {
                case 0:
                    context.startActivity(new Intent(context, UpActivity.class));
                    break;
                case 1:
                    context.startActivity(new Intent(context, UpSaveActivity.class));
                    break;
                case 2:
                    context.startActivity(new Intent(context, HistoryActivity.class));
                    break;
                case 3:
                    gps();
                    break;
                case 4:
                    EventBus.getDefault().post(new TabEvent(1));
                    break;
                case 5:
                    call();
                    break;
                case 6:
                    context.startActivity(new Intent(context, TraceOptionsActivity.class));
                    break;
                case 7:
                    context.startActivity(new Intent(context, AnimalDetailSelectActivity.class));
                    break;
                case 8:
                    break;
            }
        }

        private void call() {
            String phone = "62021756";
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        private void gps(){
            context.startActivity(new Intent(context, TraceActivity.class));
        }
    }
}
