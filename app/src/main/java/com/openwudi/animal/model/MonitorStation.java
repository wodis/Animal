package com.openwudi.animal.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by diwu on 17/7/13.
 */

public class MonitorStation {
    @JSONField(name = "F_Id")
    private String id;

    @JSONField(name = "F_Name")
    private String name;

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
}
