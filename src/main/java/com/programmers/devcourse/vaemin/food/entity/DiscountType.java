package com.programmers.devcourse.vaemin.food.entity;

import java.util.function.BinaryOperator;

public enum DiscountType {
    FIXED((original, amount) -> Math.max(0, original - amount)),
    PERCENTAGE((original, amount) -> Math.max(0, (int)( original * (100 - amount) * 0.01))),
    NONE((original, amount) -> original);

    private final BinaryOperator<Integer> discount;

    public int discount(int original, int amount) {
        return discount.apply(original, amount);
    }

    DiscountType(BinaryOperator<Integer> discount) {
        this.discount = discount;
    }
}
