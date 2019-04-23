package com.openwudi.animal.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by diwu on 2017/8/29.
 */
@Entity
public class TelEntity {
    @Id
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String tel;

    @Generated(hash = 1738529793)
    public TelEntity(Long id, @NotNull String name, @NotNull String tel) {
        this.id = id;
        this.name = name;
        this.tel = tel;
    }

    @Generated(hash = 1720453590)
    public TelEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return this.tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
