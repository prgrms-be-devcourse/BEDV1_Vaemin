package com.programmers.devcourse.vaemin.user.customer.dto;

import com.programmers.devcourse.vaemin.coupon.entity.CustomerCoupon;
import com.programmers.devcourse.vaemin.coupon.entity.dto.CouponDTO;
import com.programmers.devcourse.vaemin.order.entity.dto.CustomerOrderDTO;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import com.programmers.devcourse.vaemin.user.customer.entity.CustomerDeliveryAddress;
import lombok.Getter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CustomerDTO {
    private final long id;
    private final int point;
    private final List<CouponDTO> coupons;
    private final List<CustomerOrderDTO> orders;
    private final CustomerDeliveryAddress currentAddress;

    public CustomerDTO(Customer customer) {
        this.id = customer.getId();
        this.point = customer.getPoint();
        this.coupons = customer.getCoupons().stream()
                .map(CustomerCoupon::getCoupon)
                .map(CouponDTO::new).collect(Collectors.toList());
        this.orders = customer.getOrders().stream()
                .map(CustomerOrderDTO::new).collect(Collectors.toList());
        this.currentAddress = customer.getCurrentAddress();
    }
}
