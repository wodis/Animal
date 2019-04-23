package com.openwudi.animal.model;

/**
 * Created by diwu on 17/4/24.
 */

public class SortItem {
    private String text;
    private int pic;

    public SortItem(String text, int pic) {
        this.text = text;
        this.pic = pic;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }
}
