package com.programmers.devcourse.vaemin.user.customer.entity;

import com.programmers.devcourse.vaemin.root.IdentifiableEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class CustomerDeliveryAddress extends IdentifiableEntity {
    @Column(name = "location_code", nullable = false, length = 20)
    private String locationCode;

    @Column(name = "address_detail", nullable = false, length = 50)
    private String addressDetail;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;
}
