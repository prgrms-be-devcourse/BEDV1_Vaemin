package com.programmers.devcourse.vaemin.foodsub;

import com.programmers.devcourse.vaemin.food.controller.bind.FoodSubInformationRequest;
import com.programmers.devcourse.vaemin.food.entity.*;
import com.programmers.devcourse.vaemin.food.entity.dto.FoodSubDTO;
import com.programmers.devcourse.vaemin.food.repository.FoodRepository;
import com.programmers.devcourse.vaemin.food.repository.FoodSubRepository;
import com.programmers.devcourse.vaemin.food.repository.FoodSubSelectGroupRepository;
import com.programmers.devcourse.vaemin.food.exception.FoodEntityExceptionSuppliers;
import com.programmers.devcourse.vaemin.food.service.FoodSubService;
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
class FoodSubServiceTest {
    @Autowired
    FoodSubService foodSubService;
    @Autowired
    FoodRepository foodRepository;
    @Autowired
    ShopRepository shopRepository;
    @Autowired
    OwnerRepository ownerRepository;
    @Autowired
    FoodSubSelectGroupRepository groupRepository;
    @Autowired
    FoodSubRepository foodSubRepository;

    Owner owner;
    Shop shop;
    Food food;

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
        food = Food.builder()
                .name("Chicken Burrito")
                .discountAmount(1500)
                .discountType(DiscountType.FIXED)
                .price(4500)
                .status(FoodStatus.UNAVAILABLE)
                .shop(shop)
                .shortDesc("Burrito made of chicken!").build();
        foodRepository.save(food);
    }

    @Test
    @DisplayName("Create group-less food-sub on food")
    void createFoodSub() {
        FoodSubInformationRequest request = new FoodSubInformationRequest();
        request.setPrice(500);
        request.setName("French Fries");
        request.setGroup(null);
        List<FoodSubDTO> foodSub = foodSubService.createFoodSub(shop.getId(), food.getId(), request);
        assertTrue(foodSub.stream().anyMatch(foodSubDTO ->
                foodSubDTO.getPrice() == 500 &&
                        foodSubDTO.getName().equals(request.getName()) &&
                        foodSubDTO.getGroup() == null));
    }

    @Test
    @DisplayName("Create grouped food-sub on food")
    void createFoodSubWithGroup() {
        FoodSubSelectGroup subSelectGroup = groupRepository.save(
                FoodSubSelectGroup.builder()
                        .food(food)
                        .groupName("TEST_SELECTION_GROUP")
                        .multiSelect(false)
                        .required(true).build());
        FoodSubInformationRequest request = new FoodSubInformationRequest();
        request.setPrice(500);
        request.setName("French Fries");
        request.setGroup(subSelectGroup.getId());

        List<FoodSubDTO> foodSub = foodSubService.createFoodSub(shop.getId(), food.getId(), request);
        assertTrue(foodSub.stream().anyMatch(foodSubDTO ->
                foodSubDTO.getPrice() == 500 &&
                        foodSubDTO.getName().equals(request.getName()) &&
                        foodSubDTO.getGroup().getId() == subSelectGroup.getId()));

        List<FoodSub> foodSubInGroup = foodSubRepository.findAllBySelectGroup(subSelectGroup);
        assertTrue(foodSubInGroup.stream().anyMatch(foodSubDTO ->
                foodSubDTO.getPrice() == 500 &&
                        foodSubDTO.getName().equals(request.getName()) &&
                        foodSubDTO.getSelectGroup() == subSelectGroup));
    }

    @Test
    @DisplayName("Delete group-less food-sub from food")
    void deleteFoodSub() {
        FoodSub frenchFries = foodSubRepository.save(FoodSub.builder()
                .food(food)
                .name("FRENCH_FRIES")
                .price(500)
                .shop(shop)
                .group(null).build());
        foodSubService.deleteFoodSub(frenchFries.getId());
        assertTrue(foodSubRepository.findById(frenchFries.getId()).isEmpty());
    }

    @Test
    @DisplayName("Delete grouped food-sub from food")
    void deleteFoodSubGrouped() {
        FoodSubSelectGroup group = groupRepository.save(FoodSubSelectGroup.builder()
                .groupName("FOOD_SUB_GROUP")
                .food(food)
                .multiSelect(false)
                .required(true)
                .build());
        FoodSub frenchFries = foodSubRepository.save(FoodSub.builder()
                .food(food)
                .name("FRENCH_FRIES")
                .price(500)
                .shop(shop)
                .group(group).build());

        List<FoodSubDTO> foodSubDTOS = foodSubService.deleteFoodSub(frenchFries.getId());
        assertTrue(foodSubDTOS.stream().noneMatch(foodSubDTO -> foodSubDTO.getGroup().getId() == group.getId()));

        List<FoodSub> allBySelectGroup = foodSubRepository.findAllBySelectGroup(group);
        assertTrue(allBySelectGroup.stream().noneMatch(foodSub -> foodSub.getSelectGroup().equals(group)));
    }

    @Test
    @DisplayName("Food-sub information update.")
    void updateFoodSub() {
        FoodSub foodSub = foodSubRepository.save(FoodSub.builder()
                .food(food)
                .shop(shop)
                .name("FOOD_SUB")
                .price(2500)
                .group(null)
                .build());
        FoodSubSelectGroup group = groupRepository.save(FoodSubSelectGroup.builder()
                .groupName("FOOD_SUB_GROUP")
                .food(food)
                .multiSelect(true)
                .required(false)
                .build());
        FoodSubInformationRequest request = new FoodSubInformationRequest();
        request.setName("UPDATED_FOOD_SUB");
        request.setPrice(500);
        request.setGroup(group.getId());
        FoodSubDTO foodSubDTO = foodSubService.updateFoodSub(foodSub.getId(), request);

        FoodSub updatedFood = foodSubRepository.findById(foodSubDTO.getId()).orElseThrow(FoodEntityExceptionSuppliers.foodSubNotFound);
        assertEquals("UPDATED_FOOD_SUB", updatedFood.getName());
        assertEquals(500, updatedFood.getPrice());
        assertEquals(group, updatedFood.getSelectGroup());
    }
}
