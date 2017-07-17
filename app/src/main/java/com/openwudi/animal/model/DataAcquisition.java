package com.openwudi.animal.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by diwu on 17/7/17.
 */

public class DataAcquisition {
    @JSONField(name = "F_Id")
    private String id;

    @JSONField(name = "F_HealthPic")
    private String healthPic;

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
}
