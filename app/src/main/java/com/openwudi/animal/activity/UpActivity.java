package com.openwudi.animal.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.blankj.utilcode.utils.EmptyUtils;
import com.blankj.utilcode.utils.KeyboardUtils;
import com.blankj.utilcode.utils.NetworkUtils;
import com.blankj.utilcode.utils.RegexUtils;
import com.blankj.utilcode.utils.TimeUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.openwudi.animal.R;
import com.openwudi.animal.base.BaseActivity;
import com.openwudi.animal.contract.UpContract;
import com.openwudi.animal.contract.model.UpModel;
import com.openwudi.animal.contract.presenter.UpPresenter;
import com.openwudi.animal.model.Animal;
import com.openwudi.animal.model.DataAcquisition;
import com.openwudi.animal.model.ItemEncode;
import com.openwudi.animal.utils.TimeUtil;
import com.openwudi.animal.view.TableCellView;
import com.openwudi.animal.view.TitleBarView;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.blankj.utilcode.utils.ConstUtils.REGEX_POSITIVE_INTEGER;

/**
 * Created by diwu on 17/7/14.
 */

public class UpActivity extends BaseActivity implements UpContract.View, View.OnClickListener, EasyPermissions.PermissionCallbacks {

    public static final int REQ_CODE_NAME = 101;
    public static final int REQ_CODE_HEALTH_PIC = 102;
    public static final int REQ_CODE_ILL_PIC = 103;
    public static final int REQ_CODE_DEATH_PIC = 104;
    public static final int REQ_CODE_MAP = 105;

    @BindView(R.id.title_bar_tbv)
    TitleBarView titleBarTbv;
    @BindView(R.id.status)
    TableCellView status;
    @BindView(R.id.name)
    TableCellView name;
    @BindView(R.id.qixidi)
    TableCellView qixidi;
    @BindView(R.id.juli)
    TableCellView juli;
    @BindView(R.id.fangwei)
    TableCellView fangwei;
    @BindView(R.id.weizhi)
    TableCellView weizhi;
    @BindView(R.id.caijishuliang)
    TableCellView caijishuliang;
    @BindView(R.id.jiangkangshuliang)
    TableCellView jiangkangshuliang;
    @BindView(R.id.shengbingshuliang)
    TableCellView shengbingshuliang;
    @BindView(R.id.pic_iv)
    ImageView picIv;
    @BindView(R.id.health_left_iv)
    ImageView healthLeftIv;
    @BindView(R.id.ill_left_iv)
    ImageView illLeftIv;
    @BindView(R.id.death_left_iv)
    ImageView deathLeftIv;
    //    @BindView(R.id.save_tv)
//    TextView saveTv;
//    @BindView(R.id.submit_tv)
//    TextView submitTv;
    @BindView(R.id.siwangshuliang)
    TableCellView siwangshuliang;
    @BindView(R.id.jiangkangtupian)
    TableCellView jiangkangtupian;
    @BindView(R.id.shengbingtupian)
    TableCellView shengbingtupian;
    @BindView(R.id.siwangtupian)
    TableCellView siwangtupian;
    @BindView(R.id.shengbingmiaoshu)
    LinearLayout shengbingmiaoshu;
    @BindView(R.id.siwangmiaoshu)
    LinearLayout siwangmiaoshu;
    @BindView(R.id.pic_line)
    View picLine;
    @BindView(R.id.shengbingline)
    View shengbingline;
    @BindView(R.id.siwangline)
    View siwangline;
    @BindView(R.id.gps)
    TableCellView gps;
    @BindView(R.id.time)
    TableCellView time;
    @BindView(R.id.shengbingEt)
    EditText shengbingEt;
    @BindView(R.id.siwangEt)
    EditText siwangEt;
    @BindView(R.id.bubaoEt)
    EditText bubaoEt;
    @BindView(R.id.ly_bubao)
    View bubaoLy;

    private String pic;

