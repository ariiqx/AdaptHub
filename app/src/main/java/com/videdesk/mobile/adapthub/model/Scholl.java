package com.videdesk.mobile.adapthub.model;

public class Scholl {

    private String uid, node, title, caption, image, deadline, created, status, read;

    public Scholl() {
    }

    public Scholl(String uid, String node, String title, String caption, String image,
                  String deadline, String created, String status, String read) {
        this.uid = uid;
        this.node = node;
        this.title = title;
        this.caption = caption;
        this.image = image;
        this.deadline = deadline;
        this.created = created;
        this.status = status;
        this.read = read;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
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
