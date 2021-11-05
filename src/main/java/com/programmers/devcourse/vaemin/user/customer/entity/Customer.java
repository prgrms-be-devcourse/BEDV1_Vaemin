package com.programmers.devcourse.vaemin.user.customer.entity;

import com.programmers.devcourse.vaemin.coupon.entity.Coupon;
import com.programmers.devcourse.vaemin.coupon.entity.CustomerCoupon;
import com.programmers.devcourse.vaemin.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
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


    public void addCoupon(Coupon coupon) {
        CustomerCoupon customerCoupon = CustomerCoupon.builder()
                .coupon(coupon)
                .customer(this).build();
        this.coupons.add(customerCoupon);
        coupon.getCustomers().add(customerCoupon);
    }


    public Customer(String userName, String email, String phoneNum) {
        this.username = userName;
        this.email = email;
        this.phoneNum = phoneNum;
        this.point = 0;
    }
}
