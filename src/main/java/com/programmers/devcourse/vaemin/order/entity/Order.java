package com.programmers.devcourse.vaemin.order.entity;

import com.programmers.devcourse.vaemin.coupon.entity.Coupon;
import com.programmers.devcourse.vaemin.payment.entity.Payment;
import com.programmers.devcourse.vaemin.root.AuditableEntity;
import com.programmers.devcourse.vaemin.shop.entity.Shop;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "food_order")
public class Order extends AuditableEntity {
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "shop_id", referencedColumnName = "id")
    private Shop shop;

    @OneToOne
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    private Payment payment;

    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "total_price", nullable = false)
    private int totalPrice;

    @OneToOne
    @JoinColumn(name = "applied_coupon_id", referencedColumnName = "id")
    private Coupon appliedCoupon;

    public Order(Customer customer, Shop shop, OrderStatus orderStatus, int totalPrice) {
        this.customer = customer;
        this.shop = shop;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
    }

    public Order() {

    }
}
