package com.kgc.springboot_1.pojo;

public class User {
    private int userid;
    private String username;

    public User() {
    }

    public User(int userid, String username) {

        this.userid = userid;
        this.username = username;
    }

    public int getUserid() {

        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
