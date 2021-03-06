package com.programmers.devcourse.vaemin.order.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.programmers.devcourse.vaemin.order.entity.Order;
import com.programmers.devcourse.vaemin.payment.entity.Payment;
import lombok.Getter;

@Getter
public class CustomerOrderDTO extends AbstractOrderDTO {
    // customer 가 보는 관점에서의 주문 기록.
    @JsonProperty("shop")
    private final String shopName;

    @JsonProperty("payment")
    private final Payment payment;

    public CustomerOrderDTO(Order order) {
        super(order);
        this.shopName = order.getShop().getName();
        // TODO: use DTO later.
        this.payment = order.getPayment();
    }
}
