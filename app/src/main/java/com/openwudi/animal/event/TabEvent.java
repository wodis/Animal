package com.openwudi.animal.event;

/**
 * Created by diwu on 17/7/14.
 */

public class TabEvent {
    private int position;

    public TabEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
