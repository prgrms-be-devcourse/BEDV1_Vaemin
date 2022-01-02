package com.programmers.devcourse.vaemin.foodsub;

import com.programmers.devcourse.vaemin.food.controller.bind.FoodSubSelectGroupInformationRequest;
import com.programmers.devcourse.vaemin.food.entity.*;
import com.programmers.devcourse.vaemin.food.entity.dto.FoodSubDTO;
import com.programmers.devcourse.vaemin.food.entity.dto.FoodSubSelectGroupDTO;
import com.programmers.devcourse.vaemin.food.repository.FoodRepository;
import com.programmers.devcourse.vaemin.food.repository.FoodSubRepository;
import com.programmers.devcourse.vaemin.food.repository.FoodSubSelectGroupRepository;
import com.programmers.devcourse.vaemin.food.service.FoodSubSelectGroupService;
import com.programmers.devcourse.vaemin.food.service.FoodSubService;
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
class FoodSubGroupServiceTest {
    @Autowired
    FoodRepository foodRepository;
    @Autowired
    ShopRepository shopRepository;
    @Autowired
    OwnerRepository ownerRepository;
    @Autowired
    FoodSubRepository foodSubRepository;
    @Autowired
    FoodSubService foodSubService;
    @Autowired
    FoodSubSelectGroupRepository foodSubSelectGroupRepository;
    @Autowired
    FoodSubSelectGroupService foodSubSelectGroupService;

    Owner owner;
    Shop shop;
    Food food;

    @BeforeEach
    void init() {
        owner = ownerRepository.save(new Owner("username", "email@domain.com", "012-4567-8910"));
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
        food = foodRepository.save(Food.builder()
                .name("NAME")
                .discountType(DiscountType.FIXED)
                .discountAmount(2500)
                .shop(shop)
                .shortDesc("SHORT_DESCRIPTION")
                .price(7500)
                .status(FoodStatus.UNAVAILABLE).build());
    }

    @Test
    @DisplayName("Create sub group.")
    void createSubGroup() {
        FoodSubSelectGroupInformationRequest request = new FoodSubSelectGroupInformationRequest();
        request.setGroupName("SUB_GROUP_1");
        request.setMultiSelect(false);
        request.setRequired(true);
        List<FoodSubSelectGroupDTO> selectGroup = foodSubSelectGroupService.createSelectGroup(food.getId(), request);
        assertEquals(request.getGroupName(), selectGroup.get(0).getGroupName());
        assertFalse(selectGroup.get(0).isMultiselect());
        assertTrue(selectGroup.get(0).isRequired());
        assertTrue(foodSubSelectGroupRepository.existsById(selectGroup.get(0).getId()));
    }

    @Test
    @DisplayName("Delete sub group.")
    void deleteSubGroup() {
        FoodSubSelectGroup subGroup = FoodSubSelectGroup.builder()
                .groupName("SUB_GROUP")
                .food(food)
                .required(false)
                .multiSelect(true).build();

        FoodSub foodSub1 = foodSubRepository.save(FoodSub.builder()
                .food(food)
                .group(subGroup)
                .shop(shop)
                .price(500)
                .name("SUB_FOOD1").build());
        FoodSub foodSub2 = foodSubRepository.save(FoodSub.builder()
                .food(food)
                .group(subGroup)
                .shop(shop)
                .price(1500)
                .name("SUB_FOOD2").build());

        foodSubRepository.save(foodSub1);
        foodSubRepository.save(foodSub2);

        List<FoodSubSelectGroupDTO> foodSubSelectGroupDTOS = foodSubSelectGroupService.deleteSelectGroup(subGroup.getId());
        assertTrue(foodSubSelectGroupDTOS.isEmpty());

        List<FoodSubSelectGroup> allByParentFood = foodSubSelectGroupRepository.findAllByParentFood(food);
        assertTrue(allByParentFood.isEmpty());

        FoodSub foundFoodSub1 = foodSubRepository.findById(foodSub1.getId()).orElseThrow(EntityExceptionSuppliers.foodSubNotFound);
        assertNull(foundFoodSub1.getSelectGroup());

        FoodSub foundFoodSub2 = foodSubRepository.findById(foodSub2.getId()).orElseThrow(EntityExceptionSuppliers.foodSubNotFound);
        assertNull(foundFoodSub2.getSelectGroup());
    }

