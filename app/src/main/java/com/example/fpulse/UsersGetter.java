package com.example.fpulse;

public class UsersGetter {
    String serial;
    String picUrl;

    public UsersGetter(String serial, String picUrl) {
        this.serial = serial;
        this.picUrl = picUrl;
    }

    public UsersGetter() {
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
