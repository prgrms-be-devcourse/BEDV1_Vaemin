package com.programmers.devcourse.vaemin.review.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewExceptionSuppliers {

    public static final Supplier<RuntimeException> reviewNotFound = () -> {
        throw new IllegalArgumentException("Review with given id not found.");
    };
    
}