package com.programmers.devcourse.vaemin.order.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderExceptionSupplier {
    public static final Supplier<RuntimeException> orderNotFound = () -> {
        throw new IllegalArgumentException("Order with given id not found.");
    };
}