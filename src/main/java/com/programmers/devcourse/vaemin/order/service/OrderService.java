package com.programmers.devcourse.vaemin.order.service;

import com.programmers.devcourse.vaemin.coupon.entity.Coupon;
import com.programmers.devcourse.vaemin.coupon.exception.CouponExceptionSuppliers;
import com.programmers.devcourse.vaemin.coupon.repository.CouponRepository;
import com.programmers.devcourse.vaemin.food.entity.Food;
import com.programmers.devcourse.vaemin.food.entity.FoodSub;
import com.programmers.devcourse.vaemin.food.repository.FoodRepository;
import com.programmers.devcourse.vaemin.food.repository.FoodSubRepository;
import com.programmers.devcourse.vaemin.food.service.FoodEntityExceptionSuppliers;
import com.programmers.devcourse.vaemin.order.controller.bind.OrderInformationRequest;
import com.programmers.devcourse.vaemin.order.entity.Order;
import com.programmers.devcourse.vaemin.order.entity.OrderStatus;
import com.programmers.devcourse.vaemin.order.entity.dto.CustomerOrderDTO;
import com.programmers.devcourse.vaemin.order.repository.OrderRepository;
import com.programmers.devcourse.vaemin.payment.entity.Payment;
import com.programmers.devcourse.vaemin.payment.entity.PaymentStatus;
import com.programmers.devcourse.vaemin.payment.repository.PaymentRepository;
import com.programmers.devcourse.vaemin.shop.entity.Shop;
import com.programmers.devcourse.vaemin.shop.exception.ShopExceptionSuppliers;
import com.programmers.devcourse.vaemin.shop.repository.ShopRepository;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import com.programmers.devcourse.vaemin.user.customer.exception.CustomerExceptionSuppliers;
import com.programmers.devcourse.vaemin.user.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final CustomerRepository customerRepository;
    private final CouponRepository couponRepository;
    private final ShopRepository shopRepository;
    private final FoodRepository foodRepository;
    private final FoodSubRepository foodSubRepository;
    private final PaymentRepository paymentRepository;

    // 결제 서비스 연동.
    public long requestPayment(OrderInformationRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId()).orElseThrow(CustomerExceptionSuppliers.customerNotFound);
        int price = 0;
        price += request.getFoodItems().stream()
                .map(foodItemRequest -> {
                    Food food = foodRepository.findById(foodItemRequest.getFoodItemId())
                            .orElseThrow(FoodEntityExceptionSuppliers.foodNotFound);
                    return food.getPrice();
                })
                .reduce(0, Integer::sum);

        price += request.getFoodSubItems().stream()
                .map(foodSubItemRequest -> {
                    FoodSub foodSub = foodSubRepository.findById(foodSubItemRequest.getFoodSubItemId())
                            .orElseThrow(FoodEntityExceptionSuppliers.foodSubNotFound);
                    return foodSub.getPrice();
                })
                .reduce(0, Integer::sum);

        return paymentRepository.save(Payment.builder()
                .paymentStatus(PaymentStatus.NOT_PAYED)
                .customer(customer)
                .price(price).build()).getId();
    }

    public CustomerOrderDTO createOrder(OrderInformationRequest request, long paymentId) {
        Payment orderPayment = paymentRepository.findById(paymentId).orElseThrow(() -> new IllegalArgumentException("Payment not found."));
        if (!orderPayment.getPaymentStatus().equals(PaymentStatus.PAYED))
            throw new IllegalArgumentException("Payment not accepted.");

        Customer customer = customerRepository.findById(request.getCustomerId()).orElseThrow(CustomerExceptionSuppliers.customerNotFound);
        Coupon coupon = couponRepository.findById(request.getAppliedCouponId()).orElseThrow(CouponExceptionSuppliers.noCouponFound);
        Shop shop = shopRepository.findById(request.getShopId()).orElseThrow(ShopExceptionSuppliers.shopNotFound);
        Order order = Order.builder()
                .customer(customer)
                .shop(shop)
                .payment(orderPayment)
                .orderStatus(OrderStatus.CREATED)
                .totalPrice(0)
                .appliedCoupon(coupon).build();

        request.getFoodItems().forEach(foodItemRequest -> {
            Food food = foodRepository.findById(foodItemRequest.getFoodItemId())
                    .orElseThrow(FoodEntityExceptionSuppliers.foodNotFound);
            order.addFood(food, foodItemRequest.getCount());
        });
        request.getFoodSubItems().forEach(foodSubItemRequest -> {
            FoodSub foodSub = foodSubRepository.findById(foodSubItemRequest.getFoodSubItemId())
                    .orElseThrow(FoodEntityExceptionSuppliers.foodSubNotFound);
            order.addFoodSub(foodSub, foodSubItemRequest.getCount());
        });

        return new CustomerOrderDTO(order);
    }
}