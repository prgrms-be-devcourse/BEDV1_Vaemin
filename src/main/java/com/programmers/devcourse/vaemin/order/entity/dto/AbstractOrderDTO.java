package com.programmers.devcourse.vaemin.order.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.programmers.devcourse.vaemin.order.entity.Order;
import com.programmers.devcourse.vaemin.order.entity.OrderStatus;
import lombok.Getter;


@Getter
public abstract class AbstractOrderDTO {
    @JsonProperty("id")
    protected final long id;

    @JsonProperty("totalPrice")
    protected final int totalPrice;

    @JsonProperty("paymentPrice")
    protected final int paymentPrice;

    @JsonProperty("status")
    protected final OrderStatus orderStatus;

    @JsonProperty("appliedCoupon")
    protected final String appliedCoupon;

    protected AbstractOrderDTO(Order order) {
        this.id = order.getId();
        this.totalPrice = order.getTotalPrice();
        this.paymentPrice = order.getPayment().getPrice();
        this.orderStatus = order.getOrderStatus();
        this.appliedCoupon = order.getAppliedCoupon() == null ? "" : order.getAppliedCoupon().getName();
    }
}
