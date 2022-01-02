package com.programmers.devcourse.vaemin.order.entity;

public enum OrderStatus {
    CREATED, // order is created but not handled by shop.
    CANCELLED, // order is cancelled by customer before shop handles it.
    ACCEPTED, // order is accepted by shop.
    REJECTED // order is rejected by shop.
}
