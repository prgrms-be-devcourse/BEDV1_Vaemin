package com.programmers.devcourse.vaemin.foodsub.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmers.devcourse.vaemin.coupon.entity.Coupon;
import com.programmers.devcourse.vaemin.coupon.entity.CouponDiscountType;
import com.programmers.devcourse.vaemin.coupon.repository.CouponRepository;
import com.programmers.devcourse.vaemin.food.controller.bind.FoodSubInformationRequest;
import com.programmers.devcourse.vaemin.food.controller.bind.FoodSubSelectGroupInformationRequest;
import com.programmers.devcourse.vaemin.food.entity.*;
import com.programmers.devcourse.vaemin.food.entity.dto.FoodSubDTO;
import com.programmers.devcourse.vaemin.food.repository.FoodRepository;
import com.programmers.devcourse.vaemin.food.repository.FoodSubRepository;
import com.programmers.devcourse.vaemin.food.repository.FoodSubSelectGroupRepository;
import com.programmers.devcourse.vaemin.food.repository.GroupRepository;
import com.programmers.devcourse.vaemin.foodsub.dto.TestFoodSubDTO;
import com.programmers.devcourse.vaemin.foodsub.dto.TestFoodSubSelectGroupDTO;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
class FoodSubControllerTest {
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
    FoodSubRepository foodSubRepository;
    @Autowired
    FoodSubSelectGroupRepository foodSubSelectGroupRepository;
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
    FoodSub foodSub;
    FoodSubSelectGroup foodSubSelectGroup;

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
        foodSub = foodSubRepository.save(FoodSub.builder()
                .food(food)
                .name("FOOD_SUB_!")
                .price(250)
                .shop(shop)
                .group(null).build());
        foodSubSelectGroup = foodSubSelectGroupRepository.save(FoodSubSelectGroup.builder()
                .groupName("FOOD_SUB_GROUP")
                .food(food)
                .required(false)
                .multiSelect(true).build());
    }

    @Test
    @DisplayName("Create sub food.")
    void createFoodSub() throws Exception {
        FoodSubInformationRequest request = new FoodSubInformationRequest();
        request.setName("FOOD_SUB");
        request.setPrice(500);
        request.setGroup(null);

        mockMvc.perform(post("/owners/{ownerId}/shops/{shopId}/foods/{foodId}/sub",
                owner.getId(), shop.getId(), food.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andDo(result -> {
                    String content = result.getResponse().getContentAsString();
                    ApiResponse<List<TestFoodSubDTO>> subs = objectMapper.readValue(content, new TypeReference<>() {
                    });
                    assertEquals(2, subs.getData().size());
                    assertTrue(subs.getData().stream().anyMatch(dto ->
                        dto.getName().equals(request.getName()) &&
                                dto.getPrice() == request.getPrice()));
                })
                .andDo(document("FoodSubController/createFoodSub",
                        pathParameters(
                                parameterWithName("ownerId").description("점주의 ID 입니다."),
                                parameterWithName("shopId").description("상점의 ID 입니다."),
                                parameterWithName("foodId").description("상품의 ID 입니다.")),
                        requestFields(
                                fieldWithPath("name").description("하위 상품의 이름입니다."),
                                fieldWithPath("price").description("하위 상품의 가격입니다."),
                                fieldWithPath("group").description("하위 상품의 그룹입니다. 그룹에 속하지 않는 경우 null 입니다.")),
                        responseFields(
                                subsectionWithPath("data").description("상품의 하위 상품의 목록입니다."),
                                fieldWithPath("serverDatetime").description("서버 시각입니다."),
                                fieldWithPath("message").description("API 응답 메시지입니다."))));
    }

    @Test
    @DisplayName("Update sub food")
    void updateFoodSub() throws Exception {
        FoodSubInformationRequest request = new FoodSubInformationRequest();
        request.setName("UPDATED_FOOD_SUB_NAME");
        request.setPrice(5000);
        request.setGroup(null);

        mockMvc.perform(put("/owners/{ownerId}/shops/{shopId}/foods/{foodId}/sub/{subId}",
                owner.getId(), shop.getId(), food.getId(), foodSub.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(foodSub.getId()))
                .andExpect(jsonPath("$.data.name").value(request.getName()))
                .andExpect(jsonPath("$.data.price").value(request.getPrice()))
                .andDo(document("FoodSubController/updateFoodSub",
                        pathParameters(
                                parameterWithName("ownerId").description("점주의 ID 입니다."),
                                parameterWithName("shopId").description("상점의 ID 입니다."),
                                parameterWithName("foodId").description("상품의 ID 입니다."),
                                parameterWithName("subId").description("하위 상품의 ID 입니다.")),
                        requestFields(
                                fieldWithPath("name").description("갱신할 하위 상품의 이름입니다."),
                                fieldWithPath("price").description("갱신할 하위 상품의 가격입니다."),
                                fieldWithPath("group").description("갱신할 하위 상품의 그룹입니다. 그룹에 속하지 않는 경우 null 입니다.")),
                        responseFields(
                                subsectionWithPath("data").description("갱신된 상품의 정보입니다."),
                                fieldWithPath("serverDatetime").description("서버 시각입니다."),
                                fieldWithPath("message").description("API 응답 메시지입니다."))));
    }

    @Test
    @DisplayName("Delete food sub.")
    void deleteFoodSub() throws Exception {
        mockMvc.perform(delete("/owners/{ownerId}/shops/{shopId}/foods/{foodId}/sub/{subId}",
                owner.getId(), shop.getId(), food.getId(), foodSub.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(document("FoodSubController/deleteFoodSub",
                        pathParameters(
                                parameterWithName("ownerId").description("점주의 ID 입니다."),
                                parameterWithName("shopId").description("상점의 ID 입니다."),
                                parameterWithName("foodId").description("상품의 ID 입니다."),
                                parameterWithName("subId").description("하위 상품의 ID 입니다.")),
                        responseFields(
                                subsectionWithPath("data").description("상품의 하위 상품의 목록입니다."),
                                fieldWithPath("serverDatetime").description("서버 시각입니다."),
                                fieldWithPath("message").description("API 응답 메시지입니다."))));
    }

    @Test
    @DisplayName("Create food sub group.")
    void createFoodSubGroup() throws Exception {
        FoodSubSelectGroupInformationRequest request = new FoodSubSelectGroupInformationRequest();
        request.setGroupName("GROUP_NAME");
        request.setRequired(false);
        request.setMultiSelect(true);

        mockMvc.perform(post("/owners/{ownerId}/shops/{shopId}/foods/{foodId}/sub/groups",
                owner.getId(), shop.getId(), food.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andDo(result -> {
                    String content = result.getResponse().getContentAsString();
                    ApiResponse<List<TestFoodSubSelectGroupDTO>> groups = objectMapper.readValue(content, new TypeReference<>() {
                    });
                    assertEquals(2, groups.getData().size());
                    assertTrue(groups.getData().stream().anyMatch(dto ->
                        dto.getGroupName().equals(request.getGroupName()) &&
                                dto.isRequired() == request.isRequired() &&
                                dto.isMultiselect() == request.isMultiSelect()));
                })
                .andDo(document("FoodSubController/createFoodSubGroup",
                        pathParameters(
                                parameterWithName("ownerId").description("점주의 ID 입니다."),
                                parameterWithName("shopId").description("상점의 ID 입니다."),
                                parameterWithName("foodId").description("상품의 ID 입니다.")),
                        requestFields(
                                fieldWithPath("groupName").description("생성할 하위 상품 그룹의 이름입니다."),
                                fieldWithPath("multiSelect").description("생성할 하위 상품 그룹의 다중 선택 허용 여부입니다."),
                                fieldWithPath("required").description("생성할 하위 상품 그룹의 필수 선택 여부입니다.")),
                        responseFields(
                                subsectionWithPath("data").description("하위 상품 그룹의 목록입니다."),
                                fieldWithPath("serverDatetime").description("서버 시각입니다."),
                                fieldWithPath("message").description("API 응답 메시지입니다."))));
    }

    @Test
    @DisplayName("Update food sub group.")
    void updateFoodSubGroup() throws Exception {
        FoodSubSelectGroupInformationRequest request = new FoodSubSelectGroupInformationRequest();
        request.setGroupName("UPDATED_SUB_GROUP_NAME");
        request.setRequired(true);
        request.setMultiSelect(false);

        mockMvc.perform(put("/owners/{ownerId}/shops/{shopId}/foods/{foodId}/sub/groups/{groupId}",
                owner.getId(), shop.getId(), food.getId(), foodSubSelectGroup.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(foodSubSelectGroup.getId()))
                .andExpect(jsonPath("$.data.groupName").value(request.getGroupName()))
                .andExpect(jsonPath("$.data.multiselect").value(request.isMultiSelect()))
                .andExpect(jsonPath("$.data.required").value(request.isRequired()))
                .andDo(document("FoodSubController/updateFoodSubGroup",
                        pathParameters(
                                parameterWithName("ownerId").description("점주의 ID 입니다."),
                                parameterWithName("shopId").description("상점의 ID 입니다."),
                                parameterWithName("foodId").description("상품의 ID 입니다."),
                                parameterWithName("groupId").description("하위 상품 그룹의 ID 입니다.")),
                        requestFields(
                                fieldWithPath("groupName").description("갱신할 하위 상품 그룹의 이름입니다."),
                                fieldWithPath("multiSelect").description("갱신할 하위 상품 그룹의 다중 선택 허용 여부입니다."),
                                fieldWithPath("required").description("갱신할 하위 상품 그룹의 필수 선택 여부입니다.")),
                        responseFields(
                                subsectionWithPath("data").description("갱신된 하위 상품 그룹의 정보입니다."),
                                fieldWithPath("serverDatetime").description("서버 시각입니다."),
                                fieldWithPath("message").description("API 응답 메시지입니다."))));
    }

    @Test
    @DisplayName("Delete food sub group.")
    void deleteFoodSubGroup() throws Exception {
        foodSubSelectGroup.includeFood(foodSub);

        mockMvc.perform(delete("/owners/{ownerId}/shops/{shopId}/foods/{foodId}/sub/groups/{groupId}",
                owner.getId(), shop.getId(), food.getId(), foodSubSelectGroup.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(document("FoodSubController/deleteFoodSubGroup",
                        pathParameters(
                                parameterWithName("ownerId").description("점주의 ID 입니다."),
                                parameterWithName("shopId").description("상점의 ID 입니다."),
                                parameterWithName("foodId").description("상품의 ID 입니다."),
                                parameterWithName("groupId").description("하위 상품 그룹의 ID 입니다.")),
                        responseFields(
                                subsectionWithPath("data").description("하위 상품 그룹의 정보입니다."),
                                fieldWithPath("serverDatetime").description("서버 시각입니다."),
                                fieldWithPath("message").description("API 응답 메시지입니다."))));

        assertNull(foodSub.getSelectGroup());
    }

    @Test
    @DisplayName("Include sub food to sub group.")
    void includeFoodSubToGroup() throws Exception {
        mockMvc.perform(put("/owners/{ownerId}/shops/{shopId}/foods/{foodId}/sub/{subId}/groups/{groupId}",
                owner.getId(), shop.getId(), food.getId(), foodSub.getId(), foodSubSelectGroup.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andDo(result -> {
                    String content = result.getResponse().getContentAsString();
                    ApiResponse<List<TestFoodSubDTO>> subs = objectMapper.readValue(content, new TypeReference<>() {
                    });
                    assertEquals(1, subs.getData().size());
                    assertEquals(foodSub.getId(), subs.getData().get(0).getId());
                    assertEquals(foodSub.getName(), subs.getData().get(0).getName());
                    assertEquals(foodSub.getPrice(), subs.getData().get(0).getPrice());
                    assertEquals(foodSub.getSelectGroup().getId(), subs.getData().get(0).getGroup().getId());
                })
                .andDo(document("FoodSubController/includeFoodSubToSubGroup",
                        pathParameters(
                                parameterWithName("ownerId").description("점주의 ID 입니다."),
                                parameterWithName("shopId").description("상점의 ID 입니다."),
                                parameterWithName("foodId").description("상품의 ID 입니다."),
                                parameterWithName("subId").description("하위 상품의 ID 입니다."),
                                parameterWithName("groupId").description("하위 상품 그룹의 ID 입니다.")),
                        responseFields(
                                subsectionWithPath("data").description("하위 상품 그룹에 포함된 하위 상품 목록입니다."),
                                fieldWithPath("serverDatetime").description("서버 시각입니다."),
                                fieldWithPath("message").description("API 응답 메시지입니다."))));

        assertTrue(foodSubSelectGroup.getFoods().stream().anyMatch(fs -> fs.getId().equals(foodSub.getId())));
    }

    @Test
    @DisplayName("Exclude sub food to sub group.")
    void excludeFoodSubToGroup() throws Exception {
        mockMvc.perform(delete("/owners/{ownerId}/shops/{shopId}/foods/{foodId}/sub/{subId}/groups",
                owner.getId(), shop.getId(), food.getId(), foodSub.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andDo(result -> {
                    String content = result.getResponse().getContentAsString();
                    ApiResponse<List<TestFoodSubSelectGroupDTO>> subs = objectMapper.readValue(content, new TypeReference<>() {});
                    assertEquals(1, subs.getData().size());
                    assertEquals(foodSubSelectGroup.getId(), subs.getData().get(0).getId());
                    assertEquals(foodSubSelectGroup.getGroupName(), subs.getData().get(0).getGroupName());
                    assertEquals(foodSubSelectGroup.isRequired(), subs.getData().get(0).isRequired());
                    assertEquals(foodSubSelectGroup.isMultiSelect(), subs.getData().get(0).isMultiselect());
                })
                .andDo(document("FoodSubController/includeFoodSubToSubGroup",
                        pathParameters(
                                parameterWithName("ownerId").description("점주의 ID 입니다."),
                                parameterWithName("shopId").description("상점의 ID 입니다."),
                                parameterWithName("foodId").description("상품의 ID 입니다."),
                                parameterWithName("subId").description("하위 상품의 ID 입니다.")),
                        responseFields(
                                subsectionWithPath("data").description("상품의 하위 상품 그룹의 목록입니다."),
                                fieldWithPath("serverDatetime").description("서버 시각입니다."),
                                fieldWithPath("message").description("API 응답 메시지입니다."))));

        assertTrue(foodSubSelectGroup.getFoods().stream().noneMatch(fs -> fs.getId().equals(foodSub.getId())));
    }
}
