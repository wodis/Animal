package com.openwudi.animal.contract.presenter;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.blankj.utilcode.utils.ToastUtils;
import com.openwudi.animal.contract.UpContract;
import com.openwudi.animal.manager.ApiManager;
import com.openwudi.animal.model.Item;
import com.openwudi.animal.model.ItemEncode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by diwu on 17/7/14.
 */

public class UpPresenter extends UpContract.Presenter {
    @Override
    public void onStart() {

    }

    public void show(final String encode) {
        final Observable.OnSubscribe<List<Item>> onSubscribe = new Observable.OnSubscribe<List<Item>>() {
            @Override
            public void call(Subscriber<? super List<Item>> subscriber) {
                List<Item> items = ApiManager.getItemsList(encode);
                subscriber.onNext(items);
                subscriber.onCompleted();
            }
        };

        Observable.create(onSubscribe)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                }).subscribe(new Subscriber<List<Item>>() {
            @Override
            public void onCompleted() {
                mView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                mView.hideLoading();
                ToastUtils.showShortToast(mContext, e.getMessage());
            }

            @Override
            public void onNext(List<Item> itemList) {
                switch (encode){
                    case ItemEncode.DWZT:
                        dialogZhuantai(itemList);
                        break;
                    case ItemEncode.SJTZ:
                        dialogQixidi(itemList);
                        break;
                    case ItemEncode.CJJL:
                        dialogJuli(itemList);
                        break;
                    case ItemEncode.CJFW:
                        dialogFangwei(itemList);
                        break;
                    case ItemEncode.CJWZ:
                        dialogWeizhi(itemList);
                        break;
                }
            }
        });
    }

    private void dialogZhuantai(final List<Item> itemList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("请选择状态");
        final String[] hobbies = new String[itemList.size()];
        for (int i = 0; i < itemList.size(); i++) {
            hobbies[i] = itemList.get(i).getName();
        }
        final Set<Item> selected = new HashSet<>();
        //    设置一个单项选择下拉框
        /**
         * 第一个参数指定我们要显示的一组下拉多选框的数据集合
         * 第二个参数代表哪几个选项被选择，如果是null，则表示一个都不选择，如果希望指定哪一个多选选项框被选择，
         * 需要传递一个boolean[]数组进去，其长度要和第一个参数的长度相同，例如 {true, false, false, true};
         * 第三个参数给每一个多选项绑定一个监听器
         */
        builder.setMultiChoiceItems(hobbies, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    selected.add(itemList.get(which));
                } else {
                    selected.remove(itemList.get(which));
                }
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void dialogQixidi(List<Item> list){
        final String[] items = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            items[i] = list.get(i).getName();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        //设置标题
        builder.setTitle("请选择栖息地");
        //设置图标
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(mContext, items[i], Toast.LENGTH_SHORT).show();
            }
        });
        builder.create();
        builder.show();
    }

    private void dialogJuli(List<Item> list){
        final String[] items = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            items[i] = list.get(i).getName();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        //设置标题
        builder.setTitle("请选择距离");
        //设置图标
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(mContext, items[i], Toast.LENGTH_SHORT).show();
            }
        });
        builder.create();
        builder.show();
    }

    private void dialogFangwei(List<Item> list){
        final String[] items = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            items[i] = list.get(i).getName();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        //设置标题
        builder.setTitle("请选择方位");
        //设置图标
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(mContext, items[i], Toast.LENGTH_SHORT).show();
            }
        });
        builder.create();
        builder.show();
    }

    private void dialogWeizhi(List<Item> list){
        final String[] items = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            items[i] = list.get(i).getName();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        //设置标题
        builder.setTitle("请选择位置");
        //设置图标
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(mContext, items[i], Toast.LENGTH_SHORT).show();
            }
        });
        builder.create();
        builder.show();
    }
}
