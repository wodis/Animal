package com.openwudi.animal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.blankj.utilcode.utils.EmptyUtils;
import com.blankj.utilcode.utils.TimeUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.openwudi.animal.R;
import com.openwudi.animal.base.BaseActivity;
import com.openwudi.animal.manager.ApiManager;
import com.openwudi.animal.model.GPSDataModel;
import com.openwudi.animal.view.TitleBarView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by diwu on 17/7/21.
 */

public class MapLineActivity extends BaseActivity {
    @BindView(R.id.titlebar)
    TitleBarView titlebar;
    @BindView(R.id.map_view)
    MapView mapView;

    private BaiduMap baiduMap;
    private LatLng hmPos;

    private double lat = 40.050966;
    private double lon = 116.303128;

    private BitmapDescriptor bmGcoding = null;
    private BitmapDescriptor bmStart = null;
    private BitmapDescriptor bmEnd = null;
    private BitmapDescriptor bmPanda = null;

    private boolean onlyShow = true;
    private String date = "";
    private List<GPSDataModel> dataModelList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        bmGcoding = BitmapDescriptorFactory.fromResource(R.mipmap.icon_gcoding);
        bmStart = BitmapDescriptorFactory.fromResource(R.mipmap.icon_start);
        bmEnd = BitmapDescriptorFactory.fromResource(R.mipmap.icon_end);
        bmPanda = BitmapDescriptorFactory.fromResource(R.mipmap.icon_panda);
        try {
//            lat = Double.parseDouble(getIntent().getStringExtra("lat"));
//            lon = Double.parseDouble(getIntent().getStringExtra("lon"));
            onlyShow = getIntent().getBooleanExtra("onlyShow", true);
            date = getIntent().getStringExtra("date");
            hmPos = new LatLng(lat, lon);// 坐标
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        //BaiduMap管理具体的某一个MapView： 旋转，移动，缩放，事件。。。
        baiduMap = mapView.getMap();

        //设置缩放级别，默认级别为12
        MapStatusUpdate mapstatusUpdate = MapStatusUpdateFactory.zoomTo(19);
        baiduMap.setMapStatus(mapstatusUpdate);

//        //设置地图中心点，默认是天安门
//        MapStatusUpdate mapstatusUpdatePoint = MapStatusUpdateFactory.newLatLng(hmPos);
//        baiduMap.setMapStatus(mapstatusUpdatePoint);
//
//        final Marker marker = addOverlay(hmPos, bmGcoding, null);

        titlebar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
    }

    private void init() {
        final Observable.OnSubscribe<List<GPSDataModel>> onSubscribe = new Observable.OnSubscribe<List<GPSDataModel>>() {
            @Override
            public void call(Subscriber<? super List<GPSDataModel>> subscriber) {
                subscriber.onNext(ApiManager.getGPSDataList(date));
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
                }).subscribe(new Subscriber<List<GPSDataModel>>() {
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
            public void onNext(List<GPSDataModel> list) {
                if (EmptyUtils.isNotEmpty(list)) {
                    addOverlayLine(list);
                } else {
                    ToastUtils.showShortToast(mContext, "无记录");
                    finish();
                }
            }
        });
    }


    public Marker addOverlay(LatLng currentPoint, BitmapDescriptor icon, Bundle bundle) {
        OverlayOptions overlayOptions = new MarkerOptions().position(currentPoint)
                .icon(icon).zIndex(9).draggable(true);
        Marker marker = (Marker) baiduMap.addOverlay(overlayOptions);
        if (null != bundle) {
            marker.setExtraInfo(bundle);
        }
        return marker;
    }

    private Polyline addOverlayLine(List<GPSDataModel> list) {
        //构造纹理资源
        BitmapDescriptor custom1 = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_road_red_arrow);
        BitmapDescriptor custom2 = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_road_green_arrow);
        BitmapDescriptor custom3 = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_road_blue_arrow);

        //构造纹理队列
        List<BitmapDescriptor> customList = new ArrayList<BitmapDescriptor>();
        customList.add(custom1);
        customList.add(custom2);
        customList.add(custom3);

        Collections.sort(list, new Comparator<GPSDataModel>() {
            @Override
            public int compare(GPSDataModel o1, GPSDataModel o2) {
                long d1 = TimeUtils.string2Milliseconds(o1.getUploadTime().replaceAll("T", " "));
                long d2 = TimeUtils.string2Milliseconds(o2.getUploadTime().replaceAll("T", " "));
                if (d1 > d2) {
                    return 1;
                } else if (d1 < d2) {
                    return -1;
                }
                return 0;
            }
        });

        List<LatLng> latLngs = new ArrayList<>();
        List<Integer> index = new ArrayList<Integer>();
        double dis = 0;
        for (int i = 0; i < list.size(); i++) {
            GPSDataModel dataModel = list.get(i);
            LatLng latLng = new LatLng(Double.parseDouble(dataModel.getLat()), Double.parseDouble(dataModel.getLng()));

            //如果距离大于10才画一个熊猫
//            if (i != 0 && i != list.size() - 1) {
//                GPSDataModel dataModelLast = list.get(i - 1);
//                LatLng latLngLast = new LatLng(Double.parseDouble(dataModelLast.getLat()), Double.parseDouble(dataModelLast.getLng()));
//                dis += DistanceUtil.getDistance(latLngLast, latLng);
//
//                System.out.println(dis);
//                if (dis > 10d) {
//                    addOverlay(latLng, bmPanda, null);
//                    dis = 0;
//                }
//            }
            latLngs.add(latLng);
            index.add(1);
        }

        MapStatusUpdate mapstatusUpdatePoint = MapStatusUpdateFactory.newLatLng(latLngs.get(0));
        baiduMap.setMapStatus(mapstatusUpdatePoint);
        addOverlay(latLngs.get(0), bmStart, null);
        addOverlay(latLngs.get(latLngs.size() - 1), bmEnd, null);


        OverlayOptions ooPolyline = new PolylineOptions().width(16).color(mContext.getResources().getColor(R.color.colorPrimary)).points(latLngs).textureIndex(index).customTextureList(customList);

        //在地图上画出线条图层，mPolyline：线条图层
        Polyline mPolyline = (Polyline) baiduMap.addOverlay(ooPolyline);
        mPolyline.setDottedLine(true);
        mPolyline.setZIndex(3);
        return mPolyline;
    }
}
