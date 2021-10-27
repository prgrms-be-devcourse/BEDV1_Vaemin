package com.programmers.devcourse.vaemin.coupon.entity;

import com.programmers.devcourse.vaemin.shop.entity.Shop;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Coupon {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "discount_type", nullable = false)
    private CouponDiscountType discountType;

    @Column(name = "discount_amount", nullable = false)
    private int discountAmount;

    @Column(name = "minimum_order_price", nullable = false)
    private int minimumOrderPrice;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @ManyToOne
    @JoinColumn(name = "shop_id", referencedColumnName = "id")
    private Shop shop;
}
