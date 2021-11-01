package com.programmers.devcourse.vaemin.user.customer.dto;

import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import lombok.Getter;

@Getter
public class CustomerDetailResponse {
    private int point;
    private String userName;
    private String email;

    public CustomerDetailResponse(Customer customer) {
        this.point = customer.getPoint();
        this.userName = customer.getUsername();
        this.email = customer.getEmail();
    }
}
