package com.programmers.devcourse.vaemin.order.dto;

import com.programmers.devcourse.vaemin.order.entity.Order;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderResponse {
    // 상점이름, 주문금액, 주문날짜
    private String shopName;
    private int totalPrice;
    private LocalDateTime createdAt;

    public OrderResponse(Order order) {
        this.shopName = order.getShop().getName();
        this.totalPrice = order.getTotalPrice();
        this.createdAt = order.getCreatedAt();
    }
}
