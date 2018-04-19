package com.videdesk.mobile.adapthub.model;

public class Nation {
    private String node, code, title, dial;

    public Nation() {
    }

    public Nation(String node, String code, String title, String dial) {
        this.node = node;
        this.code = code;
        this.title = title;
        this.dial = dial;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDial() {
        return dial;
    }

    public void setDial(String dial) {
        this.dial = dial;
    }
}
