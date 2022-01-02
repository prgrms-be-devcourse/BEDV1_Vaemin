package com.programmers.devcourse.vaemin.root.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntityExceptionSuppliers {
    public static final Supplier<RuntimeException> noCouponFound = () -> {
        throw new IllegalArgumentException("Coupon with given id not found.");
    };
    public static final Supplier<RuntimeException> groupNotFound = () -> {
        throw new IllegalArgumentException("Group with given id not found.");
    };
    public static final Supplier<RuntimeException> foodNotFound = () -> {
        throw new IllegalArgumentException("Food with given id not found.");
    };
    public static final Supplier<RuntimeException> foodSubNotFound = () -> {
        throw new IllegalArgumentException("Food-sub with given id not found.");
    };
    public static final Supplier<RuntimeException> foodSubSelectGroupNotFound = () -> {
        throw new IllegalArgumentException("Select-group of food-sub with given id not found.");
    };
    public static final Supplier<RuntimeException> paymentNotFound = () -> {
        throw new IllegalArgumentException("Payment with given id not found.");
    };
    public static final Supplier<RuntimeException> reviewNotFound = () -> {
        throw new IllegalArgumentException("Review with given id not found.");
    };
    public static final Supplier<RuntimeException> shopNotFound = () -> {
        throw new IllegalArgumentException("Shop with given id not found.");
    };
    public static final Supplier<RuntimeException> categoryNotFound = () -> {
        throw new IllegalArgumentException("Category with given id not found.");
    };
    public static final Supplier<RuntimeException> shopCategoryNotFound = () -> {
        throw new IllegalArgumentException("Shop-Category with given id not found.");
    };
    public static final Supplier<RuntimeException> deliverySupportedRegionsNotFound = () -> {
        throw new IllegalArgumentException("Delivery Supported Regions with given id not found.");
    };
    public static final Supplier<RuntimeException> customerAddressNotFound = () -> {
        throw new IllegalArgumentException("Customer Address not found.");
    };
    public static final Supplier<RuntimeException> customerNotFound = () -> {
        throw new IllegalArgumentException("Customer with given id not found.");
    };
    public static final Supplier<RuntimeException> ownerNotFound = () -> {
        throw new IllegalArgumentException("Owner with given id not found.");
    };
    public static final Supplier<RuntimeException> orderNotFound = () -> {
        throw new IllegalArgumentException("Order with given id not found.");
    };
}
