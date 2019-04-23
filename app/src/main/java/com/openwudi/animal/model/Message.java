package com.openwudi.animal.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by diwu on 17/7/14.
 */

public class Message implements Serializable{
    @JSONField(name = "F_Id")
    private String id;

    @JSONField(name = "F_Title")
    private String title;

    @JSONField(name = "F_Content")
    private String content;

    @JSONField(name = "F_PostDate")
    private String date;

    @JSONField(name = "F_PostName")
    private String postName;

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

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }
}
