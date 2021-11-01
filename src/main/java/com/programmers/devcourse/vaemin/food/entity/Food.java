package com.programmers.devcourse.vaemin.food.entity;

import com.programmers.devcourse.vaemin.root.AuditableEntity;
import com.programmers.devcourse.vaemin.shop.entity.Shop;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Food extends AuditableEntity {
    @Column(name = "food_name", nullable = false, length = 30)
    private String name;

    @Column(name = "short_description", nullable = false, length = 50)
    private String shortDescription;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "discount_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @Column(name = "discount_amount", nullable = false)
    private int discountAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", referencedColumnName = "id")
    private Shop shop;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private FoodStatus status;

    @OneToMany(mappedBy = "food")
    private final List<FoodGroup> joinedGroups = new ArrayList<>();

    @OneToMany(mappedBy = "food")
    private final List<FoodSub> subFoods = new ArrayList<>();


    public void changeName(@NonNull String name) {
        if(name.isBlank()) return;
        this.name = name;
    }

    public void changeDescription(@NonNull String shortDescription) {
        if(shortDescription.isBlank()) return;
        this.shortDescription = shortDescription;
    }

    public void changePrice(int price) {
        if(price < 0) return;
        this.price = price;
    }

    public void changeDiscountType(@NonNull DiscountType discountType) {
        this.discountType = discountType;
    }

    public void changeDiscountAmount(@NonNull int discountAmount) {
        this.discountAmount = discountAmount;
    }

    public void changeFoodStatus(@NonNull FoodStatus status) {
        this.status = status;
    }


    @Builder
    public Food(
            String name,
            String shortDesc,
            int price,
            DiscountType discountType,
            int discountAmount,
            Shop shop,
            FoodStatus status) {
        this.name = name;
        this.shortDescription = shortDesc;
        this.price = price;
        this.discountType = discountType;
        this.discountAmount = discountAmount;
        this.shop = shop;
        this.status = status;
    }
}
