package com.programmers.devcourse.vaemin.review.service;

import com.programmers.devcourse.vaemin.order.entity.Order;
import com.programmers.devcourse.vaemin.order.entity.OrderStatus;
import com.programmers.devcourse.vaemin.order.repository.OrderRepository;
import com.programmers.devcourse.vaemin.payment.entity.Payment;
import com.programmers.devcourse.vaemin.payment.entity.PaymentStatus;
import com.programmers.devcourse.vaemin.payment.repository.PaymentRepository;
import com.programmers.devcourse.vaemin.review.dto.ReviewDto;
import com.programmers.devcourse.vaemin.review.entity.Review;
import com.programmers.devcourse.vaemin.review.repository.ReviewRepository;
import com.programmers.devcourse.vaemin.root.exception.EntityExceptionSuppliers;
import com.programmers.devcourse.vaemin.shop.entity.*;
import com.programmers.devcourse.vaemin.shop.repository.ShopRepository;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
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
    Long reviewId;
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
                .locationCode("LOCATION_CODE")
                .addressDetail("ADDR_DETAIL").build());
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

        review = Review.builder()
                .order(order)
                .customer(customer)
                .shop(shop)
                .text("taste good!! :)")
                .starPoint(10)
                .build();
        ReviewDto reviewDto = new ReviewDto(review);

        // When
        reviewId = reviewService.createReview(reviewDto);

        // Then
        assertThat(reviewRepository.count()).isEqualTo(1);
    }

    @Test
    void findReviewTest() {
        // When
        ReviewDto one = reviewService.findReview(reviewId);

        // Then
        assertThat(one.getOrder()).isEqualTo(order);
    }

    @Test
    void deleteReviewTest() {
        // When
        reviewService.deleteReview(reviewId);

        // Then
        assertFalse(reviewRepository.existsById(reviewId));
    }

    @Test
    void updateReviewTest() {
        // Given
        ReviewDto reviewDto = reviewService.findReview(reviewId);
        reviewDto.setText("taste bad :(");
        reviewDto.setStarPoint(2);

        // When
        Long updatedReviewId = reviewService.updateReview(reviewId, reviewDto);

        // Then
        Review updated = reviewRepository.findById(updatedReviewId).orElseThrow(EntityExceptionSuppliers.reviewNotFound);
        assertEquals("taste bad :(", updated.getText());
        assertEquals(2, updated.getStarPoint());
    }
}
