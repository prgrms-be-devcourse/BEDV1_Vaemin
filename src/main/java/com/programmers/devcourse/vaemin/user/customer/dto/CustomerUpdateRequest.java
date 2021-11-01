package com.programmers.devcourse.vaemin.user.customer.dto;

import java.time.LocalDateTime;

// question : dto 나누려고 updatedAt을 넣기는 했는데 바꾸는 게 낫나?
public class CustomerUpdateRequest {
    private String userName;
    private String email;
    private String phoneNum;
    private LocalDateTime updatedAt;

    public CustomerUpdateRequest(String userName, String email, String phoneNum) {
        this.userName = userName;
        this.email = email;
        this.phoneNum = phoneNum;
        this.updatedAt = LocalDateTime.now();
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
