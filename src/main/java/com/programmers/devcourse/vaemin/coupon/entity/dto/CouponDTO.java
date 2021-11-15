package com.programmers.devcourse.vaemin.coupon.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.programmers.devcourse.vaemin.coupon.entity.Coupon;
import com.programmers.devcourse.vaemin.coupon.entity.CouponDiscountType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CouponDTO {
    @JsonProperty("id")
    private final long id;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("discountType")
    private final CouponDiscountType discountType;

    @JsonProperty("discountAmount")
    private final int discountAmount;

    @JsonProperty("minimumOrderPrice")
    private final int minimumOrderPrice;

    @JsonProperty("expirationDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime expirationDate;


    public CouponDTO(Coupon coupon) {
        this.id = coupon.getId();
        this.name = coupon.getName();
        this.discountType = coupon.getDiscountType();
        this.discountAmount = coupon.getDiscountAmount();
        this.minimumOrderPrice = coupon.getMinimumOrderPrice();
        this.expirationDate = coupon.getExpirationDate();
    }
}
