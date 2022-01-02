package com.programmers.devcourse.vaemin.order.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.programmers.devcourse.vaemin.order.entity.Order;
import lombok.Getter;

@Getter
public class OwnerOrderDTO extends AbstractOrderDTO {
    // owner 가 보는 관점에서의 주문 기록.
    @JsonProperty("customer")
    private final String customer;

    public OwnerOrderDTO(Order order) {
        super(order);
        this.customer = order.getCustomer().getUsername();
    }
}
