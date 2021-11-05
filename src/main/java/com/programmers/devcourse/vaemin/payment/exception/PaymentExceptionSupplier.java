package com.programmers.devcourse.vaemin.payment.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentExceptionSupplier {

    public static final Supplier<RuntimeException> paymentNotFound = () -> {
        throw new IllegalArgumentException("Payment with given id not found.");
    };
}
