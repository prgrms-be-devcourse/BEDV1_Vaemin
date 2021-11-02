package com.programmers.devcourse.vaemin.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ShopSearchControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    ObjectMapper objectMapper;

    private static Shop setShop1;
    private static Shop setShop2;
    private static Category setCategory1;
    private static Category setCategory2;
    private static Owner setOwner1;
    private static Owner setOwner2;
    private static OwnerCreateRequest setOwnerRequest1;
    private static OwnerCreateRequest setOwnerRequest2;

    // question : shop 의 minTime이랑 maxTime의 타입이 LocalDateTime?
    // solved : localtime으로
    @BeforeEach
    void setUp() {

        setOwnerRequest1 = new OwnerCreateRequest("set owner1 name",
                "setowner1@gmail.com",
                "01000000000");
        setOwnerRequest2 = new OwnerCreateRequest("set owner2 name",
                "setowner2@gmail.com",
                "01000000000");

        setOwner1 = ownerService.createOwner(setOwnerRequest1);
        setOwner2 = ownerService.createOwner(setOwnerRequest2);

        setCategory1 = categoryRepository.save(new Category("분식"));
        setCategory2 = categoryRepository.save(new Category("치킨"));

        setShop1 = shopRepository.save(new Shop("set shop 1",
                "01010101010",
                "This is a short description of set shop 1.",
                "This is a long description of set shop 1.",
                ShopSupportedOrderType.BOTH,
                ShopSupportedPayment.CARD,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                2000,
                10000,
                ShopStatus.DEACTIVATED,
                "11111111",
                setOwner1,
                "set shop 1's address",
                123,
                "set shop 1's detail address"
        ));
        setShop2 = shopRepository.save(new Shop("set shop 2",
                "01010101010",
                "This is a short description of set shop 2.",
                "This is a long description of set shop 2.",
                ShopSupportedOrderType.BOTH,
                ShopSupportedPayment.CARD,
                LocalDateTime.now().plusHours(3),
                LocalDateTime.now().plusHours(4),
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
        categoryService.joinShopCategory(setShop2.getId(), setCategory2.getId());
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
}