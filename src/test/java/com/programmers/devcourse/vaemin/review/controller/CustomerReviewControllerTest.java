package com.programmers.devcourse.vaemin.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmers.devcourse.vaemin.order.entity.Order;
import com.programmers.devcourse.vaemin.order.entity.OrderStatus;
import com.programmers.devcourse.vaemin.order.repository.OrderRepository;
import com.programmers.devcourse.vaemin.payment.entity.Payment;
import com.programmers.devcourse.vaemin.payment.entity.PaymentStatus;
import com.programmers.devcourse.vaemin.payment.repository.PaymentRepository;
import com.programmers.devcourse.vaemin.review.controller.bind.ReviewInformationRequest;
import com.programmers.devcourse.vaemin.review.entity.Review;
import com.programmers.devcourse.vaemin.review.repository.ReviewRepository;
import com.programmers.devcourse.vaemin.review.service.ReviewService;
import com.programmers.devcourse.vaemin.shop.entity.Shop;
import com.programmers.devcourse.vaemin.shop.entity.ShopStatus;
import com.programmers.devcourse.vaemin.shop.entity.ShopSupportedOrderType;
import com.programmers.devcourse.vaemin.shop.entity.ShopSupportedPayment;
import com.programmers.devcourse.vaemin.shop.repository.ShopRepository;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import com.programmers.devcourse.vaemin.user.customer.entity.CustomerDeliveryAddress;
import com.programmers.devcourse.vaemin.user.customer.repository.CustomerRepository;
import com.programmers.devcourse.vaemin.user.owner.entity.Owner;
import com.programmers.devcourse.vaemin.user.owner.repository.OwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class CustomerReviewControllerTest {

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
    private ReviewRepository reviewRepository;

    @Autowired
    ObjectMapper objectMapper;

    Owner owner;
    Shop shop;
    Customer customer;
    Payment payment;
    Order order;
    Review review;

    @BeforeEach
    void setUp() {
        owner = ownerRepository.save(Owner.builder()
                .username("OWNER_USERNAME")
                .email("owner@domain.com")
                .phoneNum("123-1234-1234").build());
        shop = shopRepository.save(Shop.builder()
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
                .customer(customer)
                .shop(shop)
                .payment(payment)
                .orderStatus(OrderStatus.CREATED)
                .totalPrice(20000)
                .appliedCoupon(null).build());
        review = reviewRepository.save(Review.builder()
                .customer(customer)
                .shop(shop)
                .order(order)
                .text("TASTE GOOD")
                .starPoint(10).build());
    }

    @Test
    void createReview() throws Exception {
        Order order = orderRepository.save(Order.builder()
                .customer(customer)
                .shop(shop)
                .payment(payment)
                .orderStatus(OrderStatus.CREATED)
                .totalPrice(20000)
                .appliedCoupon(null).build());

        ReviewInformationRequest request = new ReviewInformationRequest();
        request.setText("TASTE NOT GOOD!!");
        request.setStarPoint(3);

        mockMvc.perform(post("/customers/{userId}/orders/{orderId}/reviews", customer.getId(), order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(jsonPath("$.data.order.id").value(order.getId()))
                .andExpect(jsonPath("$.data.customer.id").value(customer.getId()));

        // do document here
    }

    @Test
    void getReview() throws Exception {
        mockMvc.perform(get("/customers/{userId}/orders/{orderId}/reviews/{reviewId}",
                customer.getId(), order.getId(), review.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(review.getId()));
    }

    @Test
    void updateReview() throws Exception {
        ReviewInformationRequest request = new ReviewInformationRequest();
        request.setText("taste bad :(");
        request.setStarPoint(3);
        mockMvc.perform(put("/customers/{userId}/orders/{orderId}/reviews/{reviewId}",
                customer.getId(), order.getId(), review.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.starPoint").value(request.getStarPoint()))
                .andExpect(jsonPath("$.data.text").value(request.getText()));
    }

    @Test
    void deleteReview() throws Exception {
        mockMvc.perform(delete("/customers/{userId}/orders/{orderId}/reviews/{reviewId}",
                customer.getId(), order.getId(), review.getId()))
                .andExpect(status().isNoContent());
        long id = review.getId();
        assertThrows(IllegalArgumentException.class, () -> reviewService.findReview(id));
    }

    @Test
    void getReviews() throws Exception {
        mockMvc.perform(get("/shops/{shopId}/reviews", shop.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isNotEmpty());
        // instantiate and assert more.
    }
}