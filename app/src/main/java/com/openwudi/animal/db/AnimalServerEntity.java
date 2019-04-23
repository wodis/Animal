package com.openwudi.animal.db;

import com.alibaba.fastjson.annotation.JSONField;
import com.openwudi.animal.model.Animal;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by diwu on 17/7/4.
 */
@Entity
public class AnimalServerEntity {
    @Id
    private Long id;

    @Unique
    private String fid;

    private String name;

    private String ldname;

    private int level;

    private String distribution;

    private String physicalFeatures;

    private String photo;

    private String environment;

    private String pinyin;

    private String pinyinInitials;

    @Generated(hash = 2051686404)
    public AnimalServerEntity(Long id, String fid, String name, String ldname,
                              int level, String distribution, String physicalFeatures, String photo,
                              String environment, String pinyin, String pinyinInitials) {
        this.id = id;
        this.fid = fid;
        this.name = name;
        this.ldname = ldname;
        this.level = level;
        this.distribution = distribution;
        this.physicalFeatures = physicalFeatures;
        this.photo = photo;
        this.environment = environment;
        this.pinyin = pinyin;
        this.pinyinInitials = pinyinInitials;
    }

    @Generated(hash = 899930202)
    public AnimalServerEntity() {
    }

    public AnimalServerEntity(Animal animal) {
        this.fid = animal.getId();
        this.name = animal.getName();
        this.ldname = animal.getLdname();
        this.level = animal.getLevel();
        this.distribution = animal.getDistribution();
        this.physicalFeatures = animal.getPhysicalFeatures();
        this.photo = animal.getPhoto();
        this.environment = animal.getEnvironment();
        this.pinyin = animal.getPinyin();
        this.pinyinInitials = animal.getPinyinInitials();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFid() {
        return this.fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLdname() {
        return this.ldname;
    }

    public void setLdname(String ldname) {
        this.ldname = ldname;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDistribution() {
        return this.distribution;
    }

    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }

    public String getPhysicalFeatures() {
        return this.physicalFeatures;
    }

    public void setPhysicalFeatures(String physicalFeatures) {
        this.physicalFeatures = physicalFeatures;
    }

    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEnvironment() {
        return this.environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getPinyin() {
        return this.pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getPinyinInitials() {
        return this.pinyinInitials;
    }

    public void setPinyinInitials(String pinyinInitials) {
        this.pinyinInitials = pinyinInitials;
    }
}
