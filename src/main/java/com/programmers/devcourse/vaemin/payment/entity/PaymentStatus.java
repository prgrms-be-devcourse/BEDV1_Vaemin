package com.programmers.devcourse.vaemin.payment.entity;

public enum PaymentStatus {
    NOT_PAYED, // money not payed yet.
    REJECTED, // payment reject because of insufficient money.
    PAYED, // payment successful
    REFUND // payment successful but order cancelled(or rejected).
}
