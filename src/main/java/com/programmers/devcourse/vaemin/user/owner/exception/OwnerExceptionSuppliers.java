package com.programmers.devcourse.vaemin.user.owner.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OwnerExceptionSuppliers {
    public static final Supplier<RuntimeException> ownerNotFound = () -> {
        throw new IllegalArgumentException("Owner with given id not found.");
    };
}
