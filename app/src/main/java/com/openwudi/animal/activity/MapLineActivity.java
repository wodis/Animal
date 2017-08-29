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
import com.blankj.utilcode.utils.EmptyUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.openwudi.animal.R;
import com.openwudi.animal.base.BaseActivity;
import com.openwudi.animal.manager.ApiManager;
import com.openwudi.animal.model.GPSDataModel;
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
        List<LatLng> latLngs = new ArrayList<>();
        for (GPSDataModel dataModel : list) {
            latLngs.add(new LatLng(Double.parseDouble(dataModel.getLat()), Double.parseDouble(dataModel.getLng())));
        }

        MapStatusUpdate mapstatusUpdatePoint = MapStatusUpdateFactory.newLatLng(latLngs.get(0));
        baiduMap.setMapStatus(mapstatusUpdatePoint);
        addOverlay(latLngs.get(0), bmStart, null);
        addOverlay(latLngs.get(latLngs.size() - 1), bmEnd, null);


        OverlayOptions ooPolyline = new PolylineOptions().width(13).color(0xAAFF0000).points(latLngs);

        //在地图上画出线条图层，mPolyline：线条图层
        Polyline mPolyline = (Polyline) baiduMap.addOverlay(ooPolyline);
        mPolyline.setZIndex(3);
        return mPolyline;
    }
}
