package com.programmers.devcourse.vaemin.user.customer.entity;

import com.programmers.devcourse.vaemin.coupon.entity.Coupon;
import com.programmers.devcourse.vaemin.coupon.entity.CustomerCoupon;
import com.programmers.devcourse.vaemin.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Customer extends User {
    @Column(name = "point", nullable = false)
    private int point; // mileage

    @OneToMany(mappedBy = "customer", cascade = CascadeType.PERSIST)
    private final List<CustomerCoupon> coupons = new ArrayList<>();

    @Column(name = "location_code", nullable = false, length = 20)
    private String locationCode;

    @Column(name = "address_detail", nullable = false, length = 50)
    private String addressDetail;

    public void addCoupon(Coupon coupon) {
        CustomerCoupon customerCoupon = CustomerCoupon.builder()
                .coupon(coupon)
                .customer(this).build();
        this.coupons.add(customerCoupon);
        coupon.getCustomers().add(customerCoupon);
    }


    public Customer(String userName, String email, String phoneNum, String locationCode, String addressDetail) {
        this.username = userName;
        this.email = email;
        this.phoneNum = phoneNum;
        this.point = 0;
        this.locationCode = locationCode;
        this.addressDetail = addressDetail;
    }
}
