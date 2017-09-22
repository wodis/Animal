package com.openwudi.animal.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.api.track.DistanceResponse;
import com.baidu.trace.api.track.HistoryTrackRequest;
import com.baidu.trace.api.track.HistoryTrackResponse;
import com.baidu.trace.api.track.LatestPointResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.api.track.TrackPoint;
import com.baidu.trace.model.ProcessOption;
import com.baidu.trace.model.SortType;
import com.baidu.trace.model.StatusCodes;
import com.baidu.trace.model.TransportMode;
import com.blankj.utilcode.utils.EmptyUtils;
import com.blankj.utilcode.utils.TimeUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.openwudi.animal.R;
import com.openwudi.animal.base.AnimalApplication;
import com.openwudi.animal.base.BaseActivity;
import com.openwudi.animal.utils.BitmapUtil;
import com.openwudi.animal.utils.CommonUtil;
import com.openwudi.animal.utils.Constants;
import com.openwudi.animal.utils.MapUtil;
import com.openwudi.animal.utils.MapUtil2;
import com.openwudi.animal.view.TitleBarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by diwu on 2017/9/5.
 */

public class MapQueryActivity extends BaseActivity implements BaiduMap.OnMarkerClickListener {

    @BindView(R.id.titlebar)
    TitleBarView titlebar;
    @BindView(R.id.map_view)
    MapView mapView;

    private String date = "";

    private BaiduMap baiduMap;

    /**
     * 查询轨迹的开始时间
     */
    private long startTime = CommonUtil.getCurrentTime();

    /**
     * 查询轨迹的结束时间
     */
    private long endTime = CommonUtil.getCurrentTime();
    private double lat = 40.050966;
    private double lon = 116.303128;

    private AnimalApplication trackApp = null;

    /**
     * 历史轨迹请求
     */
    private HistoryTrackRequest historyTrackRequest = new HistoryTrackRequest();
    private AtomicInteger mSequenceGenerator = new AtomicInteger();
    private int pageIndex = 1;
    /**
     * 轨迹点集合
     */
    private List<LatLng> trackPoints = new ArrayList<>();
    /**
     * 轨迹监听器（用于接收历史轨迹回调）
     */
    private OnTrackListener mTrackListener = null;

    /**
     * 地图工具
     */
    private MapUtil2 mapUtil = null;

    /**
     * 轨迹排序规则
     */
    private SortType sortType = SortType.asc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        date = getIntent().getStringExtra("date");
        trackApp = AnimalApplication.INSTANCE;

        //BaiduMap管理具体的某一个MapView： 旋转，移动，缩放，事件。。。
        baiduMap = mapView.getMap();

        //设置缩放级别，默认级别为12
        MapStatusUpdate mapstatusUpdate = MapStatusUpdateFactory.zoomTo(19);
        baiduMap.setMapStatus(mapstatusUpdate);

        titlebar.setTitle(date);
        titlebar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        startTime = TimeUtils.string2Milliseconds(date, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())) / 1000L;
        endTime = startTime + 3600 * 23 + 3500;
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mapUtil = MapUtil2.getInstance();
        mapUtil.init(mapView);
        mapUtil.baiduMap.setOnMarkerClickListener(this);
//        mapUtil.setCenter(trackApp);

        ProcessOption processOption = new ProcessOption();
        // 设置需要去噪
        processOption.setNeedDenoise(true);
        // 设置需要抽稀
        processOption.setNeedVacuate(true);
        // 设置需要绑路
        processOption.setNeedMapMatch(true);
        // 设置精度过滤值(定位精度大于100米的过滤掉)
        processOption.setRadiusThreshold(100);
        // 设置交通方式为驾车
        processOption.setTransportMode(TransportMode.walking);
