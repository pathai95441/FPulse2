package com.example.fpulse;

public class UsersGetter {
    String longtijude;
    String serial;
    String picUrl;
    String pulseValue;
    String tags;
    String name;
    String pulseshow;
    String latijude;

    public UsersGetter(String serial, String picUrl,String pulseValue,String tags,String name,String pulseshow,String latijude,String longtijude) {
        this.serial = serial;
        this.picUrl = picUrl;
        this.pulseValue = pulseValue;
        this.tags = tags;
        this.name = name;
        this.pulseshow = pulseshow;
        this.longtijude = longtijude;
        this.latijude =latijude;
    }

    public UsersGetter() {
    }

    public String getSerial() {
        return serial;
    }

    public void setLongtijude(String longtijude) {
        this.longtijude = longtijude;
    }

    public void setLatijude(String latijude) {
        this.latijude = latijude;
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

    public String getLatijude() {
        return latijude;
    }

    public String getPulseValue(){return pulseValue;}

    public void setPulseValue(String pulseValue) {
        this.pulseValue = pulseValue;
    }

    public String getTags() {
        return tags;
    }

    public String getLongtijude() {
        return longtijude;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPulseshow() {
        return pulseshow;
    }

    public void setPulseshow(String pulseshow) {
        this.pulseshow = pulseshow;
    }
}
