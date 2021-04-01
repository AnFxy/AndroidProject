package com.ilikexy.bigwork.entity;

public class MyAddFriend {
    private String musename;//账号
    private String mfakename;//昵称
    private boolean isAdded;
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
    public MyAddFriend(String usename,String fakename,boolean isadd){
        musename = usename;
        mfakename = fakename;
        isAdded = isadd;
    }
    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean added) {
        isAdded = added;
    }
}
