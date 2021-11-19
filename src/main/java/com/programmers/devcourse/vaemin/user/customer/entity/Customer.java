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

    @Embedded
    private CustomerDeliveryAddress currentAddress;

    public void addOrder(Order order) {
        this.orders.add(order);
        order.changeCustomer(this);
    }

    public void addCoupon(Coupon coupon) {
        CustomerCoupon customerCoupon = CustomerCoupon.builder()
                .coupon(coupon)
                .customer(this).build();
        this.coupons.add(customerCoupon);
        coupon.getCustomers().add(customerCoupon);
    }

    public void changeAddress(CustomerDeliveryAddress address) {
        this.currentAddress = address;
    }
    @Builder
    public Customer(String userName, String email, String phoneNum, CustomerDeliveryAddress currentAddress) {
        this.username = userName;
        this.email = email;
        this.phoneNum = phoneNum;
        this.point = 0;
        this.currentAddress = currentAddress;
    }
}