    @Test
    @DisplayName("Join sub group.")
    void joinSubGroup() {
        FoodSubSelectGroup subGroup = FoodSubSelectGroup.builder()
                .groupName("SUB_GROUP")
                .food(food)
                .required(false)
                .multiSelect(true).build();
        FoodSub foodSub1 = foodSubRepository.save(FoodSub.builder()
                .food(food)
                .group(subGroup)
                .shop(shop)
                .price(500)
                .name("SUB_FOOD1").build());
        FoodSub foodSub2 = foodSubRepository.save(FoodSub.builder()
                .food(food)
                .group(null)
                .shop(shop)
                .price(1500)
                .name("SUB_FOOD2").build());

        List<FoodSubSelectGroupDTO> selectGroups = foodSubSelectGroupService.getSelectGroups(food.getId());
        List<FoodSubDTO> foodSubFromSubGroup = foodSubService.getFoodSubFromSubGroup(selectGroups.get(0).getId());
        assertEquals(1, foodSubFromSubGroup.size());
        assertTrue(foodSubFromSubGroup.stream()
                .anyMatch(foodSubDTO -> foodSubDTO.getId() == foodSub1.getId() &&
                        foodSubDTO.getName().equals("SUB_FOOD1") &&
                        foodSubDTO.getPrice() == 500 &&
                        foodSubDTO.getGroup().getId() == subGroup.getId()));

        List<FoodSubDTO> foodSubDTOS = foodSubSelectGroupService.joinSelectGroup(foodSub2.getId(), subGroup.getId());
        assertEquals(2, foodSubDTOS.size());
        assertTrue(foodSubDTOS.stream()
                .anyMatch(foodSubDTO -> foodSubDTO.getId() == foodSub2.getId() &&
                        foodSubDTO.getName().equals("SUB_FOOD2") &&
                        foodSubDTO.getPrice() == 1500 &&
                        foodSubDTO.getGroup().getId() == subGroup.getId()));
    }

    @Test
    @DisplayName("Withdraw sub group.")
    void withdrawSubGroup() {
        FoodSubSelectGroup subGroup = FoodSubSelectGroup.builder()
                .groupName("SUB_GROUP")
                .food(food)
                .required(false)
                .multiSelect(true).build();
        FoodSub foodSub1 = foodSubRepository.save(FoodSub.builder()
                .food(food)
                .group(subGroup)
                .shop(shop)
                .price(500)
                .name("SUB_FOOD1").build());
        FoodSub foodSub2 = foodSubRepository.save(FoodSub.builder()
                .food(food)
                .group(subGroup)
                .shop(shop)
                .price(1500)
                .name("SUB_FOOD2").build());

        foodSubSelectGroupService.withdrawSelectGroup(foodSub2.getId());
        FoodSub foundFoodSub1 = foodSubRepository.findById(foodSub1.getId()).orElseThrow(EntityExceptionSuppliers.foodSubNotFound);
        assertEquals(subGroup, foundFoodSub1.getSelectGroup());
        FoodSub foundFoodSub2 = foodSubRepository.findById(foodSub2.getId()).orElseThrow(EntityExceptionSuppliers.foodSubNotFound);
        assertNull(foundFoodSub2.getSelectGroup());
        FoodSubSelectGroup group = foodSubSelectGroupRepository.findById(subGroup.getId()).orElseThrow(EntityExceptionSuppliers.foodSubSelectGroupNotFound);
        assertEquals(1, group.getFoods().size());
        assertEquals(foodSub1, group.getFoods().get(0));
    }
}
