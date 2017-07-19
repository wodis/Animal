package com.openwudi.animal.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.utils.EmptyUtils;
import com.blankj.utilcode.utils.TimeUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.openwudi.animal.R;
import com.openwudi.animal.activity.MessageDetailActivity;
import com.openwudi.animal.base.BaseFragment;
import com.openwudi.animal.manager.ApiManager;
import com.openwudi.animal.model.Message;
import com.openwudi.animal.utils.TimeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by diwu on 17/4/24.
 */

public class MessageFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.lv)
    ListView lv;
    Unbinder unbinder;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    private MessageAdapter adapter;

    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_message, container, false);
        unbinder = ButterKnife.bind(this, view);
        adapter = new MessageAdapter();
        lv.setAdapter(adapter);
        srl.setColorSchemeColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimary));
        srl.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            refresh();
        }
    }

    private void refresh() {
        final Observable.OnSubscribe<List<Message>> onSubscribe = new Observable.OnSubscribe<List<Message>>() {
            @Override
            public void call(Subscriber<? super List<Message>> subscriber) {
                List<Message> messageList = ApiManager.listMessage();
                subscriber.onNext(messageList);
                subscriber.onCompleted();
            }
        };

        Observable.create(onSubscribe)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        srl.setRefreshing(true);
                        showLoading();
                    }
                }).subscribe(new Subscriber<List<Message>>() {
            @Override
            public void onCompleted() {
                hideLoading();
                srl.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                hideLoading();
                ToastUtils.showShortToast(getActivity(), e.getMessage());
                srl.setRefreshing(false);
            }

            @Override
            public void onNext(List<Message> messageList) {
                adapter.setMessage(messageList);
            }
        });
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    public class MessageAdapter extends BaseAdapter {

        private List<Message> data = new ArrayList<>();

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Message getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.view_message, null);
                convertView.setTag(new ViewHolder(convertView));
            }

            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            final Message message = getItem(position);
            viewHolder.tvText1.setText(message.getTitle());
            viewHolder.tvText2.setText(message.getContent());
            try {
                viewHolder.tvRight.setText(TimeUtils.date2String(TimeUtils.string2Date(message.getDate().replaceAll("T"," "))));
            } catch (Exception e) {
                e.printStackTrace();
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), MessageDetailActivity.class);
                    i.putExtra("title", message.getTitle());
                    i.putExtra("desc", message.getContent());
                    i.putExtra(Message.class.getSimpleName(), message);
                    startActivity(i);
                }
            });

            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.tv_right)
            TextView tvRight;
            @BindView(R.id.tv_text_1)
            TextView tvText1;
            @BindView(R.id.tv_text_2)
            TextView tvText2;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }

        public void setMessage(List<Message> newMessage) {
            if (EmptyUtils.isEmpty(newMessage)) {
                return;
            }
            data.clear();
            data.addAll(newMessage);
            notifyDataSetChanged();
        }
    }
}
