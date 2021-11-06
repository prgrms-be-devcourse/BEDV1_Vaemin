package com.programmers.devcourse.vaemin.user.customer.dto;

import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import lombok.Getter;

@Getter
public class CustomerAddressUpdateRequest {

    // question : Request에서 Customer 전부를 받아오고 난 후 CustomerDeliveryAddressService에서 생성할 때만 Customer.getName해야하는지
    //  Request에서 아예 Customer.getName()으로 String을 받아와야하나?
    private Customer customer;
    private String locationCode;
    private String addressDetail;

    public CustomerAddressUpdateRequest(Customer customer, String locationCode, String addressDetail) {
        this.customer = customer;
        this.locationCode = locationCode;
        this.addressDetail = addressDetail;
    }
}
