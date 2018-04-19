package com.videdesk.mobile.adapthub.model;

public class Chat {
    private String uid, node, thread_node, details, created, read;

    public Chat() {
    }

    public Chat(String uid, String node, String thread_node, String details, String created, String read) {
        this.uid = uid;
        this.node = node;
        this.thread_node = thread_node;
        this.details = details;
        this.created = created;
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

    public String getThread_node() {
        return thread_node;
    }

    public void setThread_node(String thread_node) {
        this.thread_node = thread_node;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }
}
