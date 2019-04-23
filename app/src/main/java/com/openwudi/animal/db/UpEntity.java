package com.openwudi.animal.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by diwu on 17/7/18.
 */

@Entity
public class UpEntity {
    @Id
    private Long id;

    @NotNull
    private String userId;

    @NotNull
    private String animal;

    private String qixidi;

    private String zhuangtai;

    private String juli;

    private String fangwei;

    private String weizhi;

    @NotNull
    private String data;

    @NotNull
    private Date date;

    @Generated(hash = 296538277)
    public UpEntity(Long id, @NotNull String userId, @NotNull String animal,
            String qixidi, String zhuangtai, String juli, String fangwei,
            String weizhi, @NotNull String data, @NotNull Date date) {
        this.id = id;
        this.userId = userId;
        this.animal = animal;
        this.qixidi = qixidi;
        this.zhuangtai = zhuangtai;
        this.juli = juli;
        this.fangwei = fangwei;
        this.weizhi = weizhi;
        this.data = data;
        this.date = date;
    }

    @Generated(hash = 154876131)
    public UpEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAnimal() {
        return this.animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public String getQixidi() {
        return this.qixidi;
    }

    public void setQixidi(String qixidi) {
        this.qixidi = qixidi;
    }

    public String getZhuangtai() {
        return this.zhuangtai;
    }

    public void setZhuangtai(String zhuangtai) {
        this.zhuangtai = zhuangtai;
    }

    public String getJuli() {
        return this.juli;
    }

    public void setJuli(String juli) {
        this.juli = juli;
    }

    public String getFangwei() {
        return this.fangwei;
    }

    public void setFangwei(String fangwei) {
        this.fangwei = fangwei;
    }

    public String getWeizhi() {
        return this.weizhi;
    }

    public void setWeizhi(String weizhi) {
        this.weizhi = weizhi;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
