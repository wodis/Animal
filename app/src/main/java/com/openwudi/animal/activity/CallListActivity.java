package com.openwudi.animal.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.ConstUtils;
import com.blankj.utilcode.utils.EmptyUtils;
import com.blankj.utilcode.utils.RegexUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.openwudi.animal.R;
import com.openwudi.animal.base.BaseActivity;
import com.openwudi.animal.db.TelEntity;
import com.openwudi.animal.db.manager.TelEntityManager;
import com.openwudi.animal.view.TableCellView;
import com.openwudi.animal.view.TitleBarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by diwu on 2017/8/29.
 */

public class CallListActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title_bar_tbv)
    TitleBarView titleBarTbv;
    @BindView(R.id.lv)
    ListView lv;

    CallAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calllist);
        ButterKnife.bind(this);
        titleBarTbv.setLeftListener(this);
        titleBarTbv.setRightListener(this);
        adapter = new CallAdapter();
        lv.setAdapter(adapter);
        init();
    }

    private void init() {
        final Observable.OnSubscribe<List<TelEntity>> onSubscribe = new Observable.OnSubscribe<List<TelEntity>>() {
            @Override
            public void call(Subscriber<? super List<TelEntity>> subscriber) {
                List<TelEntity> items = TelEntityManager.listAll();
                TelEntity first = new TelEntity();
                first.setName("北京市防控野生动物疫病领导小组办公室");
                first.setTel("62021756");
                items.add(0, first);
                subscriber.onNext(items);
                subscriber.onCompleted();
            }
        };

        Observable.create(onSubscribe)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showLoading();
                    }
                }).subscribe(new Subscriber<List<TelEntity>>() {
            @Override
            public void onCompleted() {
                hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                hideLoading();
                ToastUtils.showShortToast(mContext, e.getMessage());
            }

            @Override
            public void onNext(List<TelEntity> itemList) {
                adapter.setData(itemList);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left:
                finish();
                break;
            case R.id.right_tv:
                add();
                break;
            default:
                break;
        }
    }

    public void add() {
        View view = View.inflate(mContext, R.layout.view_input_call, null);
        final TableCellView name = (TableCellView) view.findViewById(R.id.name);
        final TableCellView tel = (TableCellView) view.findViewById(R.id.tel);
        tel.setInputType(InputType.TYPE_CLASS_PHONE);
        tel.setInputMaxLength(13);
        name.setInputMaxLength(20);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (EmptyUtils.isNotEmpty(name.getRightText()) && (isTel(tel.getRightText()))) {
                    TelEntity newEntity = new TelEntity();
                    newEntity.setName(name.getRightText());
                    newEntity.setTel(tel.getRightText());
                    TelEntityManager.add(newEntity);
                    init();
                } else {
                    ToastUtils.showShortToast(mContext,"请输入正确的电话号码");
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

    private void modify(final TelEntity entity){
        View view = View.inflate(mContext, R.layout.view_input_call, null);
        final TableCellView name = (TableCellView) view.findViewById(R.id.name);
        final TableCellView tel = (TableCellView) view.findViewById(R.id.tel);
        name.setRightText(entity.getName());
        tel.setRightText(entity.getTel());
        tel.setInputType(InputType.TYPE_CLASS_PHONE);
        tel.setInputMaxLength(13);
        name.setInputMaxLength(20);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (EmptyUtils.isNotEmpty(name.getRightText()) && (isTel(tel.getRightText()))) {
                    entity.setName(name.getRightText());
                    entity.setTel(tel.getRightText());
                    TelEntityManager.update(entity);
                    init();
                } else {
                    ToastUtils.showShortToast(mContext,"请输入正确的电话号码");
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

    private boolean isTel(String tel){
        return RegexUtils.isMobileSimple(tel) || RegexUtils.isTel(tel) || RegexUtils.isMatch(ConstUtils.REGEX_INTEGER, tel);
    }

    public class CallAdapter extends BaseAdapter {

        private List<TelEntity> data = new ArrayList<>();

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public TelEntity getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.view_item_tel, null);
                convertView.setTag(new ViewHolder(convertView));
            }

            final TelEntity entity = getItem(position);
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.tvText1.setText(entity.getName());
            viewHolder.tvText2.setText(entity.getTel());

            viewHolder.tel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phone = entity.getTel();
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });

            if (position == 0) {
                viewHolder.modify.setVisibility(View.GONE);
            } else {
                convertView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        delete(entity);
                        return false;
                    }
                });

                viewHolder.modify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        modify(entity);
                    }
                });
            }

            return convertView;
        }

        public void setData(List<TelEntity> newData) {
            data.clear();
            if (EmptyUtils.isNotEmpty(newData)) {
                data.addAll(newData);
            }
            notifyDataSetChanged();
        }

        public void add(TelEntity entity) {
            data.add(entity);
            notifyDataSetChanged();
        }

        public class ViewHolder {
            @BindView(R.id.tel)
            ImageView tel;
            @BindView(R.id.modify)
            ImageView modify;
            @BindView(R.id.tv_text_1)
            TextView tvText1;
            @BindView(R.id.tv_text_2)
            TextView tvText2;
            @BindView(R.id.root)
            RelativeLayout root;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }

        private void delete(final TelEntity entity) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("确定删除吗?");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    TelEntityManager.delete(entity.getId());
                    data.remove(entity);
                    notifyDataSetChanged();
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
    }
}
