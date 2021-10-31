package com.programmers.devcourse.vaemin.user.customer.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerExceptionSuppliers {
    public static final Supplier<RuntimeException> customerNotFound = () -> {
        throw new IllegalArgumentException("Customer with given id not found.");
    };
}
