package com.videdesk.mobile.adapthub.model;

/**
 * Created by Joejo on 2018-03-02.
 */

public class Thread {

    private String uid, node, title, caption, created;

    public Thread() {
    }

    public Thread(String uid, String node, String title, String caption, String created) {
        this.uid = uid;
        this.node = node;
        this.title = title;
        this.caption = caption;
        this.created = created;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
