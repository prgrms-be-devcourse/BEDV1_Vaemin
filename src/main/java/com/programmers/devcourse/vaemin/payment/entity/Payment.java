package com.programmers.devcourse.vaemin.payment.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.programmers.devcourse.vaemin.order.entity.Order;
import com.programmers.devcourse.vaemin.root.AuditableEntity;
import com.programmers.devcourse.vaemin.shop.entity.Shop;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "payment")
@NoArgsConstructor
public class Payment extends AuditableEntity {
    @Column(name = "price", nullable = false)
    private int price;

    @OneToOne(mappedBy = "payment")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @Column(name = "status", nullable = false)
    private PaymentStatus paymentStatus;

    public void changePaymentStatus(@NonNull PaymentStatus paymentStatus) {
        if(paymentStatus == PaymentStatus.PAYED) this.paymentStatus = PaymentStatus.REFUND;
    }

    @Builder
    public Payment(
            int price,
            Order order,
            Customer customer) {
        this.price = price;
        this.order = order;
        this.customer = customer;
        if(price > customer.getPoint()) this.paymentStatus = PaymentStatus.NOT_PAYED;
        else this.paymentStatus = PaymentStatus.PAYED;
    }
}
