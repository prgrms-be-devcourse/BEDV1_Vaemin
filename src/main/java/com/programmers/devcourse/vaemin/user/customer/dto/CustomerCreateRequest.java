package com.programmers.devcourse.vaemin.user.customer.dto;

public class CustomerCreateRequest {
    private String userName;
    private String email;
    private String phoneNum;

    public CustomerCreateRequest(String username, String email, String phoneNum) {
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
