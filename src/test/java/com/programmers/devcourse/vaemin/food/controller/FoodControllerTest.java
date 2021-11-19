package com.programmers.devcourse.vaemin.food.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmers.devcourse.vaemin.coupon.entity.Coupon;
import com.programmers.devcourse.vaemin.coupon.entity.CouponDiscountType;
import com.programmers.devcourse.vaemin.coupon.repository.CouponRepository;
import com.programmers.devcourse.vaemin.food.controller.bind.FoodGroupInformationRequest;
import com.programmers.devcourse.vaemin.food.controller.bind.FoodInformationRequest;
import com.programmers.devcourse.vaemin.food.entity.DiscountType;
import com.programmers.devcourse.vaemin.food.entity.Food;
import com.programmers.devcourse.vaemin.food.entity.FoodStatus;
import com.programmers.devcourse.vaemin.food.entity.Group;
import com.programmers.devcourse.vaemin.food.repository.FoodRepository;
import com.programmers.devcourse.vaemin.food.repository.GroupRepository;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
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
class FoodControllerTest {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    OwnerRepository ownerRepository;
    @Autowired
    ShopRepository shopRepository;
    @Autowired
    CouponRepository couponRepository;
    @Autowired
    FoodRepository foodRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    Shop shop;
    Coupon coupon;
    Customer customer;
    Owner owner;
    Food food;
    Group group;

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
        food = foodRepository.save(Food.builder()
                .name("FOOD_NAME")
                .shortDesc("SHORT_DESC_FOR_FOOD")
                .price(3500)
                .status(FoodStatus.NORMAL)
                .discountType(DiscountType.NONE)
                .discountAmount(0)
                .shop(shop).build());
        group = groupRepository.save(Group.builder()
                .name("GROUP_NAME")
                .shop(shop).build());
    }

    @Test
    @DisplayName("Create food on shop.")
    void createFood() throws Exception {
        FoodInformationRequest request = new FoodInformationRequest();
        request.setName("NEW_FOOD");
        request.setPrice(4500);
        request.setDiscountType(DiscountType.NONE);
        request.setDiscountAmount(0);
        request.setShortDescription("SHORT_DESC");
        request.setStatus(FoodStatus.NORMAL);

        mockMvc.perform(post("/owners/{ownerId}/shops/{shopId}/foods", owner.getId(), shop.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(jsonPath("$.data").isArray())
                .andDo(document("FoodController/createFood",
                        pathParameters(
                                parameterWithName("ownerId").description("점주의 ID 입니다."),
                                parameterWithName("shopId").description("상점의 ID 입니다.")),
                        requestFields(
                                fieldWithPath("name").description("생성할 상품의 이름입니다."),
                                fieldWithPath("shortDescription").description("생성할 상품의 간략한 설명입니다."),
                                fieldWithPath("price").description("생성할 상품의 가격입니다."),
                                fieldWithPath("discountType").description("생성할 상품의 할인 종류(FIXED/PERCENTAGE)입니다."),
                                fieldWithPath("discountAmount").description("생성할 상품의 할인 금액입니다."),
                                fieldWithPath("status").description("생성할 상품의 상태입니다.")),
                        responseFields(
                                subsectionWithPath("data").description("상점의 상품 목록입니다."),
                                fieldWithPath("serverDatetime").description("서버 시각입니다."),
                                fieldWithPath("message").description("API 응답 메시지입니다."))));
    }

    @Test
    @DisplayName("Delete food from shop.")
    void deleteFood() throws Exception {
        mockMvc.perform(delete("/owners/{ownerId}/shops/{shopId}/foods/{foodId}",
                owner.getId(), shop.getId(), food.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(document("FoodController/deleteFood",
                        pathParameters(
                                parameterWithName("ownerId").description("점주의 ID 입니다."),
                                parameterWithName("shopId").description("상점의 ID 입니다."),
                                parameterWithName("foodId").description("상품의 ID 입니다.")),
                        responseFields(
                                subsectionWithPath("data").description("상점의 상품 목록입니다."),
                                fieldWithPath("serverDatetime").description("서버 시각입니다."),
                                fieldWithPath("message").description("API 응답 메시지입니다."))));
    }

    @Test
    @DisplayName("Update food of shop.")
    void updateFood() throws Exception {
        FoodInformationRequest request = new FoodInformationRequest();
        request.setName("UPDATED_FOOD_NAME");
        request.setPrice(99999999);
        request.setStatus(FoodStatus.OUT_OF_STOCK);
        request.setShortDescription("NOT_SO_SHORT_DESC_FOR_FOOD");
        request.setDiscountAmount(1);
        request.setDiscountType(DiscountType.PERCENTAGE);

        mockMvc.perform(put("/owners/{ownerId}/shops/{shopId}/foods/{foodId}",
                owner.getId(), shop.getId(), food.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.foodName").value(request.getName()))
                .andExpect(jsonPath("$.data.shortDescription").value(request.getShortDescription()))
                .andExpect(jsonPath("$.data.price").value(request.getPrice()))
                .andExpect(jsonPath("$.data.discountType").value(request.getDiscountType().name()))
                .andExpect(jsonPath("$.data.discountAmount").value(request.getDiscountAmount()))
                .andExpect(jsonPath("$.data.status").value(request.getStatus().name()))
                .andDo(document("FoodController/updateFood",
                        pathParameters(
                                parameterWithName("ownerId").description("점주의 ID 입니다."),
                                parameterWithName("shopId").description("상점의 ID 입니다."),
                                parameterWithName("foodId").description("상품의 ID 입니다.")),
                        requestFields(
                                fieldWithPath("name").description("갱신할 상품의 이름입니다."),
                                fieldWithPath("shortDescription").description("갱신할 상품의 간략한 설명입니다."),
                                fieldWithPath("price").description("갱신할 상품의 가격입니다."),
                                fieldWithPath("discountType").description("갱신할 상품의 할인 종류(FIXED/PERCENTAGE)입니다."),
                                fieldWithPath("discountAmount").description("갱신할 상품의 할인 금액입니다."),
                                fieldWithPath("status").description("갱신할 상품의 상태입니다.")),
                        responseFields(
                                subsectionWithPath("data").description("갱신된 상품의 정보입니다."),
                                fieldWithPath("serverDatetime").description("서버 시각입니다."),
                                fieldWithPath("message").description("API 응답 메시지입니다."))));
    }

    @Test
    @DisplayName("Create food group.")
    void createFoodGroup() throws Exception {
        FoodGroupInformationRequest request = new FoodGroupInformationRequest();
        request.setName("FOOD_GROUP_NEW");

        mockMvc.perform(post("/owners/{ownerId}/shops/{shopId}/groups", owner.getId(), shop.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andDo(document("FoodController/createFoodGroup",
                        pathParameters(
                                parameterWithName("ownerId").description("점주의 ID 입니다."),
                                parameterWithName("shopId").description("상점의 ID 입니다.")),
                        requestFields(
                                fieldWithPath("name").description("생성할 상품 그룹의 이름입니다.")),
                        responseFields(
                                subsectionWithPath("data").description("생성된 상품 그룹의 목록입니다."),
                                fieldWithPath("serverDatetime").description("서버 시각입니다."),
                                fieldWithPath("message").description("API 응답 메시지입니다."))));
    }

    @Test
    @DisplayName("Update food group.")
    void updateFoodGroup() throws Exception {
        FoodGroupInformationRequest request = new FoodGroupInformationRequest();
        request.setName("UPDATED_FOOD_GROUP_NAME");

        mockMvc.perform(put("/owners/{ownerId}/shops/{shopId}/groups/{groupId}",
                owner.getId(), shop.getId(), group.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value(request.getName()))
                .andDo(document("FoodController/updateFoodGroup",
                        pathParameters(
                                parameterWithName("ownerId").description("점주의 ID 입니다."),
                                parameterWithName("shopId").description("상점의 ID 입니다."),
                                parameterWithName("groupId").description("상품 그룹의 ID 입니다.")),
                        requestFields(
                                fieldWithPath("name").description("갱신할 상품 그룹의 이름입니다.")),
                        responseFields(
                                subsectionWithPath("data").description("갱신된 상품의 정보입니다."),
                                fieldWithPath("serverDatetime").description("서버 시각입니다."),
                                fieldWithPath("message").description("API 응답 메시지입니다."))));
    }

    @Test
    @DisplayName("Delete food group.")
    void deleteFoodGroup() throws Exception {
        mockMvc.perform(delete("/owners/{ownerId}/shops/{shopId}/groups/{groupId}",
                owner.getId(), shop.getId(), group.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(document("FoodController/deleteFoodGroup",
                        pathParameters(
                                parameterWithName("ownerId").description("점주의 ID 입니다."),
                                parameterWithName("shopId").description("상점의 ID 입니다."),
                                parameterWithName("groupId").description("상품 그룹의 ID 입니다.")),
                        responseFields(
                                subsectionWithPath("data").description("남아있는 상품 그룹의 목록입니다."),
                                fieldWithPath("serverDatetime").description("서버 시각입니다."),
                                fieldWithPath("message").description("API 응답 메시지입니다."))));
    }

    @Test
    @DisplayName("Include food to group.")
    void includeFood() throws Exception {
        mockMvc.perform(put("/owners/{ownerId}/shops/{shopId}/groups/{groupId}/foods/{foodId}",
                owner.getId(), shop.getId(), group.getId(), food.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andDo(document("FoodController/includeFoodToGroup",
                        pathParameters(
                                parameterWithName("ownerId").description("점주의 ID 입니다."),
                                parameterWithName("shopId").description("상점의 ID 입니다."),
                                parameterWithName("groupId").description("상품 그룹의 ID 입니다."),
                                parameterWithName("foodId").description("상품의 ID 입니다.")),
                        responseFields(
                                subsectionWithPath("data").description("상품이 속한 그룹의 목록입니다."),
                                fieldWithPath("serverDatetime").description("서버 시각입니다."),
                                fieldWithPath("message").description("API 응답 메시지입니다."))));

        assertTrue(groupRepository.findById(group.getId()).orElseThrow(IllegalArgumentException::new)
                .getIncludingFoods().stream().anyMatch(fg -> fg.getFood().getId().equals(food.getId())));
    }

    @Test
    @DisplayName("Exclude food from group.")
    void excludeFood() throws Exception {
        group.addFood(food);

        mockMvc.perform(delete("/owners/{ownerId}/shops/{shopId}/groups/{groupId}/foods/{foodId}",
                owner.getId(), shop.getId(), group.getId(), food.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(document("FoodController/excludeFoodFromGroup",
                        pathParameters(
                                parameterWithName("ownerId").description("점주의 ID 입니다."),
                                parameterWithName("shopId").description("상점의 ID 입니다."),
                                parameterWithName("groupId").description("상품 그룹의 ID 입니다."),
                                parameterWithName("foodId").description("상품의 ID 입니다.")),
                        responseFields(
                                subsectionWithPath("data").description("상품이 속한 그룹의 목록입니다."),
                                fieldWithPath("serverDatetime").description("서버 시각입니다."),
                                fieldWithPath("message").description("API 응답 메시지입니다."))));

        assertTrue(groupRepository.findById(group.getId()).orElseThrow(IllegalArgumentException::new)
                .getIncludingFoods().stream().noneMatch(fg -> fg.getFood().getId().equals(food.getId())));
    }
}
