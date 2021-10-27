package com.programmers.devcourse.vaemin.review.entity;

import com.programmers.devcourse.vaemin.order.entity.Order;
import com.programmers.devcourse.vaemin.root.AuditableEntity;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Review extends AuditableEntity {
    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @Column(name = "text", nullable = false, length = 1024)
    private String text;

    @Column(name = "star_point", nullable = false)
    private int starPoint;
}
