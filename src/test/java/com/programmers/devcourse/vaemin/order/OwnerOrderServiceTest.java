package com.programmers.devcourse.vaemin.order;

import com.programmers.devcourse.vaemin.coupon.entity.Coupon;
import com.programmers.devcourse.vaemin.coupon.entity.CouponDiscountType;
import com.programmers.devcourse.vaemin.coupon.repository.CouponRepository;
import com.programmers.devcourse.vaemin.food.entity.*;
import com.programmers.devcourse.vaemin.food.repository.*;
import com.programmers.devcourse.vaemin.order.controller.bind.OrderStatusRequest;
import com.programmers.devcourse.vaemin.order.entity.Order;
import com.programmers.devcourse.vaemin.order.entity.OrderStatus;
import com.programmers.devcourse.vaemin.order.entity.dto.OwnerOrderDTO;
import com.programmers.devcourse.vaemin.order.repository.OrderRepository;
import com.programmers.devcourse.vaemin.order.service.ShopOrderService;
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
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class OwnerOrderServiceTest {
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
    ShopOrderService shopOrderService;

    Food food;
    Group group;
    FoodSub foodSub;
    FoodSubSelectGroup foodSubSelectGroup;
    Owner owner;
    Customer customer;
    Shop shop;
    Payment payment;
    Coupon coupon;
    Order order;

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
        owner.addShop(shop);
        food = foodRepository.save(Food.builder()
                .name("FOOD_NAME")
                .shortDesc("SHORT_DESCRIPTION")
                .status(FoodStatus.NORMAL)
                .price(2500)
                .shop(shop)
                .discountType(DiscountType.NONE)
                .discountAmount(0)
                .build());
        shop.getFoods().add(food);
        group = groupRepository.save(Group.builder()
                .name("GROUP_NAME")
                .shop(shop).build());
        shop.getGroups().add(group);
        group.addFood(food);
        foodSubSelectGroup = foodSubSelectGroupRepository.save(FoodSubSelectGroup.builder()
                .food(food)
                .required(true)
                .multiSelect(false)
                .groupName("SUB_SELECT_GROUP")
                .build());
        food.getSubFoodGroups().add(foodSubSelectGroup);
        foodSub = foodSubRepository.save(FoodSub.builder()
                .name("FOOD_SUB_NAME")
                .food(food)
                .shop(shop)
                .price(500)
                .group(foodSubSelectGroup).build());
        food.getSubFoods().add(foodSub);
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
        shop.getCoupons().add(coupon);
        order = orderRepository.save(Order.builder()
                .orderStatus(OrderStatus.CREATED)
                .totalPrice(15000)
                .customer(customer)
                .shop(shop)
                .appliedCoupon(null)
                .payment(payment).build());
        order.addFoodItems(food, 3, new HashMap<>());
        customer.getOrders().add(order);
        shop.getOrders().add(order);
        payment.registerOrder(order);
    }


    @Test
    @DisplayName("List shop orders.")
    void listShopOrders() {
        List<OwnerOrderDTO> ownerOrderDTOS = shopOrderService.listShopOrders(owner.getId(), shop.getId());
        assertEquals(1, ownerOrderDTOS.size());
        OwnerOrderDTO order = ownerOrderDTOS.get(0);
        assertEquals(OrderStatus.CREATED, order.getOrderStatus());
        assertEquals(15000, order.getTotalPrice());
        assertEquals(payment.getPrice(), order.getPaymentPrice());
        assertEquals(customer.getUsername(), order.getCustomer());
        assertEquals(OrderStatus.CREATED, order.getOrderStatus());
    }

    @Test
    @DisplayName("Read shop order.")
    void readShopOrder() {
        OwnerOrderDTO orderDTO = shopOrderService.readShopOrder(owner.getId(), shop.getId(), order.getId());
        assertEquals(order.getOrderStatus(), orderDTO.getOrderStatus());
        assertEquals(order.getCustomer().getUsername(), orderDTO.getCustomer());
        assertEquals(order.getTotalPrice(), orderDTO.getTotalPrice());
        assertEquals(order.getPayment().getPrice(), orderDTO.getPaymentPrice());
        assertEquals("", orderDTO.getAppliedCoupon());
        assertEquals(order.getOrderFoodItems().size(), orderDTO.getFoods().size());
    }

    @Test
    @DisplayName("List waiting orders.")
    void listWaitingOrders() {
        order = orderRepository.save(Order.builder()
                .orderStatus(OrderStatus.ACCEPTED)
                .totalPrice(12345)
                .customer(customer)
                .shop(shop)
                .appliedCoupon(null)
                .payment(payment).build());
        Payment payment = paymentRepository.save(Payment.builder()
                .paymentStatus(PaymentStatus.NOT_PAYED)
                .customer(customer)
                .price(12345).build());
        order.addFoodItems(food, 5, new HashMap<>());
        customer.getOrders().add(order);
        shop.getOrders().add(order);
        payment.registerOrder(order);
        List<OwnerOrderDTO> ownerOrderDTOS = shopOrderService.listWaitingOrders(owner.getId(), shop.getId());
        assertEquals(1, ownerOrderDTOS.size());
        OwnerOrderDTO ownerOrderDTO = ownerOrderDTOS.get(0);
        assertEquals(15000, ownerOrderDTO.getTotalPrice());
    }

    @Test
    @DisplayName("Accept order.")
    void acceptOrder() {
        OrderStatusRequest request = new OrderStatusRequest();
        request.setAccept(true);
        shopOrderService.receiveOrder(owner.getId(), shop.getId(), order.getId(), request);

        OrderStatus orderStatus = orderRepository.findById(order.getId()).orElseThrow(IllegalArgumentException::new).getOrderStatus();
        assertEquals(OrderStatus.ACCEPTED, orderStatus);
    }

    @Test
    @DisplayName("Refuse order.")
    void refuseOrder() {
        OrderStatusRequest request = new OrderStatusRequest();
        request.setAccept(false);
        shopOrderService.receiveOrder(owner.getId(), shop.getId(), order.getId(), request);

        OrderStatus orderStatus = orderRepository.findById(order.getId()).orElseThrow(IllegalArgumentException::new).getOrderStatus();
        assertEquals(OrderStatus.REJECTED, orderStatus);
    }
}
