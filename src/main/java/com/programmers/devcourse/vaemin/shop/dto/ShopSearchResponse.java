package com.programmers.devcourse.vaemin.shop.dto;

import com.programmers.devcourse.vaemin.shop.entity.Shop;
import com.programmers.devcourse.vaemin.shop.entity.ShopStatus;
import com.programmers.devcourse.vaemin.shop.entity.ShopSupportedOrderType;
import com.programmers.devcourse.vaemin.shop.entity.ShopSupportedPayment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ShopSearchResponse {
    private String shopName;
    private String phoneNum;
    private ShopSupportedOrderType supportedOrderType;
    private ShopSupportedPayment supportedPayment;
    private int deliveryFee;
    private int minOrderPrice;
    private ShopStatus shopStatus;

    public ShopSearchResponse(Shop shop) {
        this.shopName = shop.getName();
        this.phoneNum = shop.getPhoneNum();
        this.supportedOrderType = shop.getSupportedOrderType();
        this.supportedPayment = shop.getSupportedPayment();
        this.deliveryFee = shop.getDeliveryFee();
        this.minOrderPrice = shop.getMinOrderPrice();
        this.shopStatus = shop.getShopStatus();
    }
}
