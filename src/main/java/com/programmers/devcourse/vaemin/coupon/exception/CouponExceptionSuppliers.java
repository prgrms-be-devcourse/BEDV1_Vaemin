package com.programmers.devcourse.vaemin.coupon.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponExceptionSuppliers {
    public static final Supplier<RuntimeException> noCouponFound = () -> {
        throw new IllegalArgumentException("Coupon with given id not found.");
    };
}
