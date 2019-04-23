package com.openwudi.animal.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.blankj.utilcode.utils.ConstUtils;
import com.blankj.utilcode.utils.TimeUtils;
import com.openwudi.animal.db.GPSData;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by diwu on 17/7/24.
 */

public class GPSDataModel {
    @JSONField(name = "Terminal_Id")
    private String terminalId;

    @JSONField(name = "UploadTime")
    private String uploadTime;

    @JSONField(name = "F_Longitude")
    private String lng;

    @JSONField(name = "F_Latitude")
    private String lat;

    @JSONField(name = "F_CreatorUserId")
    private String userId;

    @JSONField(name = "F_LineLength")
    private String lineLength;

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLineLength() {
        return lineLength;
    }

    public void setLineLength(String lineLength) {
        this.lineLength = lineLength;
    }

    public GPSDataModel() {

    }

    public static final SimpleDateFormat DEFAULT_SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());

    public GPSDataModel(GPSData gpsData) {
        setUserId(gpsData.getUserId());
        setTerminalId(gpsData.getTerminalId());
        setLat(gpsData.getLatitude() + "");
        setLng(gpsData.getLongtitude() + "");
        setUploadTime(TimeUtils.date2String(gpsData.getCreateTime() , DEFAULT_SDF));
        if (gpsData.getLineLength() < 1D) {
            setLineLength("1");
        } else {
            setLineLength((int) Math.rint(gpsData.getLineLength()) + "");
        }
    }
}
