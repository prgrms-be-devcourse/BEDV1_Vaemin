package com.programmers.devcourse.vaemin.user.customer.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerAddressExceptionSuppliers {
    public static final Supplier<RuntimeException> customerAddressNotFound = () -> {
        throw new IllegalArgumentException("Customer Address not found.");
    };
}
