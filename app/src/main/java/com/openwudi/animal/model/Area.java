package com.openwudi.animal.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by diwu on 17/7/13.
 */

public class Area {
    @JSONField(name = "F_Id")
    private String id;

    @JSONField(name = "F_FullName")
    private String fullname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
