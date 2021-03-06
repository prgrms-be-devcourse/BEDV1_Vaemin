package com.programmers.devcourse.vaemin.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmers.devcourse.vaemin.food.controller.bind.FoodInformationRequest;
import com.programmers.devcourse.vaemin.food.entity.DiscountType;
import com.programmers.devcourse.vaemin.food.entity.FoodStatus;
import com.programmers.devcourse.vaemin.food.service.FoodService;
import com.programmers.devcourse.vaemin.shop.entity.*;
import com.programmers.devcourse.vaemin.shop.repository.CategoryRepository;
import com.programmers.devcourse.vaemin.shop.repository.ShopRepository;
import com.programmers.devcourse.vaemin.shop.service.CategoryService;
import com.programmers.devcourse.vaemin.user.owner.dto.OwnerCreateRequest;
import com.programmers.devcourse.vaemin.user.owner.entity.Owner;
import com.programmers.devcourse.vaemin.user.owner.service.OwnerService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ShopSearchControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private OwnerService ownerService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FoodService foodService;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    ObjectMapper objectMapper;

    private static Category setCategory1;

    @BeforeEach
    void setUp() {
        OwnerCreateRequest setOwnerRequest1 = new OwnerCreateRequest("set owner1 name",
                "setowner1@gmail.com",
                "01000000000");
        OwnerCreateRequest setOwnerRequest2 = new OwnerCreateRequest("set owner2 name",
                "setowner2@gmail.com",
                "01000000000");

        Owner setOwner1 = ownerService.createOwner(setOwnerRequest1);
        Owner setOwner2 = ownerService.createOwner(setOwnerRequest2);

        categoryRepository.deleteByName("??????");
        categoryRepository.deleteByName("??????");
        setCategory1 = categoryRepository.save(new Category("??????"));
        Category setCategory2 = categoryRepository.save(new Category("??????"));

        Shop setShop1 = shopRepository.save(new Shop("set shop 1",
                "01010101010",
                "This is a short description of set shop 1.",
                "This is a long description of set shop 1.",
                ShopSupportedOrderType.BOTH,
                ShopSupportedPayment.CARD,
                LocalTime.now(),
                LocalTime.now().plusHours(2),
                2000,
                10000,
                ShopStatus.DEACTIVATED,
                "11111111",
                setOwner1,
                "set shop 1's address",
                123,
                "set shop 1's detail address"
        ));
        Shop setShop2 = shopRepository.save(new Shop("set shop 2",
                "01010101010",
                "This is a short description of set shop 2.",
                "This is a long description of set shop 2.",
                ShopSupportedOrderType.BOTH,
                ShopSupportedPayment.CARD,
                LocalTime.now().plusHours(3),
                LocalTime.now().plusHours(4),
                3000,
                12000,
                ShopStatus.DEACTIVATED,
                "2222222",
                setOwner2,
                "set shop 2's address",
                123,
                "set shop 2's detail address"
        ));

        categoryService.joinShopCategory(setShop1.getId(), setCategory1.getId());
        categoryService.joinShopCategory(setShop2.getId(), setCategory1.getId());

        foodService.createFood(setShop1.getId(),
                new FoodInformationRequest("?????????", "?????? ?????? ?????????", 12000, DiscountType.FIXED, 0, FoodStatus.NORMAL));
        foodService.createFood(setShop2.getId(),
                new FoodInformationRequest("?????????", "shop1?????? ????????? ?????????", 10000, DiscountType.FIXED, 0, FoodStatus.NORMAL));
    }

    @Test
    void getAllShops() throws Exception {
        mockMvc.perform(get("/shops/search")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getShopsByName() throws Exception {
        mockMvc.perform(get("/shops/search/name/{shopName}", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getShopsByCategory() throws Exception {
        mockMvc.perform(get("/shops/search/category/{categoryId}", setCategory1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getShopsByFoodName() throws Exception {
        mockMvc.perform(get("/shops/search/food/{foodName}", "?????????")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}