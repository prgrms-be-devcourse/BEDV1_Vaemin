package com.programmers.devcourse.vaemin.review.service;

import com.programmers.devcourse.vaemin.order.entity.Order;
import com.programmers.devcourse.vaemin.order.entity.OrderStatus;
import com.programmers.devcourse.vaemin.order.repository.OrderRepository;
import com.programmers.devcourse.vaemin.payment.entity.Payment;
import com.programmers.devcourse.vaemin.payment.entity.PaymentStatus;
import com.programmers.devcourse.vaemin.payment.repository.PaymentRepository;
import com.programmers.devcourse.vaemin.review.controller.bind.ReviewInformationRequest;
import com.programmers.devcourse.vaemin.review.dto.ReviewDto;
import com.programmers.devcourse.vaemin.review.entity.Review;
import com.programmers.devcourse.vaemin.review.repository.ReviewRepository;
import com.programmers.devcourse.vaemin.root.exception.EntityExceptionSuppliers;
import com.programmers.devcourse.vaemin.shop.entity.*;
import com.programmers.devcourse.vaemin.shop.repository.ShopRepository;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import com.programmers.devcourse.vaemin.user.customer.entity.CustomerDeliveryAddress;
import com.programmers.devcourse.vaemin.user.customer.repository.CustomerRepository;
import com.programmers.devcourse.vaemin.user.owner.entity.Owner;
import com.programmers.devcourse.vaemin.user.owner.repository.OwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ReviewServiceTest {

    @Autowired
    ReviewService reviewService;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    ShopRepository shopRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OwnerRepository ownerRepository;
    @Autowired
    PaymentRepository paymentRepository;

    Review review;
    Order order;
    Customer customer;
    Shop shop;
    Owner owner;
    Payment payment;

    @BeforeEach
    void save_test() {
        // Given
        owner = ownerRepository.save(Owner.builder()
                .username("OWNER_USERNAME")
                .email("owner@domain.com")
                .phoneNum("123-1234-1234").build());
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
        customer = customerRepository.save(Customer.builder()
                .userName("USERNAME")
                .email("email@domain.com")
                .phoneNum("010-1234-5678")
                .currentAddress(new CustomerDeliveryAddress("LOC_CODE", "ADDR")).build());
        payment = paymentRepository.save(Payment.builder()
                .paymentStatus(PaymentStatus.PAYED)
                .price(10000)
                .customer(customer).build());
        order = orderRepository.save(Order.builder()
                .orderStatus(OrderStatus.CREATED)
                .totalPrice(15000)
                .customer(customer)
                .shop(shop)
                .appliedCoupon(null)
                .payment(payment).build());
        review = reviewRepository.save(Review.builder()
                .text("TASTE GOOD!")
                .starPoint(10)
                .shop(shop)
                .order(order)
                .customer(customer).build());
    }

    @Test
    void findReviewTest() {
        // When
        ReviewDto one = reviewService.findReview(review.getId());

        // Then
        assertThat(one.getOrder().getId()).isEqualTo(order.getId());
    }

    @Test
    void deleteReviewTest() {
        // When
        reviewService.deleteReview(review.getId());

        // Then
        assertFalse(reviewRepository.existsById(review.getId()));
    }

    @Test
    void updateReviewTest() {
        // Given
        ReviewInformationRequest request = new ReviewInformationRequest();
        request.setStarPoint(2);
        request.setText("taste bad :(");

        // When
        ReviewDto reviewDto = reviewService.updateReview(review.getId(), request);

        // Then
        Review updated = reviewRepository.findById(reviewDto.getId()).orElseThrow(EntityExceptionSuppliers.reviewNotFound);
        assertEquals("taste bad :(", updated.getText());
        assertEquals(2, updated.getStarPoint());
    }
}
