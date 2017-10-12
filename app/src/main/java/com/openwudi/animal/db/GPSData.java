package com.openwudi.animal.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by diwu on 17/7/24.
 */
@Entity
public class GPSData {
    @Id
    private Long id;

    @NotNull
    String terminalId;

    @NotNull
    double latitude;

    @NotNull
    double longtitude;

    @NotNull
    @Unique
    Date createTime;

    @NotNull
    String userId;

    String uuid;

    double lineLength;

    @Generated(hash = 1790871638)
    public GPSData(Long id, @NotNull String terminalId, double latitude, double longtitude,
            @NotNull Date createTime, @NotNull String userId, String uuid,
            double lineLength) {
        this.id = id;
        this.terminalId = terminalId;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.createTime = createTime;
        this.userId = userId;
        this.uuid = uuid;
        this.lineLength = lineLength;
    }

    @Generated(hash = 311243752)
    public GPSData() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTerminalId() {
        return this.terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return this.longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public double getLineLength() {
        return this.lineLength;
    }

    public void setLineLength(double lineLength) {
        this.lineLength = lineLength;
    }
}
