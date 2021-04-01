package com.ilikexy.bigwork.entity;

public class Users {
    private String fakename;
    private String sex;
    private String age;
    private String hometown;
    private String address;
    private String signature;
    private String usename;
    public Users(String usename,String fakename,String sex,String age,String hometown,String address,String signature){
        this.usename = usename;
        this.fakename = fakename;
        this.sex = sex;
        this.age = age;
        this.hometown = hometown;
        this.address = address;
        this.signature = signature;
    }
    public String getFakename() {
        return fakename;
    }

    public void setFakename(String fakename) {
        this.fakename = fakename;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getUsename() {
        return usename;
    }

    public void setUsename(String usename) {
        this.usename = usename;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
