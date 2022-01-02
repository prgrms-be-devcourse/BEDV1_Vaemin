package com.programmers.devcourse.vaemin.user.customer.dto;

import com.programmers.devcourse.vaemin.user.customer.entity.CustomerDeliveryAddress;
import lombok.Getter;

@Getter
public class CustomerDeliveryAddressResponse {
    private String locationCode;
    private String addressDetail;

    public CustomerDeliveryAddressResponse(String locationCode, String addressDetail) {
        this.locationCode = locationCode;
        this.addressDetail = addressDetail;
    }

    public CustomerDeliveryAddressResponse(CustomerDeliveryAddress address) {
        this.locationCode = address.getLocationCode();
        this.addressDetail = address.getAddressDetail();
    }
}
