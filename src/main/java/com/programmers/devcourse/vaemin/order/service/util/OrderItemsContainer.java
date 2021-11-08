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

    private void validatePayment() {
        if (!payment.getPaymentStatus().equals(PaymentStatus.PAYED)) {
            throw new IllegalArgumentException("Payment not accepted.");
        }

        if(payment.getOrder() != null) {
            throw new IllegalArgumentException("Already processed payment.");
        }

        int requiredPay = getTotalPrice() - (coupon == null ? 0 : coupon.getDiscountAmount());
        if (requiredPay != payment.getPrice()) {
            throw new IllegalArgumentException(String.format(
                    "Payment amount(%s) not matched with order price(%s).", payment.getPrice(), requiredPay));
        }
    }

    private void validateCouponUsable() {
        if (coupon == null) return;
        if(coupon.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Coupon is not available because it's expired.");
        }
        if(coupon.getMinimumOrderPrice() > getTotalPrice()) {
            throw new IllegalArgumentException("Coupon is expired because of minimum price limit.");
        }
    }

    private void validateMinimumTotalPriceLimit() {
        if(getTotalPrice() < shop.getMinOrderPrice()) {
            throw new IllegalArgumentException("Total price should be greater than shop's minimum order price.");
        }
    }

    public void validateOrder() {
        foodItemsContainer.forEach(FoodItemsContainer::validateFoodOrder);
        validatePayment();
        validateCouponUsable();
        validateMinimumTotalPriceLimit();
    }
}
