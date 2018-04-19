package com.videdesk.mobile.adapthub.model;

/**
 * Created by Joejo on 2017-12-14.
 */

public class Theme {

    private String node, title, caption, image, color;

    public Theme(){}

    public Theme(String node, String title, String caption, String image, String color) {
        this.node = node;
        this.title = title;
        this.caption = caption;
        this.image = image;
        this.color = color;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
