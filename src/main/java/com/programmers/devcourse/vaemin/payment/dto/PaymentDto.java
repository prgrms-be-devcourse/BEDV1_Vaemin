package com.programmers.devcourse.vaemin.payment.dto;

import com.programmers.devcourse.vaemin.order.entity.Order;
import com.programmers.devcourse.vaemin.payment.entity.Payment;
import com.programmers.devcourse.vaemin.payment.entity.PaymentStatus;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PaymentDto {

    @NotNull(message = "Price is mandatory")
    private int price;

    @NotNull(message = "Order is mandatory")
    private Order order;

    @NotNull(message = "Customer is mandatory")
    private Customer customer;

    @NotNull(message = "Payment Status is mandatory")
    private PaymentStatus paymentStatus;

    public PaymentDto(Payment payment) {
        this.price = payment.getPrice();
        this.order = payment.getOrder();
        this.customer = payment.getCustomer();
        this.paymentStatus = payment.getPaymentStatus();
    }
}
