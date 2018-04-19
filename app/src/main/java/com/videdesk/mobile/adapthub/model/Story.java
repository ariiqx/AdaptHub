package com.videdesk.mobile.adapthub.model;

/**
 * Created by Joejo on 2018-03-19.
 */

public class Story {

    private String uid, node, theme_node, title, caption, details, image, video, document, album_node, url, created, status, read;

    public Story() {
    }

    public Story(String uid, String node, String theme_node, String title, String caption, String details, String image, String video,
                 String document, String album_node, String url, String created, String status, String read) {
        this.uid = uid;
        this.node = node;
        this.theme_node = theme_node;
        this.title = title;
        this.caption = caption;
        this.details = details;
        this.image = image;
        this.video = video;
        this.document = document;
        this.album_node = album_node;
        this.url = url;
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

    public String getTheme_node() {
        return theme_node;
    }

    public void setTheme_node(String theme_node) {
        this.theme_node = theme_node;
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getAlbum_node() {
        return album_node;
    }

    public void setAlbum_node(String album_node) {
        this.album_node = album_node;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
