package com.programmers.devcourse.vaemin.user.customer.dto;

import lombok.Getter;

@Getter
public class CustomerDeliveryAddressRequest {
    private String locationCode;
    private String addressDetail;

    public CustomerDeliveryAddressRequest(String locationCode, String addressDetail) {
        this.locationCode = locationCode;
        this.addressDetail = addressDetail;
    }
}
