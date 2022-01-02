package com.programmers.devcourse.vaemin.coupon.entity;

import com.programmers.devcourse.vaemin.root.IdentifiableEntity;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "customer_coupon")
public class CustomerCoupon extends IdentifiableEntity {
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "coupon_id", referencedColumnName = "id")
    private Coupon coupon;

    @Builder
    public CustomerCoupon(Customer customer, Coupon coupon) {
        this.customer = customer;
        this.coupon = coupon;
    }
}
