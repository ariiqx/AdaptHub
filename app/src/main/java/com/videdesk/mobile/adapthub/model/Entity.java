package com.videdesk.mobile.adapthub.model;

/**
 * Created by Joejo on 2018-03-02.
 */

public class Entity {

    private String uid, node, title, caption, about, details, phone, email, url,
            postal, location, nation, image, created, status, read;

    public Entity(){}

    public Entity(String uid, String node, String title, String caption, String about,
                  String details, String phone, String email, String url, String postal,
                  String location, String nation, String image, String created,
                  String status, String read) {
        this.uid = uid;
        this.node = node;
        this.title = title;
        this.caption = caption;
        this.about = about;
        this.details = details;
        this.phone = phone;
        this.email = email;
        this.url = url;
        this.postal = postal;
        this.location = location;
        this.nation = nation;
        this.image = image;
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

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
