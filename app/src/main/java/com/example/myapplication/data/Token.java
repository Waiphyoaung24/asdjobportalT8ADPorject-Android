package com.example.myapplication.data;

import java.io.Serializable;

public class Token implements Serializable {
    private String access_token;
    private String refresh_token;
    private String username;

    public Token(){}
    public Token(String access_token) {
        this.access_token = access_token;
    }

    public Token(String access_token, String refresh_token, String username) {
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.username = username;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Token{" +
                "access_token='" + access_token + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}