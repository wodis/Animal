package com.openwudi.animal.location;

import android.content.Context;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;


public class LocationHelper {

    private OnLocationListener mOnLocationListener;
    public LocationClient mLocationClient = null;

    //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
    private final static int SPAN = 1000;

    public static LocationHelper build(Context c, OnLocationListener listener){
        return new LocationHelper(c,listener);
    }

    private LocationHelper(Context c, OnLocationListener listener){
        this.mOnLocationListener = listener;
        mLocationClient = new LocationClient(c);     //声明LocationClient类
        mLocationClient.registerLocationListener(new mBDLocationListener());    //注册监听函数
        initLocation();

    }

    class mBDLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if(null == location ||
                null == location.getAddress() ||
                TextUtils.isEmpty(location.getAddress().province)){

                if(mOnLocationListener != null){
                    mOnLocationListener.locError();
                }
            }else{

                if (location.getLocType() == BDLocation.TypeGpsLocation ||
                    location.getLocType() == BDLocation.TypeNetWorkLocation ||
                    location.getLocType() == BDLocation.TypeOffLineLocation){// GPS定位结果
                    //定位成功
                    if(mOnLocationListener != null){
                        mOnLocationListener.locSuccess(location.getAddress(),location.getLatitude(),location.getLongitude(), location.getAltitude());
                    }
                }else{
                    if(mOnLocationListener != null){
                        mOnLocationListener.locError();
                    }
                }

            }
            stop();
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(SPAN);
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(true);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setIsNeedAltitude(true);
        mLocationClient.setLocOption(option);
    }

    public void start(){
        if(null != mLocationClient) {
            mLocationClient.start();
        }
    }

    public void stop(){
        if(null != mLocationClient){
            mLocationClient.stop();
        }
    }
}
