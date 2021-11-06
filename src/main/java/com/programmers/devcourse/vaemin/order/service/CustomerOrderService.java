package com.programmers.devcourse.vaemin.order.service;

import com.programmers.devcourse.vaemin.coupon.entity.Coupon;
import com.programmers.devcourse.vaemin.coupon.exception.CouponExceptionSuppliers;
import com.programmers.devcourse.vaemin.coupon.repository.CouponRepository;
import com.programmers.devcourse.vaemin.food.entity.Food;
import com.programmers.devcourse.vaemin.food.entity.FoodSub;
import com.programmers.devcourse.vaemin.food.entity.FoodSubSelectGroup;
import com.programmers.devcourse.vaemin.food.repository.FoodRepository;
import com.programmers.devcourse.vaemin.food.repository.FoodSubRepository;
import com.programmers.devcourse.vaemin.food.service.FoodEntityExceptionSuppliers;
import com.programmers.devcourse.vaemin.order.controller.bind.OrderInformationRequest;
import com.programmers.devcourse.vaemin.order.entity.Order;
import com.programmers.devcourse.vaemin.order.entity.OrderFoodSub;
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

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerOrderService {
    private final CustomerRepository customerRepository;
    private final CouponRepository couponRepository;
    private final ShopRepository shopRepository;
    private final FoodRepository foodRepository;
    private final FoodSubRepository foodSubRepository;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;


    private int calculateTotalPrice(OrderInformationRequest request) {
        return request.getFoodItems().stream()
                .map(foodItemRequest -> {
                    int foodPrice = foodRepository.findById(foodItemRequest.getFoodItemId())
                            .map(food -> food.getPrice() * foodItemRequest.getCount())
                            .orElseThrow(FoodEntityExceptionSuppliers.foodNotFound);
                    int foodSubPrices = foodItemRequest.getFoodSubItemRequests().stream()
                            .map(foodSubItemRequest -> {
                                FoodSub foodSub = foodSubRepository.findById(foodSubItemRequest.getFoodSubItemId())
                                        .orElseThrow(FoodEntityExceptionSuppliers.foodSubNotFound);
                                return foodSub.getPrice() * foodSubItemRequest.getCount();
                            })
                            .reduce(0, Integer::sum);
                    return foodPrice + foodSubPrices;
                })
                .reduce(0, Integer::sum);
    }

    public long requestPayment(OrderInformationRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId()).orElseThrow(CustomerExceptionSuppliers.customerNotFound);
        int price = calculateTotalPrice(request);
        if(request.getAppliedCouponId() != null) {
            Coupon coupon = couponRepository.findById(request.getAppliedCouponId()).orElseThrow(CouponExceptionSuppliers.noCouponFound);
            validateCouponConstraint(coupon, price);
            price = Math.max(0, price - coupon.getDiscountAmount());
        }
        // TODO: 결제 서비스 연동
        return paymentRepository.save(Payment.builder()
                .paymentStatus(PaymentStatus.NOT_PAYED)
                .customer(customer)
                .price(price).build()).getId();
    }

    private void validateFoodOrderSubSelectGroupSelection(OrderInformationRequest request) {
        if (request.getFoodItems().isEmpty()) throw new IllegalArgumentException("Order cannot be empty.");
        request.getFoodItems().forEach(
                foodRequest -> {
                    Food food = foodRepository.findById(foodRequest.getFoodItemId())
                            .orElseThrow(FoodEntityExceptionSuppliers.foodNotFound);
                    Map<FoodSubSelectGroup, LinkedList<OrderFoodSub>> foodSubSelectGroupMap =
                            foodRequest.getFoodSubItemRequests().stream()
                                    .map(subRequest -> {
                                        FoodSub foodSub = foodSubRepository.findById(subRequest.getFoodSubItemId())
                                                .orElseThrow(FoodEntityExceptionSuppliers.foodSubNotFound);
                                        return OrderFoodSub.builder()
                                                .foodSub(foodSub)
                                                .foodSubCount(subRequest.getCount()).build();
                                    })
                                    .collect(Collectors.toMap(
                                            orderFoodSub -> orderFoodSub.getFoodSub().getSelectGroup(),
                                            orderFoodSub -> new LinkedList<>(List.of(orderFoodSub)),
                                            (o1, o2) -> {
                                                o1.addAll(o2);
                                                return o1;
                                            }));
                    food.getSubFoodGroups().forEach(subGroup -> {
                        LinkedList<OrderFoodSub> selectedFoodSubs = foodSubSelectGroupMap.get(subGroup);
                        if (subGroup.isRequired() && (selectedFoodSubs == null || selectedFoodSubs.isEmpty())) {
                            throw new IllegalArgumentException(
                                    String.format("Food sub of group %s should be included at least one.",
                                            subGroup.getGroupName()));
                        }

                        if (!subGroup.isMultiSelect() && selectedFoodSubs.size() > 1) {
                            throw new IllegalArgumentException(
                                    String.format("Food sub of group %s should be included only one.",
                                            subGroup.getGroupName()));
                        }
                    });
                });
    }

    private void validateCouponConstraint(Coupon coupon, int totalPrice) {
        if(coupon.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Coupon expiration date exceeded.");
        }

        if (totalPrice < coupon.getMinimumOrderPrice())
            throw new IllegalArgumentException(String.format(
                    "Order price %d should exceed coupon's required minimum order price %d.",
                    totalPrice, coupon.getMinimumOrderPrice()));
    }

    private void validateCouponConstraint(OrderInformationRequest request) {
        if (request.getAppliedCouponId() == null) return;
        Coupon coupon = couponRepository.findById(request.getAppliedCouponId())
                .orElseThrow(CouponExceptionSuppliers.noCouponFound);
        int totalPrice = calculateTotalPrice(request);
        validateCouponConstraint(coupon, totalPrice);
    }

    public CustomerOrderDTO createOrder(OrderInformationRequest request, long paymentId) {
        validateFoodOrderSubSelectGroupSelection(request);
        validateCouponConstraint(request);

        Payment orderPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found."));
        if (!orderPayment.getPaymentStatus().equals(PaymentStatus.PAYED))
            throw new IllegalArgumentException("Payment not accepted.");

        Customer customer = customerRepository.findById(request.getCustomerId()).orElseThrow(CustomerExceptionSuppliers.customerNotFound);
        Coupon coupon = request.getAppliedCouponId() == null ?
                null :
                couponRepository.findById(request.getAppliedCouponId()).orElseThrow(CouponExceptionSuppliers.noCouponFound);
        Shop shop = shopRepository.findById(request.getShopId()).orElseThrow(ShopExceptionSuppliers.shopNotFound);
        Order order = Order.builder()
                .customer(customer)
                .shop(shop)
                .payment(orderPayment)
                .orderStatus(OrderStatus.CREATED)
                .totalPrice(0)
                .appliedCoupon(coupon).build();

        int totalPrice = request.getFoodItems().stream().map(foodItemRequest -> {
            Food food = foodRepository.findById(foodItemRequest.getFoodItemId())
                    .orElseThrow(FoodEntityExceptionSuppliers.foodNotFound);
            order.addFood(food, foodItemRequest.getCount());
            int foodPrice = food.getPrice() * foodItemRequest.getCount();

            int subFoodPrice = foodItemRequest.getFoodSubItemRequests().stream().map(foodSubItemRequest -> {
                FoodSub foodSub = foodSubRepository.findById(foodSubItemRequest.getFoodSubItemId())
                        .orElseThrow(FoodEntityExceptionSuppliers.foodSubNotFound);
                order.addFoodSub(foodSub, foodSubItemRequest.getCount());
                return foodSub.getPrice() * foodSubItemRequest.getCount();
            }).reduce(0, Integer::sum);
            return foodPrice + subFoodPrice;
        }).reduce(0, Integer::sum);
        order.changeTotalPrice(totalPrice);

        orderRepository.save(order);
        return new CustomerOrderDTO(order);
    }

    public List<CustomerOrderDTO> listCustomerOrders(long customerId) {
        return customerRepository.findById(customerId).orElseThrow(CustomerExceptionSuppliers.customerNotFound)
                .getOrders().stream()
                .map(CustomerOrderDTO::new)
                .collect(Collectors.toList());
    }

    public CustomerOrderDTO readCustomerOrder(long customerId, long orderId) {
        return customerRepository.findById(customerId).orElseThrow(CustomerExceptionSuppliers.customerNotFound)
                .getOrders().stream()
                .filter(order -> order.getId() == orderId)
                .findAny()
                .map(CustomerOrderDTO::new)
                .orElseThrow(() -> new IllegalArgumentException("Order with given id not found."));
    }

    public CustomerOrderDTO revokeCustomerOrder(long customerId, long orderId) {
        Order order = customerRepository.findById(customerId).orElseThrow(CustomerExceptionSuppliers.customerNotFound)
                .getOrders().stream()
                .filter(o -> o.getId() == orderId)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Order with given id not found."));
        if (!order.getOrderStatus().equals(OrderStatus.CREATED)) {
            throw new IllegalArgumentException("Only created(and not handled) orders can be cancelled.");
        }
        order.changeOrderStatus(OrderStatus.CANCELLED);
        order.getPayment().changeStatus(PaymentStatus.REFUND);
        return new CustomerOrderDTO(order);
    }
}