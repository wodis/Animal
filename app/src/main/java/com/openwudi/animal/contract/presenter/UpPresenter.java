package com.openwudi.animal.contract.presenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Toast;

import com.baidu.location.Address;
import com.blankj.utilcode.utils.ConstUtils;
import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.ImageUtils;
import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.RegexUtils;
import com.blankj.utilcode.utils.TimeUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.entity.LocalMedia;
import com.openwudi.animal.R;
import com.openwudi.animal.activity.PhotoActivity;
import com.openwudi.animal.base.BaseActivity;
import com.openwudi.animal.contract.UpContract;
import com.openwudi.animal.location.LocationHelper;
import com.openwudi.animal.location.OnLocationListener;
import com.openwudi.animal.manager.AccountManager;
import com.openwudi.animal.manager.ApiManager;
import com.openwudi.animal.model.Animal;
import com.openwudi.animal.model.DataAcquisition;
import com.openwudi.animal.model.Item;
import com.openwudi.animal.model.ItemEncode;
import com.openwudi.animal.utils.TimeUtil;
import com.openwudi.animal.utils.Utils;
import com.openwudi.animal.view.pickerview.TimePickerDialog;
import com.openwudi.animal.view.pickerview.data.Type;
import com.openwudi.animal.view.pickerview.listener.OnDateSetListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by diwu on 17/7/14.
 */

public class UpPresenter extends UpContract.Presenter implements OnDateSetListener {
    private Animal animal;
    private Item qixidi;
    private Set<Item> zhuangtai;
    private Item juli;
    private Item fangwei;
    private Item weizhi;
    private Item tujing;

    private LocalMedia health;
    private LocalMedia ill;
    private LocalMedia death;

    private Address address;
    private double latitude;
    private double longtitude;
    private double altitude = 0;
    private long collectionTime = 0;

