package com.programmers.devcourse.vaemin.shop.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShopExceptionSuppliers {

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
}
