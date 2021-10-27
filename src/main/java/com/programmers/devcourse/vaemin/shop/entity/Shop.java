package com.programmers.devcourse.vaemin.shop.entity;

import com.programmers.devcourse.vaemin.user.owner.entity.Owner;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Shop {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "phone_num", nullable = false, length = 20)
    private String phoneNum;

    @Column(name = "short_description", nullable = false, length = 100)
    private String shortDescription;

    @Column(name = "long_description", nullable = false, length = 1024)
    private String longDescription;

    @Column(name = "supported_order_method", nullable = false)
    private ShopSupportedOrderType supportedOrderType;

    @Column(name = "supported_payment", nullable = false)
    private ShopSupportedPayment supportedPayment;

    @Column(name = "open_time", nullable = false)
    private LocalDateTime openTime;

    @Column(name = "close_time", nullable = false)
    private LocalDateTime closeTime;

    @Column(name = "delivery_fee", nullable = false)
    private int deliveryFee;

    @Column(name = "min_order_price", nullable = false)
    private int minOrderPrice;

    @Column(name = "shop_status", nullable = false)
    private ShopStatus shopStatus;

    @Column(name = "register_number", nullable = false, length = 30)
    private String registerNumber;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Owner owner;

    @Column(name = "doro_address", nullable = false)
    private String doroAddress;

    @Column(name = "doro_index", nullable = false)
    private int doroIndex;

    @Column(name = "detail_address", nullable = false)
    private String detailAddress;
}