    @Override
    public void onStart() {

    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public void setHealth(LocalMedia health) {
        this.health = health;
    }

    public void setIll(LocalMedia ill) {
        this.ill = ill;
    }

    public void setDeath(LocalMedia death) {
        this.death = death;
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
                switch (encode) {
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
                    case ItemEncode.SYDX:
                        dialogTujing(itemList);
                        break;
                }
            }
        });
    }

    private void dialogTujing(final List<Item> list) {
        final String[] items = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            items[i] = list.get(i).getName();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        //设置标题
        builder.setTitle("请选择上报途径");
        //设置图标
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(mContext, items[i], Toast.LENGTH_SHORT).show();
                mView.setQixidi(items[i]);
                tujing = list.get(i);
            }
        });
        builder.create();
        builder.show();
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
                if (selected.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (Item item : selected) {
                        sb.append(item.getName()).append(",");
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    mView.setZhuangTai(sb.toString());
                    zhuangtai = selected;
                } else {
                    mView.setZhuangTai("");
                    zhuangtai = null;
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void dialogQixidi(final List<Item> list) {
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
                mView.setQixidi(items[i]);
                qixidi = list.get(i);
            }
        });
        builder.create();
        builder.show();
    }

    private void dialogJuli(final List<Item> list) {
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
                String item = items[i];
                Toast.makeText(mContext, item, Toast.LENGTH_SHORT).show();
                mView.setJuli(item);
                juli = list.get(i);
            }
        });
        builder.create();
        builder.show();
    }

    private void dialogFangwei(final List<Item> list) {
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
                String item = items[i];
                Toast.makeText(mContext, item, Toast.LENGTH_SHORT).show();
                mView.setFangwei(item);
                fangwei = list.get(i);
            }
        });
        builder.create();
        builder.show();
    }

    private void dialogWeizhi(final List<Item> list) {
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
                String item = items[i];
                Toast.makeText(mContext, item, Toast.LENGTH_SHORT).show();
                mView.setWeizhi(item);
                weizhi = list.get(i);
            }
        });
        builder.create();
        builder.show();
    }

    private String getAnimalStateId() {
        StringBuilder sb = new StringBuilder();
        for (Item item : zhuangtai) {
            sb.append(item.getCode()).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public void startPhoto(int i) {
        Intent intent = new Intent(mContext, PhotoActivity.class);
        switch (i) {
            case 0:
                if (health == null) {
                    return;
                }
                intent.putExtra(Animal.class.getSimpleName(), health);
                break;
            case 1:
                if (ill == null) {
                    return;
                }
                intent.putExtra(Animal.class.getSimpleName(), ill);
                break;
            case 2:
                if (death == null) {
                    return;
                }
                intent.putExtra(Animal.class.getSimpleName(), death);
                break;
        }
        mContext.startActivity(intent);
    }

    private boolean check() {
        String msg = "";
        if (animal == null) {
            msg = "请选择动物类型";
        } else if (mView.getTotal() <= 0) {
            msg = "请填写采集数量";
        } else if (qixidi == null) {
            msg = "请填写栖息地";
        } else if (zhuangtai == null || zhuangtai.size() <= 0) {
            msg = "请填写状态";
        } else if (juli == null) {
            msg = "请填写距离";
        } else if (fangwei == null) {
            msg = "请填写方位";
        } else if (weizhi == null) {
            msg = "请填写位置";
        } else if (mView.getHealthNum() > 0 && health == null) {
            msg = "请上传图片";
        } else if (mView.getIllNum() > 0 && ill == null) {
            msg = "请上传图片";
        } else if (mView.getDeathNum() > 0 && death == null) {
            msg = "请上传图片";
        } else if (address == null) {
            msg = "请重新定位";
        } else if (collectionTime <= 0) {
            msg = "请选择采集时间";
        }

        if (!TextUtils.isEmpty(msg)) {
            ToastUtils.showShortToast(mContext, msg);
            return false;
        }

        return true;
    }

    public DataAcquisition getData() {
        DataAcquisition data = new DataAcquisition();
        data.setId("");
        data.setMonitorStationId(AccountManager.getAccount().getMonitorStationId());
        data.setTerminalId(AccountManager.getAccount().getTerminalId());
        data.setUseObjectId(AccountManager.getAccount().getUseObjectId());
        data.setCollectionTime(TimeUtils.milliseconds2String(collectionTime));
        data.setAnimalId(animal.getId());
        data.setAnimaStateId(getAnimalStateId());
        data.setHabitate(qixidi.getCode());
        data.setTotal(mView.getTotal());
        data.setHealthNum(mView.getHealthNum());
        data.setLatitude(latitude + "");
        data.setLongtitude(longtitude + "");
        data.setAltitude((int) altitude);
        if (health != null) {
            byte[] imageByteO = FileUtils.readFile2Bytes(health.getPath());
            byte[] imageByte = Utils.compressImage(imageByteO, 0.3D);
            data.setHealthPic(Base64.encodeToString(imageByte, Base64.DEFAULT));
        }
        data.setIllNum(mView.getIllNum());
        if (ill != null) {
            byte[] imageByteO = FileUtils.readFile2Bytes(ill.getPath());
            byte[] imageByte = Utils.compressImage(imageByteO, 0.3D);
            data.setIllPic(Base64.encodeToString(imageByte, Base64.DEFAULT));
        }

        if (data.getIllNum() > 0) {
            data.setIllDesc(mView.illDesc());
        }

        data.setDeathNum(mView.getDeathNum());
        if (death != null) {
            byte[] imageByteO = FileUtils.readFile2Bytes(death.getPath());
            byte[] imageByte = Utils.compressImage(imageByteO, 0.3D);
            data.setDeathPic(Base64.encodeToString(imageByte, Base64.DEFAULT));
        }

        if (data.getDeathNum() > 0) {
            data.setDeathDesc(mView.deathDesc());
        }

        data.setDistance(juli.getCode());
        data.setAzimuth(fangwei.getCode());
        data.setPosition(weizhi.getCode());

        data.setBubao(0);
        String bubao = mView.bubao();
        if (!TextUtils.isEmpty(bubao)) {
            data.setBubao(1);
            data.setBubaoDesc(bubao);
            data.setBubaoTime(TimeUtil.getDateTime());
        }
        return data;
    }

    public void submit(final boolean saveOnly) {
        if (!check()) {
            return;
        }
        final Observable.OnSubscribe<String> onSubscribe = new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                DataAcquisition dataAcquisition = getData();
                if (saveOnly) {
                    mModel.saveDataAcquisition(animal, dataAcquisition, qixidi, zhuangtai, juli, fangwei, weizhi);
                    subscriber.onNext("");
                } else {
                    String result = ApiManager.saveDataAcquisition(dataAcquisition);
                    subscriber.onNext(result);
                }
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
                }).subscribe(new Subscriber<String>() {
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
            public void onNext(String string) {
                ((BaseActivity) mContext).finish();
            }
        });
    }

    public void gps() {
        LocationHelper.build(mContext, new OnLocationListener() {
            @Override
            public void locSuccess(Address add, double lat, double lon, double alt) {
                LogUtils.d("L", "定位经纬度：latitude：" + lat + ",longitude:" + lon + ",altitude:" + altitude);
                mView.setGps(lat + "," + lon);
                if (!TextUtils.isEmpty(add.address)) {
                    ToastUtils.showShortToast(mContext, add.street);
                }
                address = add;
                latitude = lat;
                longtitude = lon;
                if (alt > 0 && alt < 4.9E-324) {
                    altitude = alt;
                }
            }

            @Override
            public void locError() {
                ToastUtils.showShortToast(mContext, "定位失败, 请检查是否开启定位权限");
            }
        }).start();
    }

    public void getTime() {
        TimePickerDialog.Builder mBuilder = initBuilder();

        long currentTime = System.currentTimeMillis();
        mBuilder.setCurrentMillseconds(currentTime);
        mBuilder.build().showDialog(((BaseActivity) mContext).getSupportFragmentManager(), "year_month");
    }

    private static int currentYear = 2017;
    private static int maxYear = 2023;
    private static int minYear = 1974;

    private int yearNum;
    private long maxMillSeconds;
    private long minMillSeconds;

    private TimePickerDialog.Builder initBuilder() {
        TimePickerDialog.Builder mBuilder;
        String timeMin = mContext.getString(R.string.graduation_time_limit, minYear, 1);
        String timeMax = mContext.getString(R.string.graduation_time_limit, maxYear, 12);
        maxMillSeconds = Utils.string2Long(timeMax, mContext.getString(R.string.time_format));
        minMillSeconds = Utils.string2Long(timeMin, mContext.getString(R.string.time_format));
        mBuilder = new TimePickerDialog.Builder()
                .setType(Type.ALL)
                .setThemeColor(mContext.getResources().getColor(R.color.colorPrimary))
                .setCallBack(this)
                .setTitleStringId("采集时间")
                .setWheelItemTextSize(14)
                .setMinMillseconds(minMillSeconds)
                .setMaxMillseconds(maxMillSeconds)
        ;
        return mBuilder;
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        collectionTime = millseconds;
        System.out.println(millseconds);
        String time = TimeUtils.milliseconds2String(millseconds);
        mView.setTime(time);
    }
}
