package com.programmers.devcourse.vaemin.shop.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmers.devcourse.vaemin.shop.dto.ShopDto;
import com.programmers.devcourse.vaemin.shop.entity.*;
import com.programmers.devcourse.vaemin.shop.repository.CategoryRepository;
import com.programmers.devcourse.vaemin.shop.service.ShopService;
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

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class ShopControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShopService shopService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    ObjectMapper objectMapper;

    Owner owner;
    Shop shop;
    Long shopId;
    Long ownerId;
    Category category;

    @BeforeEach
    void setUp() {
        owner = ownerRepository.save(Owner.builder()
                .username("OWNER_USERNAME")
                .email("owner@domain.com")
                .phoneNum("123-1234-1234").build());
        ownerId = owner.getId();
        shop = Shop.builder()
                .name("SHOP")
                .phoneNum("010-1234-5678")
                .shortDesc("SHOP CONTROLLER TEST")
                .longDesc("SHOP CONTROLLER TEST")
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
                .build();
        category = categoryRepository.save(Category.builder()
                .name("category").build());
    }

    @Test
    void createShop() throws Exception {
        mockMvc.perform(post("/owners/{ownerId}/shops", ownerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ShopDto(shop))))
                .andExpect(status().isOk()) // 200
                .andDo(print());
    }

    @Test
    void deleteShop() throws Exception {
        ShopDto shopDto = new ShopDto(shop);
        shopId = shopService.createShop(shopDto);
        mockMvc.perform(delete("/owners/{ownerId}/shops/{shopId}", ownerId, shopId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void updateShop() throws Exception {
        ShopDto shopDto = new ShopDto(shop);
        shopId = shopService.createShop(shopDto);
        shop.changeLongDescription("update shop");
        mockMvc.perform(put("/owners/{ownerId}/shops/{shopId}", ownerId, shopId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ShopDto(shop))))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getShop() throws Exception {
        ShopDto shopDto = new ShopDto(shop);
        shopId = shopService.createShop(shopDto);
        mockMvc.perform(get("/owners/{ownerId}/shops/{shopId}", ownerId, shopId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getReviews() throws Exception {
        ShopDto shopDto = new ShopDto(shop);
        shopId = shopService.createShop(shopDto);
        mockMvc.perform(get("/owners/{ownerId}/shops/{shopId}", ownerId, shopId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void joinShopCategory() throws Exception {
        ShopDto shopDto = new ShopDto(shop);
        shopId = shopService.createShop(shopDto);
        mockMvc.perform(post("/owners/{ownerId}/shops/{shopId}/categories/{categoryId}", ownerId, shopId, category.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void withdrawShopCategory() throws Exception {
        ShopDto shopDto = new ShopDto(shop);
        shopId = shopService.createShop(shopDto);
        mockMvc.perform(delete("/owners/{ownerId}/shops/{shopId}/categories/{categoryId}", ownerId, shopId, category.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
