package com.programmers.devcourse.vaemin.payment.entity;

import com.programmers.devcourse.vaemin.order.entity.Order;
import com.programmers.devcourse.vaemin.root.AuditableEntity;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Payment extends AuditableEntity {
    @Column(name = "price", nullable = false)
    private int price;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @Column(name = "status", nullable = false)
    private PaymentStatus paymentStatus;

    @OneToOne(mappedBy = "payment")
    private Order order;


    public void changeStatus(@NonNull PaymentStatus status) {
        this.paymentStatus = status;
    }

    public void registerOrder(@NonNull Order order) {
        if (this.order != null) throw new IllegalArgumentException("Payment already registered order.");
        this.order = order;
    }

    @Builder
    public Payment(int price, Customer customer, PaymentStatus paymentStatus) {
        this.price = price;
        this.customer = customer;
        this.paymentStatus = paymentStatus;
    }
}
