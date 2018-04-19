package com.videdesk.mobile.adapthub.model;

public class Like {
    private String uid, node, story_node;

    public Like() {
    }

    public Like(String uid, String node, String story_node) {
        this.uid = uid;
        this.node = node;
        this.story_node = story_node;
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

    public String getStory_node() {
        return story_node;
    }

    public void setStory_node(String story_node) {
        this.story_node = story_node;
    }
}
