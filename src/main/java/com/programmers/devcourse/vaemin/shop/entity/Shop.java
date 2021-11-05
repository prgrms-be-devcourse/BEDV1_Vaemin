package com.programmers.devcourse.vaemin.shop.entity;

import com.programmers.devcourse.vaemin.coupon.entity.Coupon;
import com.programmers.devcourse.vaemin.food.entity.Food;
import com.programmers.devcourse.vaemin.order.entity.Order;
import com.programmers.devcourse.vaemin.food.entity.Group;
import com.programmers.devcourse.vaemin.review.entity.Review;
import com.programmers.devcourse.vaemin.root.AuditableEntity;
import com.programmers.devcourse.vaemin.user.owner.entity.Owner;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "shop")
@NoArgsConstructor
public class Shop extends AuditableEntity {
    @Column(name = "shop_name", nullable = false, length = 50)
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
    private LocalTime openTime;

    @Column(name = "close_time", nullable = false)
    private LocalTime closeTime;

    @Column(name = "delivery_fee", nullable = false)
    private int deliveryFee;

    @Column(name = "min_order_price", nullable = false)
    private int minOrderPrice;

    @Column(name = "shop_status", nullable = false)
    private ShopStatus shopStatus;

    @Column(name = "register_number", nullable = false, unique = true, length = 30)
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

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private final List<DeliverySupportedRegions> regions = new ArrayList<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private final List<Food> foods = new ArrayList<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.PERSIST)
    private final List<Group> groups = new ArrayList<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private final List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private final List<Coupon> coupons = new ArrayList<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private final List<ShopCategory> shopCategories = new ArrayList<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private final List<Review> reviews = new ArrayList<>();

    public void changeName(@NonNull String name) {
        if (name.isBlank()) return;
        this.name = name;
    }

    public void changePhoneNum(@NonNull String phoneNum) {
        if (phoneNum.isBlank()) return;
        this.phoneNum = phoneNum;
    }

    public void changeShortDescription(@NonNull String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void changeLongDescription(@NonNull String longDescription) {
        this.longDescription = longDescription;
    }

    public void changeSupportedOrderType(@NonNull ShopSupportedOrderType supportedOrderType) {
        this.supportedOrderType = supportedOrderType;
    }

    public void changeSupportedPayment(@NonNull ShopSupportedPayment supportedPayment) {
        this.supportedPayment = supportedPayment;
    }

    public void changeOpenTime(@NonNull LocalTime openTime) {
        this.openTime = openTime;
    }

    public void changeCloseTime(@NonNull LocalTime closeTime) {
        this.closeTime = closeTime;
    }

    public void changeDeliveryFee(int deliveryFee) {
        if (deliveryFee < 0) return;
        this.deliveryFee = deliveryFee;
    }

    public void changeMinOrderPrice(int minOrderPrice) {
        if (minOrderPrice < 0) return;
        this.minOrderPrice = minOrderPrice;
    }

    public void changeShopStatus(@NonNull ShopStatus shopStatus) {
        this.shopStatus = shopStatus;
    }

    public void changeRegisterNumber(@NonNull String registerNumber) {
        if (registerNumber.isBlank()) return;
        this.registerNumber = registerNumber;
    }

    public void changeAddress(@NonNull String doroAddress, int doroIndex, String detailAddress) {
        this.doroAddress = doroAddress;
        this.doroIndex = doroIndex;
        this.detailAddress = detailAddress;
    }

    // TODO: decouple fields?
    @Builder
    public Shop(
            String name,
            String phoneNum,
            String shortDesc,
            String longDesc,
            ShopSupportedOrderType orderType,
            ShopSupportedPayment payment,
            LocalTime openTime,
            LocalTime closeTime,
            int deliveryFee,
            int minOrderPrice,
            ShopStatus shopStatus,
            String registerNumber,
            Owner owner,
            String doroAddress,
            int doroIndex,
            String detailAddress) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.shortDescription = shortDesc;
        this.longDescription = longDesc;
        this.supportedOrderType = orderType;
        this.supportedPayment = payment;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.deliveryFee = deliveryFee;
        this.minOrderPrice = minOrderPrice;
        this.shopStatus = shopStatus;
        this.registerNumber = registerNumber;
        this.owner = owner;
        this.doroAddress = doroAddress;
        this.doroIndex = doroIndex;
        this.detailAddress = detailAddress;
    }
}