    private UpPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up);
        ButterKnife.bind(this);
        presenter = new UpPresenter();
        presenter.setVM(this, this, new UpModel());

        caijishuliang.setInputType(InputType.TYPE_CLASS_PHONE);
        jiangkangshuliang.setInputType(InputType.TYPE_CLASS_PHONE);
        shengbingshuliang.setInputType(InputType.TYPE_CLASS_PHONE);
        siwangshuliang.setInputType(InputType.TYPE_CLASS_PHONE);

        caijishuliang.setInputMaxLength(9);
        jiangkangshuliang.setInputMaxLength(9);
        shengbingshuliang.setInputMaxLength(9);
        siwangshuliang.setInputMaxLength(9);

        titleBarTbv.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("确定退出吗?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
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
        titleBarTbv.setRightListener(this);

        name.setOnClickListener(this);
        picIv.setOnClickListener(this);
        status.setOnClickListener(this);
        qixidi.setOnClickListener(this);
        juli.setOnClickListener(this);
        fangwei.setOnClickListener(this);
        weizhi.setOnClickListener(this);
        jiangkangtupian.setOnClickListener(this);
        shengbingtupian.setOnClickListener(this);
        siwangtupian.setOnClickListener(this);
//        saveTv.setOnClickListener(this);
//        submitTv.setOnClickListener(this);
        healthLeftIv.setOnClickListener(this);
        illLeftIv.setOnClickListener(this);
        deathLeftIv.setOnClickListener(this);
        gps.setOnClickListener(this);
        time.setOnClickListener(this);

        caijishuliang.getmInputEt().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!checkNumber(s) && s.length() > 0) {
                    s = s.subSequence(0, s.length() - 1);
                    caijishuliang.setRightText(s.toString());
                } else {
                    jiangkangshuliang.setRightText(s.toString());
                    shengbingshuliang.setRightText("");
                    siwangshuliang.setRightText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        jiangkangshuliang.getmInputEt().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!checkNumber(s) && s.length() > 0) {
                    s = s.subSequence(0, s.length() - 1);
                    jiangkangshuliang.setRightText(s.toString());
                }
                healthLeftIv.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
                jiangkangtupian.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        shengbingshuliang.getmInputEt().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!checkNumber(s) && s.length() > 0) {
                    s = s.subSequence(0, s.length() - 1);
                    shengbingshuliang.setRightText(s.toString());
                }
                illLeftIv.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
                shengbingtupian.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
                shengbingmiaoshu.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
                shengbingline.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
                presenter.equalsAllNumbers();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        siwangshuliang.getmInputEt().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!checkNumber(s) && s.length() > 0) {
                    s = s.subSequence(0, s.length() - 1);
                    siwangshuliang.setRightText(s.toString());
                }
                deathLeftIv.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
                siwangtupian.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
                siwangmiaoshu.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
                siwangline.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
