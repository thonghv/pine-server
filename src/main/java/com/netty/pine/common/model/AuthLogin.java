package com.netty.pine.common.model;

public class AuthLogin {

    private String userName;
    private String token;


    public AuthLogin(String username, String token) {
        this.userName = username;
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

