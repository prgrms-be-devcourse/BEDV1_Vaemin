package com.programmers.devcourse.vaemin.order.service.util;

import com.programmers.devcourse.vaemin.coupon.entity.Coupon;
import com.programmers.devcourse.vaemin.payment.entity.Payment;
import com.programmers.devcourse.vaemin.payment.entity.PaymentStatus;
import com.programmers.devcourse.vaemin.shop.entity.Shop;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class OrderItemsContainer {
    private final Shop shop;
    private final List<FoodItemsContainer> foodItemsContainer;
    private final Payment payment;
    private final Coupon coupon;

    public int getTotalPrice() {
        return foodItemsContainer.stream()
                .map(FoodItemsContainer::getTotalPrice)
                .reduce(0, Integer::sum);
    }

    private void validatePaymentStatus() {
        if (!payment.getPaymentStatus().equals(PaymentStatus.PAYED)) {
            throw new IllegalArgumentException("Payment not accepted.");
        }
    }

    private void validateCouponUsable() {
        if (coupon == null) return;
        if(coupon.getExpirationDate().isBefore(LocalDateTime.now()) ||
                coupon.getMinimumOrderPrice() >= getTotalPrice()) {
            throw new IllegalArgumentException("Coupon is expired or not usable because of minimum price limit.");
        }
    }

    private void validateMinimumTotalPriceLimit() {
        if(getTotalPrice() < shop.getMinOrderPrice()) {
            throw new IllegalArgumentException("Total price should be greater than shop's minimum order price.");
        }
    }

    public void validateOrder() {
        foodItemsContainer.forEach(FoodItemsContainer::validateFoodOrder);
        validatePaymentStatus();
        validateCouponUsable();
        validateMinimumTotalPriceLimit();
    }
}
