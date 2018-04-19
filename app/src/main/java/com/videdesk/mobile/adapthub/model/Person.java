package com.videdesk.mobile.adapthub.model;

/**
 * Created by Joejo on 2018-03-01.
 */

public class Person {

    private String node, role, name, email, phone, image, nation, region, location, gender, created, status;

    public Person(){}

    public Person(String node, String role, String name, String email, String phone, String image,
                  String nation, String region, String location, String gender, String created, String status) {
        this.node = node;
        this.role = role;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.image = image;
        this.nation = nation;
        this.region = region;
        this.location = location;
        this.gender = gender;
        this.created = created;
        this.status = status;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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
}
