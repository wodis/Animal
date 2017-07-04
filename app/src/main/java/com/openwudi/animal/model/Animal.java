package com.openwudi.animal.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by diwu on 17/7/4.
 */

public class Animal {
    @JSONField(name = "F_Id")
    private String id;

    @JSONField(name = "F_Name")
    private String name;

    @JSONField(name = "F_LDName")
    private String ldname;

    @JSONField(name = "F_Level")
    private int level;

    @JSONField(name = "F_Distribution")
    private String distribution;

    @JSONField(name = "F_PhysicalFeatures")
    private String physicalFeatures;

    @JSONField(name = "F_Photo")
    private String photo;

    @JSONField(name = "F_Environment")
    private String environment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLdname() {
        return ldname;
    }

    public void setLdname(String ldname) {
        this.ldname = ldname;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDistribution() {
        return distribution;
    }

    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }

    public String getPhysicalFeatures() {
        return physicalFeatures;
    }

    public void setPhysicalFeatures(String physicalFeatures) {
        this.physicalFeatures = physicalFeatures;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}
