package com.programmers.devcourse.vaemin.order.dto;

import com.programmers.devcourse.vaemin.order.entity.Order;
import com.programmers.devcourse.vaemin.order.entity.OrderStatus;
import com.programmers.devcourse.vaemin.payment.entity.Payment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderDetailResponse {
    // 상점이름, 결제정보, 주문 상태, 주문 상품
    // TODO: 2021-11-04 주문 상품에 대한 정보도 추후 추가
    // TODO: 2021-11-04 Payment 자체가 아닌 paymentDto 만들어서 price와 paymentStatus만 가져오게 변경
    //  (현재는 test 코드에서 payment가 null값이라 보류)
    private LocalDateTime createdAt;
    private String shopName;
    private OrderStatus orderStatus;
    private Payment payment;

    // TODO: 2021-11-04 price가 order.totalPrice인지 payment.price인지 결정 후 생성자도 변경
    public OrderDetailResponse(Order order) {
        this.createdAt = order.getCreatedAt();
        this.shopName = order.getShop().getName();
        this.orderStatus = order.getOrderStatus();
        this.payment = order.getPayment();
    }
}
