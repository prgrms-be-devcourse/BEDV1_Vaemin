package com.programmers.devcourse.vaemin.order.service;

import com.programmers.devcourse.vaemin.coupon.entity.Coupon;
import com.programmers.devcourse.vaemin.coupon.repository.CouponRepository;
import com.programmers.devcourse.vaemin.food.entity.Food;
import com.programmers.devcourse.vaemin.food.entity.FoodSub;
import com.programmers.devcourse.vaemin.food.entity.FoodSubSelectGroup;
import com.programmers.devcourse.vaemin.food.repository.FoodRepository;
import com.programmers.devcourse.vaemin.food.repository.FoodSubRepository;
import com.programmers.devcourse.vaemin.order.controller.bind.OrderInformationRequest;
import com.programmers.devcourse.vaemin.order.entity.Order;
import com.programmers.devcourse.vaemin.order.entity.OrderStatus;
import com.programmers.devcourse.vaemin.order.entity.dto.CustomerOrderDTO;
import com.programmers.devcourse.vaemin.order.repository.OrderRepository;
import com.programmers.devcourse.vaemin.order.service.util.FoodItemsContainer;
import com.programmers.devcourse.vaemin.order.service.util.FoodSubItemsContainer;
import com.programmers.devcourse.vaemin.order.service.util.OrderItemsContainer;
import com.programmers.devcourse.vaemin.payment.entity.Payment;
import com.programmers.devcourse.vaemin.payment.entity.PaymentStatus;
import com.programmers.devcourse.vaemin.payment.repository.PaymentRepository;
import com.programmers.devcourse.vaemin.root.exception.EntityExceptionSuppliers;
import com.programmers.devcourse.vaemin.shop.entity.Shop;
import com.programmers.devcourse.vaemin.shop.repository.ShopRepository;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import com.programmers.devcourse.vaemin.user.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    private List<FoodItemsContainer> mapFoodItemsToContainer(OrderInformationRequest request) {
        if (request.getFoodItems().isEmpty()) throw new IllegalArgumentException("Order cannot be empty.");
        return request.getFoodItems().stream().map(
                foodRequest -> {
                    Food food = foodRepository.findById(foodRequest.getFoodItemId())
                            .orElseThrow(EntityExceptionSuppliers.foodNotFound);
                    FoodItemsContainer foodItemsContainer = new FoodItemsContainer(food, foodRequest.getCount());

                    Map<FoodSubSelectGroup, List<FoodSubItemsContainer>> foodSubSelectGroupMap =
                            foodRequest.getFoodSubItemRequests().stream()
                                    .map(subRequest -> {
                                        FoodSub foodSub = foodSubRepository.findById(subRequest.getFoodSubItemId())
                                                .orElseThrow(EntityExceptionSuppliers.foodSubNotFound);
                                        return new FoodSubItemsContainer(foodSub, subRequest.getCount());
                                    })
                                    .collect(Collectors.toMap(
                                            orderFoodSub -> orderFoodSub.getFoodSub().getSelectGroup(),
                                            orderFoodSub -> new LinkedList<>(List.of(orderFoodSub)),
                                            (o1, o2) -> {
                                                o1.addAll(o2);
                                                return o1;
                                            }));
                    foodItemsContainer.getFoodSubs().putAll(foodSubSelectGroupMap);
                    return foodItemsContainer;
                })
                .collect(Collectors.toList());
    }

    public CustomerOrderDTO createOrder(OrderInformationRequest request) {
        List<FoodItemsContainer> foodItemsContainers = mapFoodItemsToContainer(request);
        Payment orderPayment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new IllegalArgumentException("Payment not found."));
        Coupon coupon = request.getAppliedCouponId() == null ?
                null :
                couponRepository.findById(request.getAppliedCouponId())
                        .orElseThrow(EntityExceptionSuppliers.noCouponFound);
        Shop shop = shopRepository.findById(request.getShopId()).orElseThrow(EntityExceptionSuppliers.shopNotFound);
        OrderItemsContainer orderItemsContainer = new OrderItemsContainer(shop, foodItemsContainers, orderPayment, coupon);
        orderItemsContainer.validateOrder();

        Customer customer = customerRepository.findById(request.getCustomerId()).orElseThrow(EntityExceptionSuppliers.customerNotFound);
        Order order = Order.builder()
                .customer(customer)
                .shop(shop)
                .payment(orderPayment)
                .orderStatus(OrderStatus.CREATED)
                .totalPrice(orderItemsContainer.getTotalPrice())
                .appliedCoupon(coupon).build();

        orderItemsContainer.getFoodItemsContainer().forEach(
                fic -> {
                    Map<FoodSub, Integer> foodSubMap = fic.getFoodSubs().values().stream()
                            .reduce(new LinkedList<>(), (l1, l2) -> {
                                l1.addAll(l2);
                                return l1;
                            })
                            .stream().collect(Collectors.toMap(
                                    FoodSubItemsContainer::getFoodSub,
                                    FoodSubItemsContainer::getCount,
                                    Integer::sum));
                    order.addFoodItems(fic.getFood(), fic.getFoodCount(), foodSubMap);
                });
        orderPayment.registerOrder(order);
        orderRepository.save(order);
        return new CustomerOrderDTO(order);
    }

    public List<CustomerOrderDTO> listCustomerOrders(long customerId) {
        return customerRepository.findById(customerId).orElseThrow(EntityExceptionSuppliers.customerNotFound)
                .getOrders().stream()
                .map(CustomerOrderDTO::new)
                .collect(Collectors.toList());
    }

    public CustomerOrderDTO readCustomerOrder(long customerId, long orderId) {
        return customerRepository.findById(customerId).orElseThrow(EntityExceptionSuppliers.customerNotFound)
                .getOrders().stream()
                .filter(order -> order.getId() == orderId)
                .findAny()
                .map(CustomerOrderDTO::new)
                .orElseThrow(() -> new IllegalArgumentException("Order with given id not found."));
    }

    public CustomerOrderDTO revokeCustomerOrder(long customerId, long orderId) {
        Order order = customerRepository.findById(customerId).orElseThrow(EntityExceptionSuppliers.customerNotFound)
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