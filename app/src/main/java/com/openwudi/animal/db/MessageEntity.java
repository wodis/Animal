package com.openwudi.animal.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by diwu on 2017/9/7.
 */
@Entity
public class MessageEntity {
    @Id
    private Long id;

    @NotNull
    @Unique
    private String fid;

    @Generated(hash = 2129735665)
    public MessageEntity(Long id, @NotNull String fid) {
        this.id = id;
        this.fid = fid;
    }

    @Generated(hash = 1797882234)
    public MessageEntity() {
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
}
