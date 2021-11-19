package com.programmers.devcourse.vaemin.coupon.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmers.devcourse.vaemin.coupon.controller.bind.CouponInformationRequest;
import com.programmers.devcourse.vaemin.coupon.dto.TestCouponDTO;
import com.programmers.devcourse.vaemin.coupon.entity.Coupon;
import com.programmers.devcourse.vaemin.coupon.entity.CouponDiscountType;
import com.programmers.devcourse.vaemin.coupon.repository.CouponRepository;
import com.programmers.devcourse.vaemin.root.ApiResponse;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ActiveProfiles("test")
@Transactional
class OwnerCouponControllerTest {
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
    @Autowired
    ObjectMapper objectMapper;

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
                .currentAddress(new CustomerDeliveryAddress("LOC_CODE", "ADDR"))
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
    @DisplayName("Create coupon.")
    void createCoupon() throws Exception {
        CouponInformationRequest request = new CouponInformationRequest();
        request.setName("COUPON_NEW");
        request.setExpirationDate(LocalDateTime.of(2022, 2, 2, 2, 2));
        request.setMinimumOrderPrice(5000);
        request.setDiscountType(CouponDiscountType.FIXED);
        request.setDiscountAmount(2500);

        mockMvc.perform(post("/owners/{ownerId}/shops/{shopId}/coupons", owner.getId(), shop.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(jsonPath("$.data.name").value(request.getName()))
                .andExpect(jsonPath("$.data.discountType").value(request.getDiscountType().name()))
                .andExpect(jsonPath("$.data.discountAmount").value(request.getDiscountAmount()))
                .andExpect(jsonPath("$.data.minimumOrderPrice").value(request.getMinimumOrderPrice()))
                .andDo(document("CouponController/owner/createCoupon",
                        pathParameters(
                                parameterWithName("ownerId").description("점주의 ID 입니다."),
                                parameterWithName("shopId").description("쿠폰을 발행할 상점의 ID 입니다.")),
                        requestFields(
                                fieldWithPath("name").description("생성할 쿠폰의 이름입니다."),
                                fieldWithPath("discountType").description("생성할 쿠폰의 종류(FIXED/PERCENTAGE)입니다."),
                                fieldWithPath("discountAmount").description("생성할 쿠폰의 할인값입니다."),
                                fieldWithPath("minimumOrderPrice").description("생성할 쿠폰의 최소 주문가격입니다."),
                                fieldWithPath("expirationDate").description("생성할 쿠폰의 만료일입니다. ISO DATE_TIME 규격을 준수합니다.")),
                        responseFields(
                                subsectionWithPath("data").description("생성된 쿠폰의 정보입니다."),
                                fieldWithPath("serverDatetime").description("서버 시각입니다."),
                                fieldWithPath("message").description("API 응답 비고 메시지입니다."))));
    }

    @Test
    @DisplayName("List coupon.")
    void listCoupon() throws Exception {
        mockMvc.perform(get("/owners/{ownerId}/shops/{shopId}/coupons", owner.getId(), shop.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andDo(document("CouponController/owner/listCoupon",
                        pathParameters(
                                parameterWithName("ownerId").description("점주의 ID 입니다."),
                                parameterWithName("shopId").description("쿠폰을 발행한 상점의 ID 입니다.")),
                        responseFields(
                                subsectionWithPath("data").description("쿠폰의 목록입니다."),
                                fieldWithPath("serverDatetime").description("서버 시각입니다."),
                                fieldWithPath("message").description("API 응답 비고 메시지입니다."))))
                .andDo(result -> {
                    String content = result.getResponse().getContentAsString();
                    ApiResponse<List<TestCouponDTO>> readResult = objectMapper.readValue(content, new TypeReference<>() {
                    });
                    assertEquals(1, readResult.getData().size());
                    assertEquals(coupon.getId(), readResult.getData().get(0).getId());
                    assertEquals(coupon.getName(), readResult.getData().get(0).getName());
                    assertEquals(coupon.getExpirationDate().truncatedTo(ChronoUnit.SECONDS), readResult.getData().get(0).getExpirationDate());
                    assertEquals(coupon.getMinimumOrderPrice(), readResult.getData().get(0).getMinimumOrderPrice());
                    assertEquals(coupon.getDiscountType(), readResult.getData().get(0).getDiscountType());
                    assertEquals(coupon.getDiscountAmount(), readResult.getData().get(0).getDiscountAmount());
                });
    }

    @Test
    @DisplayName("Read coupon.")
    void readCoupon() throws Exception {
        mockMvc.perform(get("/owners/{ownerId}/shops/{shopId}/coupons/{couponId}", owner.getId(), shop.getId(), coupon.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value(coupon.getName()))
                .andExpect(jsonPath("$.data.discountType").value(coupon.getDiscountType().name()))
                .andExpect(jsonPath("$.data.discountAmount").value(coupon.getDiscountAmount()))
                .andExpect(jsonPath("$.data.minimumOrderPrice").value(coupon.getMinimumOrderPrice()))
                .andDo(document("CouponController/owner/readCoupon",
                        pathParameters(
                                parameterWithName("ownerId").description("점주의 ID 입니다."),
                                parameterWithName("shopId").description("쿠폰을 발행한 상점의 ID 입니다."),
                                parameterWithName("couponId").description("읽을 쿠폰의 ID 입니다.")),
                        responseFields(
                                subsectionWithPath("data").description("쿠폰의 정보입니다."),
                                fieldWithPath("serverDatetime").description("서버 시각입니다."),
                                fieldWithPath("message").description("API 응답 비고 메시지입니다."))));
    }

    @Test
    @DisplayName("Delete coupon.")
    void deleteCoupon() throws Exception {
        Coupon newCoupon = couponRepository.save(Coupon.builder()
                .name("ALIVE_COUPON")
                .expirationDate(LocalDateTime.now().plusHours(5))
                .minimumOrderPrice(3000)
                .discountType(CouponDiscountType.FIXED)
                .discountAmount(1000)
                .shop(shop).build());
        mockMvc.perform(delete("/owners/{ownerId}/shops/{shopId}/coupons/{couponId}",
                owner.getId(), shop.getId(), coupon.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andDo(
                        document("CouponController/owner/deleteCoupon",
                                pathParameters(
                                        parameterWithName("ownerId").description("점주의 ID 입니다."),
                                        parameterWithName("shopId").description("쿠폰을 발행할 상점의 ID 입니다."),
                                        parameterWithName("couponId").description("쿠폰의 ID 입니다.")),
                                responseFields(
                                        subsectionWithPath("data").description("남아있는 쿠폰의 목록입니다."),
                                        fieldWithPath("serverDatetime").description("서버 시각입니다."),
                                        fieldWithPath("message").description("API 응답 비고 메시지입니다."))))
                .andDo(result -> {
                    ApiResponse<List<TestCouponDTO>> readResult = objectMapper.readValue(
                            result.getResponse().getContentAsString(), new TypeReference<>() {});
                    assertEquals(1, readResult.getData().size());
                    assertEquals(newCoupon.getId(), readResult.getData().get(0).getId());
                    assertEquals(newCoupon.getDiscountType(), readResult.getData().get(0).getDiscountType());
                    assertEquals(newCoupon.getDiscountAmount(), readResult.getData().get(0).getDiscountAmount());
                    assertEquals(newCoupon.getMinimumOrderPrice(), readResult.getData().get(0).getMinimumOrderPrice());
                    assertEquals(newCoupon.getName(), readResult.getData().get(0).getName());
                });
    }
}
