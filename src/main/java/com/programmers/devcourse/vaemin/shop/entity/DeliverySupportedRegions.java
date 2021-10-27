package com.programmers.devcourse.vaemin.shop.entity;

import com.programmers.devcourse.vaemin.root.IdentifiableEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class DeliverySupportedRegions extends IdentifiableEntity {
    @Column(name = "location_code", nullable = false)
    private String locationCode;

    @ManyToOne
    @JoinColumn(name = "shop_id", referencedColumnName = "id")
    private Shop shop;
}
