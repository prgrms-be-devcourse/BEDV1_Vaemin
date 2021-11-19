package com.programmers.devcourse.vaemin.coupon.controller;

import com.programmers.devcourse.vaemin.coupon.entity.Coupon;
import com.programmers.devcourse.vaemin.coupon.entity.CouponDiscountType;
import com.programmers.devcourse.vaemin.coupon.repository.CouponRepository;
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
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ActiveProfiles("test")
@Transactional
class CustomerCouponControllerTest {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    OwnerRepository ownerRepository;
    @Autowired
    ShopRepository shopRepository;
    @Autowired
    CouponRepository couponRepository;
    @Autowired
    MockMvc mockMvc;

    Shop shop;
    Coupon coupon;
    Customer customer;
    Owner owner;

    @BeforeEach
    void init() {
        owner = ownerRepository.save(Owner.builder()
                .username("USERNAME")
                .email("EMAIL")
                .phoneNum("123456789").build());
        customer = customerRepository.save(Customer.builder()
                .addressDetail("ADDR")
                .locationCode("LOC_CODE")
                .phoneNum("123456789")
                .email("EMAIL")
                .userName("USERNAME").build());
        shop = shopRepository.save(Shop.builder()
                .name("SHOP_TEST")
                .phoneNum("123456789")
                .shortDesc("SHORT_DESC")
                .longDesc("LONG_DESC")
                .orderType(ShopSupportedOrderType.BOTH)
                .payment(ShopSupportedPayment.CARD)
                .openTime(LocalTime.now())
                .closeTime(LocalTime.now())
                .deliveryFee(2000)
                .minOrderPrice(15000)
                .shopStatus(ShopStatus.DEACTIVATED)
                .registerNumber("123456789")
                .owner(owner)
                .doroAddress("DORO_ADDR")
                .doroIndex(123)
                .detailAddress("DETAIL_ADDR").build());
        coupon = couponRepository.save(Coupon.builder()
                .name("COUPON")
                .expirationDate(LocalDateTime.now().plusHours(5))
                .minimumOrderPrice(5000)
                .discountType(CouponDiscountType.FIXED)
                .discountAmount(2000)
                .shop(shop).build());
    }

    @Test
    @DisplayName("Receive coupon")
    void receiveCoupon() throws Exception {
        mockMvc.perform(put("/customers/{customerId}/coupons/{couponId}", customer.getId(), coupon.getId()))
                .andExpect(status().isOk())
                .andDo(
                        document("CouponController/customer/receiveCoupon",
                                pathParameters(
                                        parameterWithName("customerId").description("고객의 ID 입니다."),
                                        parameterWithName("couponId").description("쿠폰의 ID 입니다.")),
                                responseFields(
                                        subsectionWithPath("data").description("고객의 쿠폰 목록입니다."),
                                        fieldWithPath("serverDatetime").description("서버 시각입니다."),
                                        fieldWithPath("message").description("API 응답 비고 메시지입니다."))));
    }

    @Test
    @DisplayName("Get coupon list")
    void listCoupon() throws Exception {
        customer.addCoupon(coupon);

        mockMvc.perform(get("/customers/{customerId}/coupons", customer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andDo(document("CouponController/customer/listCoupon",
                        pathParameters(parameterWithName("customerId").description("고객의 ID 입니다.")),
                        responseFields(
                                subsectionWithPath("data").description("고객의 쿠폰 목록입니다."),
                                fieldWithPath("serverDatetime").description("서버 시각입니다."),
                                fieldWithPath("message").description("API 응답 비고 메시지입니다."))));
    }

    @Test
    @DisplayName("Get coupon information.")
    void readCoupon() throws Exception {
        customer.addCoupon(coupon);

        mockMvc.perform(get("/customers/{customerId}/coupons/{couponId}", customer.getId(), coupon.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(coupon.getId()))
                .andDo(document("CouponController/customer/readCoupon",
                        pathParameters(
                                parameterWithName("customerId").description("고객의 ID 입니다."),
                                parameterWithName("couponId").description("조회하려는 쿠폰의 ID 입니다.")),
                        responseFields(
                                subsectionWithPath("data").description("고객의 쿠폰 정보입니다."),
                                fieldWithPath("serverDatetime").description("서버 시각입니다."),
                                fieldWithPath("message").description("API 응답 비고 메시지입니다."))));
    }
}
