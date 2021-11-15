package com.programmers.devcourse.vaemin.coupon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.programmers.devcourse.vaemin.coupon.entity.CouponDiscountType;

import java.time.LocalDateTime;

public class TestCouponDTO {
    private long id;

    private String name;

    private CouponDiscountType discountType;

    private int discountAmount;

    private int minimumOrderPrice;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expirationDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CouponDiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(CouponDiscountType discountType) {
        this.discountType = discountType;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }

    public int getMinimumOrderPrice() {
        return minimumOrderPrice;
    }

    public void setMinimumOrderPrice(int minimumOrderPrice) {
        this.minimumOrderPrice = minimumOrderPrice;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }
}
