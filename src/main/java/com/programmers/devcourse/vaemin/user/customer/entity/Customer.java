package com.programmers.devcourse.vaemin.user.customer.entity;

import com.programmers.devcourse.vaemin.coupon.entity.CustomerCoupon;
import com.programmers.devcourse.vaemin.user.User;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Customer extends User {
    @Column(name = "point", nullable = false)
    private int point; // mileage

    @OneToMany(mappedBy = "customer")
    @Column(name = "customer_coupon_id")
    private final List<CustomerCoupon> coupons = new ArrayList<>();

    // question : 처음 생성했을 때 updatedAt이 null일텐데 왜 nullable = false?
    public Customer(String userName, String email, String phoneNum) {
        this.username = userName;
        this.email = email;
        this.phoneNum = phoneNum;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.point = 0;
    }

    public Customer() {

    }
}
