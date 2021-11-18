package com.programmers.devcourse.vaemin.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmers.devcourse.vaemin.order.entity.Order;
import com.programmers.devcourse.vaemin.order.entity.OrderStatus;
import com.programmers.devcourse.vaemin.order.repository.OrderRepository;
import com.programmers.devcourse.vaemin.payment.entity.Payment;
import com.programmers.devcourse.vaemin.payment.entity.PaymentStatus;
import com.programmers.devcourse.vaemin.payment.repository.PaymentRepository;
import com.programmers.devcourse.vaemin.review.dto.ReviewDto;
import com.programmers.devcourse.vaemin.review.entity.Review;
import com.programmers.devcourse.vaemin.review.service.ReviewService;
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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ObjectMapper objectMapper;

    Review review;
    Long reviewId;
    Long userId;
    Long orderId;
    Long shopId;

    @BeforeEach
    void setUp() {
        Owner owner = ownerRepository.save(Owner.builder()
                .username("OWNER_USERNAME")
                .email("owner@domain.com")
                .phoneNum("123-1234-1234").build());
        Shop shop = shopRepository.save(Shop.builder()
                .name("test")
                .phoneNum("010-1234-5678")
                .shortDesc("shop service test")
                .longDesc("test is success")
                .orderType(ShopSupportedOrderType.BOTH)
                .payment(ShopSupportedPayment.CARD)
                .openTime(LocalTime.of(11, 0, 0))
                .closeTime(LocalTime.of(22, 0, 0))
                .deliveryFee(3000)
                .minOrderPrice(15000)
                .shopStatus(ShopStatus.DEACTIVATED)
                .registerNumber("123-456-7890")
                .owner(owner)
                .doroAddress("Doro-ro")
                .doroIndex(123)
                .detailAddress("Seoul")
                .build());
        shopId = shop.getId();
        Customer customer = customerRepository.save(Customer.builder()
                .userName("USERNAME")
                .email("email@domain.com")
                .phoneNum("010-1234-5678")
                .locationCode("LOCATION_CODE")
                .addressDetail("ADDR_DETAIL").build());
        userId = customer.getId();
        Payment payment = paymentRepository.save(Payment.builder()
                .paymentStatus(PaymentStatus.PAYED)
                .price(10000)
                .customer(customer).build());
        Order order = orderRepository.save(Order.builder()
                .customer(customer)
                .shop(shop)
                .payment(payment)
                .orderStatus(OrderStatus.CREATED)
                .totalPrice(20000)
                .appliedCoupon(null).build());
        orderId = order.getId();
        review = Review.builder()
                .order(order)
                .customer(customer)
                .shop(shop)
                .text("taste good!! :)")
                .starPoint(10)
                .build();
        ReviewDto reviewDto = new ReviewDto(review);
        reviewId = reviewService.createReview(reviewDto);
    }

    @Test
    void createReview() throws Exception {
        mockMvc.perform(post("/customers/{userId}/orders/{orderId}/reviews", userId, orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ReviewDto(review))))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getReview() throws Exception {
        mockMvc.perform(get("/customers/{userId}/orders/{orderId}/reviews/{reviewId}", userId, orderId, reviewId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void updateReview() throws Exception {
        review.changeStarPoint(3);
        review.changeText("taste bad :(");
        mockMvc.perform(put("/customers/{userId}/orders/{orderId}/reviews/{reviewId}", userId, orderId, reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ReviewDto(review))))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void deleteReview() throws Exception {
        mockMvc.perform(delete("/customers/{userId}/orders/{orderId}/reviews/{reviewId}", userId, orderId, reviewId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getReviews() throws Exception {
        mockMvc.perform(get("/shops/{shopId}/reviews", shopId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}