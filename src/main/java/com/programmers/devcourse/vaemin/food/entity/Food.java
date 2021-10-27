package com.programmers.devcourse.vaemin.food.entity;

import com.programmers.devcourse.vaemin.root.AuditableEntity;
import com.programmers.devcourse.vaemin.shop.entity.Shop;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Food extends AuditableEntity {
    @Column(name = "food_name", nullable = false, length = 30)
    private String name;

    @Column(name = "short_description", nullable = false, length = 50)
    private String shortDescription;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "discount_type", nullable = false)
    private DiscountType discountType;

    @Column(name = "discount_amount", nullable = false)
    private int amount;

    @ManyToOne
    @JoinColumn(name = "shop_id", referencedColumnName = "id")
    private Shop shop;

    @Column(name = "status", nullable = false)
    private FoodStatus status;
}
