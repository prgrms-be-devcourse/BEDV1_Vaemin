package com.programmers.devcourse.vaemin.coupon.entity;

import com.programmers.devcourse.vaemin.root.IdentifiableEntity;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class CustomerCoupon extends IdentifiableEntity {
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "coupon_id", referencedColumnName = "id")
    private Coupon coupon;
}
