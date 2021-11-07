package com.programmers.devcourse.vaemin.order;

import com.programmers.devcourse.vaemin.coupon.entity.Coupon;
import com.programmers.devcourse.vaemin.coupon.entity.CouponDiscountType;
import com.programmers.devcourse.vaemin.coupon.repository.CouponRepository;
import com.programmers.devcourse.vaemin.food.entity.*;
import com.programmers.devcourse.vaemin.food.repository.*;
import com.programmers.devcourse.vaemin.order.controller.bind.FoodItemRequest;
import com.programmers.devcourse.vaemin.order.controller.bind.FoodSubItemRequest;
import com.programmers.devcourse.vaemin.order.controller.bind.OrderInformationRequest;
import com.programmers.devcourse.vaemin.order.entity.Order;
import com.programmers.devcourse.vaemin.order.entity.OrderStatus;
import com.programmers.devcourse.vaemin.order.entity.dto.CustomerOrderDTO;
import com.programmers.devcourse.vaemin.order.repository.OrderRepository;
import com.programmers.devcourse.vaemin.order.service.CustomerOrderService;
import com.programmers.devcourse.vaemin.payment.entity.Payment;
import com.programmers.devcourse.vaemin.payment.entity.PaymentStatus;
import com.programmers.devcourse.vaemin.payment.repository.PaymentRepository;
import com.programmers.devcourse.vaemin.shop.entity.Shop;
import com.programmers.devcourse.vaemin.shop.entity.ShopStatus;
import com.programmers.devcourse.vaemin.shop.entity.ShopSupportedOrderType;
import com.programmers.devcourse.vaemin.shop.entity.ShopSupportedPayment;
import com.programmers.devcourse.vaemin.shop.repository.ShopRepository;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import com.programmers.devcourse.vaemin.user.customer.repository.CustomerRepository;
import com.programmers.devcourse.vaemin.user.owner.entity.Owner;
import com.programmers.devcourse.vaemin.user.owner.repository.OwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class CustomerOrderServiceTest {
    @Autowired
    OwnerRepository ownerRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    ShopRepository shopRepository;
    @Autowired
    FoodRepository foodRepository;
    @Autowired
    FoodGroupRepository foodGroupRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    FoodSubRepository foodSubRepository;
    @Autowired
    FoodSubSelectGroupRepository foodSubSelectGroupRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    CouponRepository couponRepository;
    @Autowired
    CustomerOrderService customerOrderService;


    Food food;
    Group group;
    FoodSub foodSub;
    FoodSubSelectGroup foodSubSelectGroup;
    Owner owner;
    Customer customer;
    Shop shop;
    Payment payment;
    Coupon coupon;

    @BeforeEach
    void init() {
        customer = customerRepository.save(new Customer("USERNAME", "email@domain.com", "010-1234-5678"));
        owner = ownerRepository.save(new Owner("OWNER_USERNAME", "owner@domain.com", "123-1234-1234"));
        shop = shopRepository.save(Shop.builder()
                .name("SHOP_NAME")
                .shortDesc("SHOP_SHORT_DESCRIPTION")
                .longDesc("SHOP_LONG_DESCRIPTION")
                .deliveryFee(2500)
                .minOrderPrice(10000)
                .orderType(ShopSupportedOrderType.BOTH)
                .doroAddress("DORO_ADDRESS")
                .doroIndex(123)
                .detailAddress("DETAILED_ADDRESS")
                .owner(owner)
                .payment(ShopSupportedPayment.CASH)
                .phoneNum("010-1234-5678")
                .registerNumber("ABCD-1234-5678")
                .shopStatus(ShopStatus.NORMAL)
                .openTime(LocalTime.NOON)
                .closeTime(LocalTime.MIDNIGHT)
                .build());
        food = foodRepository.save(Food.builder()
                .name("FOOD_NAME")
                .shortDesc("SHORT_DESCRIPTION")
                .status(FoodStatus.NORMAL)
                .price(2500)
                .shop(shop)
                .discountType(DiscountType.NONE)
                .discountAmount(0)
                .build());
        // make food group, add to group, so as food-sub and init test.
        group = groupRepository.save(Group.builder()
                .name("GROUP_NAME")
                .shop(shop).build());
        group.addFood(food);
        foodSubSelectGroup = foodSubSelectGroupRepository.save(FoodSubSelectGroup.builder()
                .food(food)
                .required(true)
                .multiSelect(false)
                .groupName("SUB_SELECT_GROUP")
                .build());
        foodSub = foodSubRepository.save(FoodSub.builder()
                .name("FOOD_SUB_NAME")
                .food(food)
                .shop(shop)
                .price(500)
                .group(foodSubSelectGroup).build());
        payment = paymentRepository.save(Payment.builder()
                .paymentStatus(PaymentStatus.PAYED)
                .price(10000)
                .customer(customer).build());
        coupon = couponRepository.save(Coupon.builder()
                .name("COUPON")
                .shop(shop)
                .discountType(CouponDiscountType.FIXED)
                .discountAmount(1500)
                .minimumOrderPrice(25000)
                .expirationDate(LocalDateTime.of(2222, 2, 22, 2, 22)).build());
    }

    @Test
    @DisplayName("Create order / no coupon / no food sub.")
    void createCustomerOrder() {
        OrderInformationRequest request = new OrderInformationRequest();
        request.setShopId(shop.getId());
        request.setCustomerId(customer.getId());
        request.setAppliedCouponId(null);
        request.setPaymentId(payment.getId());

        FoodItemRequest foodItemRequest = new FoodItemRequest();
        foodItemRequest.setFoodItemId(food.getId());
        foodItemRequest.setCount(4);
        request.setFoodItems(List.of(foodItemRequest));
        CustomerOrderDTO createdOrder = customerOrderService.createOrder(request);

        Order order = orderRepository.findById(createdOrder.getId())
                .orElseThrow(() -> new IllegalArgumentException("Order with given id not found."));
        assertEquals(customer.getId(), order.getCustomer().getId());
        assertEquals(payment.getId(), order.getPayment().getId());
        assertEquals(payment.getOrder().getId(), order.getId());
        assertEquals(OrderStatus.CREATED, order.getOrderStatus());
        assertEquals(food.getPrice() * foodItemRequest.getCount(), order.getTotalPrice());
        assertEquals(shop.getId(), order.getShop().getId());
        assertEquals(1, order.getOrderFoodItems().size());
        assertEquals(food.getId(), order.getOrderFoodItems().get(0).getFood().getId());
        assertEquals(order.getId(), order.getOrderFoodItems().get(0).getOrder().getId());
        assertEquals(foodItemRequest.getCount(), order.getOrderFoodItems().get(0).getFoodCount());
        assertTrue(order.getOrderFoodItems().get(0).getFoodSubs().isEmpty());
        assertNull(order.getAppliedCoupon());
    }

    @Test
    @DisplayName("Create order / use coupon / no food sub.")
    void createCustomerOrderWithCoupon() {
        Coupon coupon = couponRepository.save(Coupon.builder()
                .minimumOrderPrice(10000)
                .discountType(CouponDiscountType.FIXED)
                .discountAmount(2500)
                .shop(shop)
                .expirationDate(LocalDateTime.now().plusHours(1))
                .name("COUPON_DISCOUNT_2500").build());
        Payment payment = paymentRepository.save(Payment.builder()
                .customer(customer)
                .price(7500)
                .paymentStatus(PaymentStatus.PAYED).build());

        OrderInformationRequest request = new OrderInformationRequest();
        request.setShopId(shop.getId());
        request.setCustomerId(customer.getId());
        request.setAppliedCouponId(coupon.getId());
        request.setPaymentId(payment.getId());

        FoodItemRequest foodItemRequest = new FoodItemRequest();
        foodItemRequest.setFoodItemId(food.getId());
        foodItemRequest.setCount(4);
        request.setFoodItems(List.of(foodItemRequest));
        CustomerOrderDTO createdOrder = customerOrderService.createOrder(request);

        Order order = orderRepository.findById(createdOrder.getId())
                .orElseThrow(() -> new IllegalArgumentException("Order with given id not found."));
        assertEquals(customer.getId(), order.getCustomer().getId());
        assertEquals(payment.getId(), order.getPayment().getId());
        assertEquals(food.getPrice() * foodItemRequest.getCount() - coupon.getDiscountAmount(), order.getPayment().getPrice());
        assertEquals(payment.getOrder().getId(), order.getId());
        assertEquals(OrderStatus.CREATED, order.getOrderStatus());
        assertEquals(food.getPrice() * foodItemRequest.getCount(), order.getTotalPrice());
        assertEquals(shop.getId(), order.getShop().getId());
        assertEquals(1, order.getOrderFoodItems().size());
        assertEquals(food.getId(), order.getOrderFoodItems().get(0).getFood().getId());
        assertEquals(order.getId(), order.getOrderFoodItems().get(0).getOrder().getId());
        assertEquals(foodItemRequest.getCount(), order.getOrderFoodItems().get(0).getFoodCount());
        assertTrue(order.getOrderFoodItems().get(0).getFoodSubs().isEmpty());
        assertEquals(coupon.getId(), order.getAppliedCoupon().getId());
    }

    @Test
    @DisplayName("Create order / use coupon / with food sub.")
    void createOrderWithCouponWithFoodSub() {
        Coupon coupon = couponRepository.save(Coupon.builder()
                .minimumOrderPrice(10000)
                .discountType(CouponDiscountType.FIXED)
                .discountAmount(2500)
                .shop(shop)
                .expirationDate(LocalDateTime.now().plusHours(1))
                .name("COUPON_DISCOUNT_2500").build());
        Payment payment = paymentRepository.save(Payment.builder()
                .customer(customer)
                .price(10000)
                .paymentStatus(PaymentStatus.PAYED).build());

        // four food = 2500 * 4
        // five food subs = 500 * 5
        // discount = 2500.
        OrderInformationRequest request = new OrderInformationRequest();
        request.setShopId(shop.getId());
        request.setCustomerId(customer.getId());
        request.setAppliedCouponId(coupon.getId());
        request.setPaymentId(payment.getId());

        FoodItemRequest foodItemRequest = new FoodItemRequest();
        foodItemRequest.setFoodItemId(food.getId());
        foodItemRequest.setCount(4);
        FoodSubItemRequest foodSubItemRequest = new FoodSubItemRequest();
        foodSubItemRequest.setFoodSubItemId(foodSub.getId());
        foodSubItemRequest.setCount(5);
        foodItemRequest.setFoodSubItemRequests(List.of(foodSubItemRequest));

        request.setFoodItems(List.of(foodItemRequest));
        CustomerOrderDTO createdOrder = customerOrderService.createOrder(request);

        Order order = orderRepository.findById(createdOrder.getId())
                .orElseThrow(() -> new IllegalArgumentException("Order with given id not found."));
        assertEquals(customer.getId(), order.getCustomer().getId());
        assertEquals(payment.getId(), order.getPayment().getId());
        assertEquals(food.getPrice() * foodItemRequest.getCount() +
                foodSub.getPrice() * foodSubItemRequest.getCount() -
                coupon.getDiscountAmount(), order.getPayment().getPrice());
        assertEquals(payment.getOrder().getId(), order.getId());
        assertEquals(OrderStatus.CREATED, order.getOrderStatus());
        assertEquals(food.getPrice() * foodItemRequest.getCount() +
                foodSub.getPrice() * foodSubItemRequest.getCount(), order.getTotalPrice());
        assertEquals(shop.getId(), order.getShop().getId());
        assertEquals(1, order.getOrderFoodItems().size());
        assertEquals(food.getId(), order.getOrderFoodItems().get(0).getFood().getId());
        assertEquals(order.getId(), order.getOrderFoodItems().get(0).getOrder().getId());
        assertEquals(foodItemRequest.getCount(), order.getOrderFoodItems().get(0).getFoodCount());
        assertEquals(foodSub.getId(), order.getOrderFoodItems().get(0).getFoodSubs().get(0).getFoodSub().getId());
        assertEquals(foodSubItemRequest.getCount(), order.getOrderFoodItems().get(0).getFoodSubs().get(0).getFoodSubCount());
        assertEquals(coupon.getId(), order.getAppliedCoupon().getId());
    }

    @Test
    @DisplayName("List all orders of customer.")
    void listOrders() {
        Order order1 = orderRepository.save(Order.builder()
                .appliedCoupon(null)
                .orderStatus(OrderStatus.CREATED)
                .totalPrice(10000)
                .shop(shop)
                .payment(payment)
                .customer(customer)
                .build());
        Order order2 = orderRepository.save(Order.builder()
                .appliedCoupon(coupon)
                .orderStatus(OrderStatus.ACCEPTED)
                .totalPrice(25000)
                .shop(shop)
                .payment(payment)
                .customer(customer)
                .build());
        customer.addOrder(order1);
        customer.addOrder(order2);

        List<CustomerOrderDTO> orders = customerOrderService.listCustomerOrders(customer.getId());
        assertEquals(2, orders.size());
        assertTrue(orders.stream()
                .anyMatch(dto -> dto.getAppliedCoupon().equals("") &&
                        dto.getOrderStatus().equals(OrderStatus.CREATED) &&
                        dto.getTotalPrice() == 10000 &&
                        dto.getShopName().equals(shop.getName()) &&
                        dto.getPayment().getPrice() == payment.getPrice()));
        assertTrue(orders.stream()
                .anyMatch(dto -> dto.getAppliedCoupon().equals(coupon.getName()) &&
                        dto.getOrderStatus().equals(OrderStatus.ACCEPTED) &&
                        dto.getTotalPrice() == 25000 &&
                        dto.getShopName().equals(shop.getName()) &&
                        dto.getPayment().getPrice() == payment.getPrice()));
    }

    @Test
    @DisplayName("Read order of customer.")
    void readOrder() {
        Order order = orderRepository.save(Order.builder()
                .appliedCoupon(null)
                .orderStatus(OrderStatus.CREATED)
                .totalPrice(10000)
                .shop(shop)
                .payment(payment)
                .customer(customer)
                .build());
        customer.addOrder(order);

        CustomerOrderDTO orderDTO = customerOrderService.readCustomerOrder(customer.getId(), order.getId());
        assertEquals("", orderDTO.getAppliedCoupon());
        assertEquals(OrderStatus.CREATED, orderDTO.getOrderStatus());
        assertEquals(10000, orderDTO.getTotalPrice());
        assertEquals(order.getId(), orderDTO.getId());
        assertEquals(payment.getId(), orderDTO.getPayment().getId());
        assertEquals(payment.getPrice(), orderDTO.getPaymentPrice());
        assertEquals(shop.getName(), orderDTO.getShopName());
    }

    @Test
    @DisplayName("Cancel customer order.")
    void cancelOrder() {
        Order order = orderRepository.save(Order.builder()
                .appliedCoupon(null)
                .orderStatus(OrderStatus.CREATED)
                .totalPrice(10000)
                .shop(shop)
                .payment(payment)
                .customer(customer)
                .build());
        customer.addOrder(order);

        customerOrderService.revokeCustomerOrder(customer.getId(), order.getId());
        CustomerOrderDTO revokedCustomerOrder = customerOrderService.readCustomerOrder(customer.getId(), order.getId());
        assertEquals(OrderStatus.CANCELLED, revokedCustomerOrder.getOrderStatus());
        assertEquals(PaymentStatus.REFUND, revokedCustomerOrder.getPayment().getPaymentStatus());
    }
}
