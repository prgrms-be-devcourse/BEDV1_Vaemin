package com.programmers.devcourse.vaemin.delivery.entity;

import com.programmers.devcourse.vaemin.order.entity.Order;
import com.programmers.devcourse.vaemin.root.AuditableEntity;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@Getter
public class Delivery extends AuditableEntity {
    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @Column(name = "delivery_status", nullable = false)
    private DeliveryStatus deliveryStatus;
}
