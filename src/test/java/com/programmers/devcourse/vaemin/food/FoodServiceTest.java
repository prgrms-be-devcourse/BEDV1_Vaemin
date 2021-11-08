package com.programmers.devcourse.vaemin.food;

import com.programmers.devcourse.vaemin.food.controller.bind.FoodInformationRequest;
import com.programmers.devcourse.vaemin.food.entity.DiscountType;
import com.programmers.devcourse.vaemin.food.entity.Food;
import com.programmers.devcourse.vaemin.food.entity.FoodStatus;
import com.programmers.devcourse.vaemin.food.entity.dto.FoodDTO;
import com.programmers.devcourse.vaemin.food.repository.FoodRepository;
import com.programmers.devcourse.vaemin.food.service.FoodService;
import com.programmers.devcourse.vaemin.root.exception.EntityExceptionSuppliers;
import com.programmers.devcourse.vaemin.shop.entity.Shop;
import com.programmers.devcourse.vaemin.shop.entity.ShopStatus;
import com.programmers.devcourse.vaemin.shop.entity.ShopSupportedOrderType;
import com.programmers.devcourse.vaemin.shop.entity.ShopSupportedPayment;
import com.programmers.devcourse.vaemin.shop.repository.ShopRepository;
import com.programmers.devcourse.vaemin.user.owner.entity.Owner;
import com.programmers.devcourse.vaemin.user.owner.repository.OwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class FoodServiceTest {
    @Autowired
    FoodService foodService;
    @Autowired
    FoodRepository foodRepository;
    @Autowired
    ShopRepository shopRepository;
    @Autowired
    OwnerRepository ownerRepository;

    Owner owner;
    Shop shop;

    @BeforeEach
    void init() {
        owner = new Owner("username", "email", "phoneNum");
        ownerRepository.save(owner);
        shop = new Shop(
                "shop_test",
                "012-345-6789",
                "Shop for food service test.",
                "Shop for food service test.",
                ShopSupportedOrderType.BOTH,
                ShopSupportedPayment.CARD,
                LocalTime.now(),
                LocalTime.now(),
                2000,
                15000,
                ShopStatus.DEACTIVATED,
                "123-456-7890",
                owner,
                "Doro-ro",
                123,
                "Seoul");
        shopRepository.save(shop);
    }

    @Test
    @DisplayName("Add food to shop by owner.")
    void addFood() {
        FoodInformationRequest request = new FoodInformationRequest();
        request.setName("Chicken Burrito");
        request.setDiscountAmount(1500);
        request.setDiscountType(DiscountType.FIXED);
        request.setPrice(4500);
        request.setStatus(FoodStatus.UNAVAILABLE);
        request.setShortDescription("Burrito made of chicken!");
        List<FoodDTO> food = foodService.createFood(shop.getId(), request);
        assertTrue(food.stream().anyMatch(foodDTO ->
                foodDTO.getName().equals(request.getName()) &&
                foodDTO.getDiscountType().equals(DiscountType.FIXED) &&
                foodDTO.getDiscountAmount() == 1500 &&
                foodDTO.getPrice() == 4500 &&
                foodDTO.getStatus().equals(FoodStatus.UNAVAILABLE)));
    }

    @Test
    @DisplayName("Delete food from shop by owner.")
    void deleteFood() {
        Food food = Food.builder()
                .name("Chicken Burrito")
                .discountAmount(1500)
                .discountType(DiscountType.FIXED)
                .price(4500)
                .status(FoodStatus.UNAVAILABLE)
                .shop(shop)
                .shortDesc("Burrito made of chicken!").build();
        foodRepository.save(food);
        foodService.deleteFood(food.getId());
        assertFalse(foodRepository.existsById(food.getId()));
    }

    @Test
    @DisplayName("Update food information.")
    void updateFood() {
        Food food = foodRepository.save(Food.builder()
                .name("FOOD_NAME")
                .shortDesc("SHORT_DESCRIPTION")
                .shop(shop)
                .price(2500)
                .discountType(DiscountType.NONE)
                .discountAmount(0)
                .status(FoodStatus.UNAVAILABLE)
                .build());
        FoodInformationRequest request = new FoodInformationRequest();
        request.setShortDescription("UPDATED_SHORT_DESCRIPTION");
        request.setPrice(5500);
        request.setStatus(FoodStatus.NORMAL);
        request.setName("UPDATED_FOOD_NAME");
        request.setDiscountType(DiscountType.FIXED);
        request.setDiscountAmount(50);
        FoodDTO foodDTO = foodService.updateFood(shop.getId(), food.getId(), request);

        Food updated = foodRepository.findById(foodDTO.getId()).orElseThrow(EntityExceptionSuppliers.foodNotFound);
        assertEquals("UPDATED_FOOD_NAME", updated.getName());
        assertEquals("UPDATED_SHORT_DESCRIPTION", updated.getShortDescription());
        assertEquals(5500, updated.getPrice());
        assertEquals(FoodStatus.NORMAL, updated.getStatus());
        assertEquals(DiscountType.FIXED, updated.getDiscountType());
        assertEquals(50, updated.getDiscountAmount());
        assertEquals(shop, updated.getShop());
    }
}
