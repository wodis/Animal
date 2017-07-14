package com.openwudi.animal.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by diwu on 17/7/14.
 */

public class Message {
    @JSONField(name = "F_Id")
    private String id;

    @JSONField(name = "F_Title")
    private String title;

    @JSONField(name = "F_Content")
    private String content;

    @JSONField(name = "F_PostDate")
    private String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
