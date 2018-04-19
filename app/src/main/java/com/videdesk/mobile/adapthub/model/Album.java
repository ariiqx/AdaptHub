package com.videdesk.mobile.adapthub.model;

/**
 * Created by Joejo on 2018-03-01.
 */

public class Album {

    private String node, uid, caption, image, created, status, read;

    public Album() {
    }

    public Album(String node, String uid, String created, String caption, String image, String status, String read) {
        this.node = node;
        this.uid = uid;
        this.created = created;
        this.caption = caption;
        this.image = image;
        this.status = status;
        this.read = read;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }
}
