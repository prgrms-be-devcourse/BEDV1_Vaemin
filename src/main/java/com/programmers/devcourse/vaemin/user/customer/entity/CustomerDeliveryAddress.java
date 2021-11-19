package com.programmers.devcourse.vaemin.user.customer.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
public class CustomerDeliveryAddress {

    @Column(name = "location_code", nullable = false, length = 20)
    private String locationCode;

    @Column(name = "address_detail", nullable = false, length = 50)
    private String addressDetail;

    public CustomerDeliveryAddress(String locationCode, String addressDetail) {
        this.locationCode = locationCode;
        this.addressDetail = addressDetail;
    }

    public CustomerDeliveryAddress() {

    }
}