//                presenter.equalsAllNumbers();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        healthLeftIv.setVisibility(View.GONE);
        illLeftIv.setVisibility(View.GONE);
        deathLeftIv.setVisibility(View.GONE);

        DataAcquisition dataAcquisition = (DataAcquisition) getIntent().getSerializableExtra(DataAcquisition.class.toString());
        presenter.setLatest(dataAcquisition);

        checkPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        KeyboardUtils.hideSoftInput(mContext);
    }

    private boolean checkNumber(CharSequence s) {
        s = EmptyUtils.isEmpty(s.toString()) ? "0" : s;
        boolean check = RegexUtils.isMatch(REGEX_POSITIVE_INTEGER, s);
        if ("0".equals(s.toString())) {
            check = true;
        }
        if (!check) {
            ToastUtils.showShortToast(mContext, "请输入正确的数字");
        }
        return check;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.name:
                startActivityForResult(new Intent(mContext, AnimalSelectActivity.class), REQ_CODE_NAME);
                break;
            case R.id.qixidi:
                qixidi.setEnabled(false);
                presenter.show(ItemEncode.SJTZ);
                qixidi.setEnabled(true);
                break;
            case R.id.status:
                status.setEnabled(false);
                presenter.show(ItemEncode.DWZT);
                status.setEnabled(true);
                break;
            case R.id.juli:
                juli.setEnabled(false);
                presenter.show(ItemEncode.CJJL);
                juli.setEnabled(true);
                break;
            case R.id.fangwei:
                fangwei.setEnabled(false);
                presenter.show(ItemEncode.CJFW);
                fangwei.setEnabled(true);
                break;
            case R.id.weizhi:
                weizhi.setEnabled(false);
                presenter.show(ItemEncode.CJWZ);
                weizhi.setEnabled(true);
                break;
            case R.id.pic_iv:
                Intent picIntent = new Intent(mContext, PhotoActivity.class);
                picIntent.putExtra("pic", pic);
                startActivity(picIntent);
                break;
            case R.id.jiangkangtupian:
                startAlbum(REQ_CODE_HEALTH_PIC);
                break;
            case R.id.shengbingtupian:
                startAlbum(REQ_CODE_ILL_PIC);
                break;
            case R.id.siwangtupian:
                startAlbum(REQ_CODE_DEATH_PIC);
                break;
            case R.id.health_left_iv:
                presenter.startPhoto(0);
                break;
            case R.id.ill_left_iv:
                presenter.startPhoto(1);
                break;
            case R.id.death_left_iv:
                presenter.startPhoto(2);
                break;
            case R.id.gps:
                presenter.gps();
                break;
            case R.id.time:
                presenter.getTime();
                break;
            case R.id.right_tv:
                submit();
                break;
//            case R.id.save_tv:
//                presenter.submit(true);
//                break;
//            case R.id.submit_tv:
//                presenter.submit(false);
//                break;
        }
    }

    private void submit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("确定提交吗?");
        builder.setPositiveButton("直接上报", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.submit(false);
            }
        });
        //    设置一个NegativeButton
        builder.setNegativeButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.submit(true);
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQ_CODE_NAME == requestCode && resultCode == RESULT_OK) {
            Animal animal = (Animal) data.getSerializableExtra(Animal.class.getSimpleName());
            name.setRightText(animal.getName());
            if (!TextUtils.isEmpty(animal.getPhoto())) {
                picIv.setVisibility(View.VISIBLE);
                findViewById(R.id.pic_line).setVisibility(View.VISIBLE);
                pic = getString(R.string.PIC_URL) + animal.getPhoto();
                Glide.with(mContext).load(pic).into(picIv);
            } else {
                picIv.setVisibility(View.GONE);
                findViewById(R.id.pic_line).setVisibility(View.GONE);
            }
            presenter.setAnimal(animal);
        } else if (REQ_CODE_HEALTH_PIC == requestCode) {
            List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(data);
            if (localMedias != null && localMedias.size() > 0) {
                for (LocalMedia item : localMedias) {
                    String path = item.getPath();
                    Glide.with(mContext).load(new File(path)).into(healthLeftIv);
                    presenter.setHealth(item);
                    healthLeftIv.setVisibility(View.VISIBLE);
                }
            }
        } else if (REQ_CODE_ILL_PIC == requestCode) {
            List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(data);
            if (localMedias != null && localMedias.size() > 0) {
                for (LocalMedia item : localMedias) {
                    String path = item.getPath();
                    Glide.with(mContext).load(new File(path)).into(illLeftIv);
                    presenter.setIll(item);
                    illLeftIv.setVisibility(View.VISIBLE);
                }
            }
        } else if (REQ_CODE_DEATH_PIC == requestCode) {
            List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(data);
            if (localMedias != null && localMedias.size() > 0) {
                for (LocalMedia item : localMedias) {
                    String path = item.getPath();
                    Glide.with(mContext).load(new File(path)).into(deathLeftIv);
                    presenter.setDeath(item);
                    deathLeftIv.setVisibility(View.VISIBLE);
                }
            }
        } else if (REQ_CODE_MAP == requestCode && data != null) {
            LatLng latLng = data.getParcelableExtra(LatLng.class.getSimpleName());
            presenter.setLatLng(latLng);
            DecimalFormat df = new DecimalFormat("#.00000");
            setGps(df.format(latLng.latitude) + "," + df.format(latLng.longitude), false);
        }
    }

    @Override
    public int getTotal() {
        String total = caijishuliang.getRightRealString();
        return Integer.parseInt(total.length() > 0 ? total : "0");
    }

    @Override
    public int getHealthNum() {
        String total = jiangkangshuliang.getRightRealString();
        return Integer.parseInt(total.length() > 0 ? total : "0");
    }

    @Override
    public int getIllNum() {
        String total = shengbingshuliang.getRightRealString();
        return Integer.parseInt(total.length() > 0 ? total : "0");
    }

    @Override
    public int getDeathNum() {
        String total = siwangshuliang.getRightRealString();
        return Integer.parseInt(total.length() > 0 ? total : "0");
    }

    @Override
    public int setHealthNum(int healthNum) {
        jiangkangshuliang.setRightText(String.valueOf(healthNum));
        return healthNum;
    }

    @Override
    public String illDesc() {
        return shengbingEt.getText().toString();
    }

    @Override
    public String deathDesc() {
        return siwangEt.getText().toString();
    }

    @Override
    public String bubao() {
        return bubaoEt.getText().toString();
    }

    @Override
    public void setQixidi(String string) {
        qixidi.setRightText(string);
        KeyboardUtils.hideSoftInput(mContext);
    }

    @Override
    public void setZhuangTai(String string) {
        status.setRightText(string);
        KeyboardUtils.hideSoftInput(mContext);
    }

    @Override
    public void setJuli(String string) {
        juli.setRightText(string);
        KeyboardUtils.hideSoftInput(mContext);
    }

    @Override
    public void setFangwei(String string) {
        fangwei.setRightText(string);
        KeyboardUtils.hideSoftInput(mContext);
    }

    @Override
    public void setWeizhi(String string) {
        weizhi.setRightText(string);
        KeyboardUtils.hideSoftInput(mContext);
    }

    @Override
    public void setTime(String string) {
        time.setRightText(string);
        String currentTime = TimeUtils.getCurTimeString(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()));
        boolean isToday = currentTime.equals(string.substring(0, 10));
        bubaoLy.setVisibility(isToday ? View.GONE : View.VISIBLE);
        if (isToday) {
            bubaoEt.setText("");
        }
    }

    @Override
    public void startMap(Intent intent) {
        startActivityForResult(intent, REQ_CODE_MAP);
    }

    @Override
    public void setGps(final String string, boolean isFirst) {
        if (isFirst) {
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gps.setRightText(string);
                gps.postInvalidate();
            }
        });
    }

    @Override
    public String getGps(){
        return gps.getRightText();
    }

    public void startAlbum(int requestCode) {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                .maxSelectNum(1)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(
                        PictureConfig.SINGLE)// 多选 or 单选
                .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                .isCamera(true)// 是否显示拍照按钮
                .compressMode(PictureConfig.SYSTEM_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .openClickSound(false)// 是否开启点击声音
//                .selectionMedia(selectList)// 是否传入已选图片
                .forResult(requestCode);//结果回调onActivityResult code
    }

    public static final int REQ_PERMISSION = 1000;
    private static final int REQ_SETTING_RESULT = 201;

    /**
     * 所需权限
     */
    private String[] mPerms = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS
    };


    private void checkPermission() {
        if (!EasyPermissions.hasPermissions(this, mPerms)) {
            EasyPermissions.requestPermissions(this, "需要相关权限, 否则无法运行",
                    REQ_PERMISSION, mPerms);
        } else {
            if (NetworkUtils.isConnected(mContext)) {
                presenter.gps();
            } else {
                startGpsWithoutNetwork();
            }
        }
    }

    //gps
    private LocationManager gpsManager;

    private void startGpsWithoutNetwork() {
        // 获取到LocationManager对象
        gpsManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //provider可为gps定位，也可为为基站和WIFI定位
        String provider = gpsManager.getProvider(LocationManager.GPS_PROVIDER).getName();

        //3000ms为定位的间隔时间，10m为距离变化阀值，gpsListener为回调接口
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        gpsManager.requestLocationUpdates(provider, 3000, 1, gpsListener);
    }

    private void stopGpsWithoutNetwork() {
        gpsManager.removeUpdates(gpsListener);
    }

    // 创建位置监听器
    private LocationListener gpsListener = new LocationListener() {

        // 位置发生改变时调用
        @Override
        public void onLocationChanged(Location location) {
            Log.e("Location", "onLocationChanged");
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            float speed = location.getSpeed();
            long time = location.getTime();
            String s = "latitude--->" + latitude
                    + "  longitude--->" + longitude
                    + "  speed--->" + speed
                    + "  time--->" + new Date(time).toLocaleString();
            LatLng latLng = new LatLng(latitude, longitude);
            presenter.setLatLng(latLng);
            DecimalFormat df = new DecimalFormat("#.00000");
            setGps(df.format(latitude) + "," + df.format(longitude), false);
        }

        // provider失效时调用
        @Override
        public void onProviderDisabled(String provider) {
            Log.e("Location", "onProviderDisabled");
        }

        // provider启用时调用
        @Override
        public void onProviderEnabled(String provider) {
            Log.e("Location", "onProviderEnabled");
        }

        // 状态改变时调用
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e("Location", "onStatusChanged");
        }
    };

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == REQ_PERMISSION && perms.size() >= mPerms.length) {
            if (NetworkUtils.isConnected(mContext)) {
                presenter.gps();
            } else {
                startGpsWithoutNetwork();
            }
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        try {
            new AppSettingsDialog.Builder(UpActivity.this, "需要相关权限, 否则无法运行")
                    .setPositiveButton("确定")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setRequestCode(REQ_SETTING_RESULT)
                    .build()
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showShortToast(UpActivity.this, "需要相关权限, 否则无法运行");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
