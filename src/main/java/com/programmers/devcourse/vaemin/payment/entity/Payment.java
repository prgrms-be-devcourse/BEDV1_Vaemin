package com.programmers.devcourse.vaemin.payment.entity;

import com.programmers.devcourse.vaemin.root.AuditableEntity;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Payment extends AuditableEntity {
    @Column(name = "price", nullable = false)
    private int price;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @Column(name = "status", nullable = false)
    private PaymentStatus paymentStatus;
}
