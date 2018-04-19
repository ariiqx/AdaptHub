package com.videdesk.mobile.adapthub.model;

/**
 * Created by Joejo on 2018-03-14.
 */

public class Photo {

    private String node, album_node, created, caption, image;

    public Photo() {
    }

    public Photo(String node, String album_node, String created, String caption, String image) {
        this.node = node;
        this.album_node = album_node;
        this.created = created;
        this.caption = caption;
        this.image = image;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getAlbum_node() {
        return album_node;
    }

    public void setAlbum_node(String album_node) {
        this.album_node = album_node;
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
}
