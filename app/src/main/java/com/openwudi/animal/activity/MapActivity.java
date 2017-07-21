package com.openwudi.animal.activity;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
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
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.blankj.utilcode.utils.ToastUtils;
import com.openwudi.animal.R;
import com.openwudi.animal.base.BaseActivity;
import com.openwudi.animal.model.CurrentLocation;
import com.openwudi.animal.view.TitleBarView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by diwu on 17/7/21.
 */

public class MapActivity extends BaseActivity {
    @BindView(R.id.titlebar)
    TitleBarView titlebar;
    @BindView(R.id.map_view)
    MapView mapView;

    private BaiduMap baiduMap;
    private LatLng hmPos;

    private double lat = 40.050966;
    private double lon = 116.303128;

    private BitmapDescriptor bmGcoding = null;

    private boolean onlyShow = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        bmGcoding = BitmapDescriptorFactory.fromResource(R.mipmap.icon_gcoding);
        try {
            lat = Double.parseDouble(getIntent().getStringExtra("lat"));
            lon = Double.parseDouble(getIntent().getStringExtra("lon"));
            onlyShow = getIntent().getBooleanExtra("onlyShow", true);
            hmPos = new LatLng(lat, lon);// 坐标
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        //BaiduMap管理具体的某一个MapView： 旋转，移动，缩放，事件。。。
        baiduMap = mapView.getMap();

        //设置缩放级别，默认级别为12
        MapStatusUpdate mapstatusUpdate = MapStatusUpdateFactory.zoomTo(19);
        baiduMap.setMapStatus(mapstatusUpdate);

        //设置地图中心点，默认是天安门
        MapStatusUpdate mapstatusUpdatePoint = MapStatusUpdateFactory.newLatLng(hmPos);
        baiduMap.setMapStatus(mapstatusUpdatePoint);

        final Marker marker = addOverlay(hmPos, bmGcoding, null);

        if (!onlyShow) {
            baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    ToastUtils.showShortToast(mContext, latLng.latitude + "," + latLng.longitude);
                    hmPos = latLng;
                    marker.setPosition(latLng);
                }

                @Override
                public boolean onMapPoiClick(MapPoi mapPoi) {
                    return false;
                }
            });
        }

        titlebar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!onlyShow) {
                    Intent i = new Intent();
                    i.putExtra(LatLng.class.getSimpleName(), hmPos);
                    setResult(RESULT_OK, i);
                }
                finish();
            }
        });
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


    public Marker addOverlay(LatLng currentPoint, BitmapDescriptor icon, Bundle bundle) {
        OverlayOptions overlayOptions = new MarkerOptions().position(currentPoint)
                .icon(icon).zIndex(9).draggable(true);
        Marker marker = (Marker) baiduMap.addOverlay(overlayOptions);
        if (null != bundle) {
            marker.setExtraInfo(bundle);
        }
        return marker;
    }
}
