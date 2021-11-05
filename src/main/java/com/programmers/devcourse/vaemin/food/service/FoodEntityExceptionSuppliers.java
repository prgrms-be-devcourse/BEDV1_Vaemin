package com.programmers.devcourse.vaemin.food.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FoodEntityExceptionSuppliers {
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
}
