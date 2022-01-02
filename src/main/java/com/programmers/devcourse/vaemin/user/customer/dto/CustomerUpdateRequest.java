package com.programmers.devcourse.vaemin.user.customer.dto;

import java.time.LocalDateTime;

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
