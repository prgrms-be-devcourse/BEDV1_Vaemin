package com.programmers.devcourse.vaemin.coupon.entity;

import com.programmers.devcourse.vaemin.root.IdentifiableEntity;
import com.programmers.devcourse.vaemin.shop.entity.Shop;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Coupon extends IdentifiableEntity {
    @Column(name = "coupon_name", nullable = false, length = 30)
    private String name;

    @Column(name = "discount_type", nullable = false)
    private CouponDiscountType discountType;

    @Column(name = "discount_amount", nullable = false)
    private int discountAmount;

    @Column(name = "minimum_order_price", nullable = false)
    private int minimumOrderPrice;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @ManyToOne
    @JoinColumn(name = "shop_id", referencedColumnName = "id")
    private Shop shop;


    public void changeName(@NonNull String name) {
        if(name.isBlank()) throw new IllegalArgumentException("Updated coupon name cannot be blank.");
        this.name = name;
    }

    public void changeDiscountType(@NonNull CouponDiscountType type) {
        this.discountType = type;
    }

    public void changeDiscountAmount(int discountAmount) {
        if(discountAmount < 0) throw new IllegalArgumentException("Coupon discount amount cannot be negative.");
        this.discountAmount = discountAmount;
    }

    public void changeMinimumOrderPrice(int minimumOrderPrice) {
        if(minimumOrderPrice < 0) throw new IllegalArgumentException("Minimum order price cannot be negative.");
        this.minimumOrderPrice = minimumOrderPrice;
    }

    public void changeExpirationDate(@NonNull LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void changeShop(@NonNull Shop shop) {
        shop.getCoupons().remove(this);
        this.shop = shop;
        shop.getCoupons().add(this);
    }


    @Builder
    public Coupon(
            String name,
            CouponDiscountType discountType,
            int discountAmount,
            int minimumOrderPrice,
            LocalDateTime expirationDate,
            Shop shop) {
        this.name = name;
        this.discountType = discountType;
        this.discountAmount = discountAmount;
        this.minimumOrderPrice = minimumOrderPrice;
        this.expirationDate = expirationDate;
        this.shop = shop;
    }
}
