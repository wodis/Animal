package com.openwudi.animal.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by diwu on 17/7/17.
 */

public class DataAcquisition implements Serializable {
    @JSONField(name = "F_Id")
    private String id;

    @JSONField(name = "F_MonitorStation_Id")
    private String monitorStationId;

    @JSONField(name = "F_Terminal_Id")
    private String terminalId;

    @JSONField(name = "F_CollectionTime")
    private String collectionTime;

    @JSONField(name = "F_Animal_Id")
    private String animalId;

    @JSONField(name = "F_AnimalName")
    private String animalName;

    @JSONField(name = "F_TotalNumber")
    private int total;

    @JSONField(name = "F_AnimaState_Id")
    private String animaStateId;

    @JSONField(name = "F_HealthNumber")
    private int healthNum;

    @JSONField(name = "F_HealthPic")
    private String healthPic;

    @JSONField(name = "F_IllNumber")
    private int illNum;

    @JSONField(name = "F_IllDes")
    private String illDesc;

    @JSONField(name = "F_IllPic")
    private String illPic;

    @JSONField(name = "F_DeathNumber")
    private int deathNum;

    @JSONField(name = "F_DeathDes")
    private String deathDesc;

    @JSONField(name = "F_DeathPic")
    private String deathPic;

    @JSONField(name = "F_Position_Id")
    private String position;

    @JSONField(name = "F_Azimuth_Id")
    private String azimuth;

    @JSONField(name = "F_Distance_Id")
    private String distance;

    @JSONField(name = "F_Habitat_Id")
    private String habitate;

    @JSONField(name = "F_CollectionLongitude")
    private String longtitude;

    @JSONField(name = "F_CollectionLatitude")
    private String latitude;

    @JSONField(name = "F_Altitude")
    private int altitude;

    @JSONField(name = "F_UploadTime")
    private String uploadTime;

    @JSONField(name = "F_UploadName")
    private String uploadName;

    @JSONField(name = "F_IfBubao")
    private int bubao;

    @JSONField(name = "F_BubaoDes")
    private String bubaoDesc;

    @JSONField(name = "F_BubaoTime")
    private String bubaoTime;

    @JSONField(name = "F_UseObject_Id")
    private String useObjectId;

    @JSONField(name = "F_CreatorUserId")
    private String creatorUserId;

    @JSONField(name = "F_CreatorTime")
    private String creatorTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHealthPic() {
        return healthPic;
    }

    public void setHealthPic(String healthPic) {
        this.healthPic = healthPic;
    }

    public String getMonitorStationId() {
        return monitorStationId;
    }

    public void setMonitorStationId(String monitorStationId) {
        this.monitorStationId = monitorStationId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getCollectionTime() {
        return collectionTime;
    }

    public void setCollectionTime(String collectionTime) {
        this.collectionTime = collectionTime;
    }

    public String getAnimalId() {
        return animalId;
    }

    public void setAnimalId(String animalId) {
        this.animalId = animalId;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getAnimaStateId() {
        return animaStateId;
    }

    public void setAnimaStateId(String animaStateId) {
        this.animaStateId = animaStateId;
    }

    public int getHealthNum() {
        return healthNum;
    }

    public void setHealthNum(int healthNum) {
        this.healthNum = healthNum;
    }

    public int getIllNum() {
        return illNum;
    }

    public void setIllNum(int illNum) {
        this.illNum = illNum;
    }

    public String getIllDesc() {
        return illDesc;
    }

    public void setIllDesc(String illDesc) {
        this.illDesc = illDesc;
    }

    public String getIllPic() {
        return illPic;
    }

    public void setIllPic(String illPic) {
        this.illPic = illPic;
    }

    public int getDeathNum() {
        return deathNum;
    }

    public void setDeathNum(int deathNum) {
        this.deathNum = deathNum;
    }

    public String getDeathDesc() {
        return deathDesc;
    }

    public void setDeathDesc(String deathDesc) {
        this.deathDesc = deathDesc;
    }

    public String getDeathPic() {
        return deathPic;
    }

    public void setDeathPic(String deathPic) {
        this.deathPic = deathPic;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(String azimuth) {
        this.azimuth = azimuth;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getHabitate() {
        return habitate;
    }

    public void setHabitate(String habitate) {
        this.habitate = habitate;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getUploadName() {
        return uploadName;
    }

    public void setUploadName(String uploadName) {
        this.uploadName = uploadName;
    }

    public int getBubao() {
        return bubao;
    }

    public void setBubao(int bubao) {
        this.bubao = bubao;
    }

    public String getBubaoDesc() {
        return bubaoDesc;
    }

    public void setBubaoDesc(String bubaoDesc) {
        this.bubaoDesc = bubaoDesc;
    }

    public String getBubaoTime() {
        return bubaoTime;
    }

    public void setBubaoTime(String bubaoTime) {
        this.bubaoTime = bubaoTime;
    }

    public String getUseObjectId() {
        return useObjectId;
    }

    public void setUseObjectId(String useObjectId) {
        this.useObjectId = useObjectId;
    }

    public String getAnimalName() {
        return animalName;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }

    public String getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(String creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public String getCreatorTime() {
        return creatorTime;
    }

    public void setCreatorTime(String creatorTime) {
        this.creatorTime = creatorTime;
    }
}
