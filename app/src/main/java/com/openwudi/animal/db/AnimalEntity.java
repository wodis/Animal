package com.openwudi.animal.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by diwu on 2017/8/25.
 */
@Entity
public class AnimalEntity {
    @Id
    private Long id;

    @NotNull
    private String aid;

    @NotNull
    private String animal;

    @NotNull
    private Date date;

    @Generated(hash = 2085749703)
    public AnimalEntity(Long id, @NotNull String aid, @NotNull String animal,
            @NotNull Date date) {
        this.id = id;
        this.aid = aid;
        this.animal = animal;
        this.date = date;
    }

    @Generated(hash = 870655894)
    public AnimalEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnimal() {
        return this.animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAid() {
        return this.aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }
}
