package com.ilikexy.bigwork.entity;

public class MyAddFriendZ {
    private String musename;//账号
    private String mfakename;//昵称
    private String isAdded;
    public String getMusename() {
        return musename;
    }

    public void setMusename(String musename) {
        this.musename = musename;
    }

    public String getMfakename() {
        return mfakename;
    }

    public void setMfakename(String mfakename) {
        this.mfakename = mfakename;
    }
    public MyAddFriendZ(String usename,String fakename,String isadd){
        musename = usename;
        mfakename = fakename;
        isAdded = isadd;
    }
    public String isAdded() {
        return isAdded;
    }

    public void setAdded(String added) {
        isAdded = added;
    }
}
