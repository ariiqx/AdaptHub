package com.videdesk.mobile.adapthub.model;

public class Region {
    private String node, nation_node, title;

    public Region() {
    }

    public Region(String node, String nation_node, String title) {
        this.node = node;
        this.nation_node = nation_node;
        this.title = title;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getNation_node() {
        return nation_node;
    }

    public void setNation_node(String nation_node) {
        this.nation_node = nation_node;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
