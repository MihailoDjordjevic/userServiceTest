package com.raf.cinemauserservice.dto;

public class BanRequest {


    private String username;

    public BanRequest(String username) {
        this.username = username;
    }

    public BanRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
