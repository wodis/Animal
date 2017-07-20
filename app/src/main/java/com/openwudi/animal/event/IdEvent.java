package com.openwudi.animal.event;

/**
 * Created by diwu on 17/7/20.
 */

public class IdEvent {
    private String id;

    public IdEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
