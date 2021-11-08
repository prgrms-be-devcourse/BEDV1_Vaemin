package com.programmers.devcourse.vaemin.user.customer.entity;

import com.programmers.devcourse.vaemin.coupon.entity.Coupon;
import com.programmers.devcourse.vaemin.coupon.entity.CustomerCoupon;
import com.programmers.devcourse.vaemin.order.entity.Order;
import com.programmers.devcourse.vaemin.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "customer")
public class Customer extends User {
    @Column(name = "point", nullable = false)
    private int point; // mileage

    @OneToMany(mappedBy = "customer", cascade = CascadeType.PERSIST)
    private final List<CustomerCoupon> coupons = new ArrayList<>();


    @OneToMany(mappedBy = "customer", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<Order> orders = new ArrayList<>();

    public void addOrder(Order order) {
        this.orders.add(order);
        order.changeCustomer(this);
    }
  
    @Column(name = "location_code", nullable = false, length = 20)
    private String locationCode;

    @Column(name = "address_detail", nullable = false, length = 50)
    private String addressDetail;

    public void changeLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public void changeAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public void addCoupon(Coupon coupon) {
        CustomerCoupon customerCoupon = CustomerCoupon.builder()
                .coupon(coupon)
                .customer(this).build();
        this.coupons.add(customerCoupon);
        coupon.getCustomers().add(customerCoupon);
    }

    @Builder
    public Customer(String userName, String email, String phoneNum, String locationCode, String addressDetail) {
        this.username = userName;
        this.email = email;
        this.phoneNum = phoneNum;
        this.point = 0;
        this.locationCode = locationCode;
        this.addressDetail = addressDetail;
    }
}
