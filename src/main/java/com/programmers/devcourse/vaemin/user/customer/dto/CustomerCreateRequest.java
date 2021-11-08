package com.programmers.devcourse.vaemin.user.customer.dto;

import lombok.Getter;

@Getter
public class CustomerCreateRequest {
    private String userName;
    private String email;
    private String phoneNum;
    private String locationCode;
    private String addressDetail;

    public CustomerCreateRequest(String username, String email, String phoneNum, String locationCode, String addressDetail) {
        this.userName = username;
        this.email = email;
        this.phoneNum = phoneNum;
        this.locationCode = locationCode;
        this.addressDetail = addressDetail;
    }
}