//        historyTrackRequest.setProcessOption(processOption);
////        // 设置需要纠偏
//        historyTrackRequest.setProcessed(true);
        initListener();
        queryHistoryTrack();
    }

    private void initListener() {
        mTrackListener = new OnTrackListener() {
            @Override
            public void onHistoryTrackCallback(HistoryTrackResponse response) {
                int total = response.getTotal();
                if (StatusCodes.SUCCESS != response.getStatus()) {
                    ToastUtils.showShortToast(mContext, response.getMessage());
                } else if (0 == total) {
                    ToastUtils.showShortToast(mContext, getString(R.string.no_track_data));
                } else {
                    List<TrackPoint> points = response.getTrackPoints();
                    if (null != points) {
                        for (TrackPoint trackPoint : points) {
                            if (!CommonUtil.isZeroPoint(trackPoint.getLocation().getLatitude(),
                                    trackPoint.getLocation().getLongitude())) {
                                trackPoints.add(MapUtil.convertTrace2Map(trackPoint.getLocation()));
                            }
                        }
                    }
                }

                if (total > Constants.PAGE_SIZE * pageIndex) {
                    historyTrackRequest.setPageIndex(++pageIndex);
                    queryHistoryTrack();
                } else {
//                    mapUtil.drawHistoryTrack(trackPoints, sortType);
                    draw(trackPoints);
                }
            }

            @Override
            public void onDistanceCallback(DistanceResponse response) {
                super.onDistanceCallback(response);
            }

            @Override
            public void onLatestPointCallback(LatestPointResponse response) {
                super.onLatestPointCallback(response);
            }
        };
    }

    /**
     * 查询历史轨迹
     */
    private void queryHistoryTrack() {
        trackApp.initRequest(historyTrackRequest);
        historyTrackRequest.setEntityName(trackApp.entityName);
        historyTrackRequest.setStartTime(startTime);
        historyTrackRequest.setEndTime(endTime);
        historyTrackRequest.setPageIndex(pageIndex);
        historyTrackRequest.setPageSize(Constants.PAGE_SIZE);
        trackApp.mClient.queryHistoryTrack(historyTrackRequest, mTrackListener);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    private void draw(List<LatLng> points) {
        if (EmptyUtils.isEmpty(points)) {
            ToastUtils.showShortToast(mContext, "无记录");
            finish();
            return;
        }

//构造纹理资源
        BitmapDescriptor custom1 = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_road_red_arrow);
        BitmapDescriptor custom2 = BitmapDescriptorFactory
                .fromAsset("icon_road_green_arrow.png");
        BitmapDescriptor custom3 = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_road_blue_arrow);

        //构造纹理队列
        List<BitmapDescriptor> customList = new ArrayList<BitmapDescriptor>();
        customList.add(custom1);
        customList.add(custom2);
        customList.add(custom3);

        List<LatLng> latLngs = new ArrayList<>();
        List<Integer> index = new ArrayList<Integer>();
        for (int i = 0; i < points.size(); i++) {
            LatLng latLng = points.get(i);
            latLngs.add(latLng);
            index.add(1);
        }

        MapStatusUpdate mapstatusUpdatePoint = MapStatusUpdateFactory.newLatLng(latLngs.get(0));
        baiduMap.setMapStatus(mapstatusUpdatePoint);
        addOverlay(latLngs.get(0), BitmapUtil.bmStart, null);
        addOverlay(latLngs.get(latLngs.size() - 1), BitmapUtil.bmEnd, null);


        OverlayOptions ooPolyline = new PolylineOptions().width(16).color(mContext.getResources().getColor(R.color.colorPrimary)).points(latLngs).textureIndex(index).customTextureList(customList);

        //在地图上画出线条图层，mPolyline：线条图层
        Polyline mPolyline = (Polyline) baiduMap.addOverlay(ooPolyline);
        mPolyline.setDottedLine(true);
        mPolyline.setZIndex(3);
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

    @Override
    protected void onResume() {
        super.onResume();
        mapUtil.onResume();

        // 在Android 6.0及以上系统，若定制手机使用到doze模式，请求将应用添加到白名单。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = trackApp.getPackageName();
            PowerManager powerManager = (PowerManager) trackApp.getSystemService(Context.POWER_SERVICE);
            boolean isIgnoring = powerManager.isIgnoringBatteryOptimizations(packageName);
            if (!isIgnoring) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                try {
                    startActivity(intent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapUtil.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapUtil.clear();
    }
}
