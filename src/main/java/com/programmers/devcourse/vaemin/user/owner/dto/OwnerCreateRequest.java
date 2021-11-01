package com.programmers.devcourse.vaemin.user.owner.dto;

public class OwnerCreateRequest {
    private String userName;
    private String email;
    private String phoneNum;

    public OwnerCreateRequest(String username, String email, String phoneNum) {
        this.userName = username;
        this.email = email;
        this.phoneNum = phoneNum;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhoneNum() {
        return this.phoneNum;
    }
}
