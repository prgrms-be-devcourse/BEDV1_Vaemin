package com.programmers.devcourse.vaemin.food;

import com.programmers.devcourse.vaemin.food.controller.bind.FoodGroupInformationRequest;
import com.programmers.devcourse.vaemin.food.entity.*;
import com.programmers.devcourse.vaemin.food.entity.dto.GroupDTO;
import com.programmers.devcourse.vaemin.food.repository.FoodGroupRepository;
import com.programmers.devcourse.vaemin.food.repository.FoodRepository;
import com.programmers.devcourse.vaemin.food.repository.GroupRepository;
import com.programmers.devcourse.vaemin.food.service.EntityExceptionSuppliers;
import com.programmers.devcourse.vaemin.food.service.FoodGroupService;
import com.programmers.devcourse.vaemin.food.service.FoodService;
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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class FoodGroupServiceTest {
    @Autowired
    FoodService foodService;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    FoodGroupService foodGroupService;
    @Autowired
    FoodRepository foodRepository;
    @Autowired
    ShopRepository shopRepository;
    @Autowired
    OwnerRepository ownerRepository;
    @Autowired
    FoodGroupRepository foodGroupRepository;

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
    @DisplayName("Group creation test.")
    void createGroup() {
        FoodGroupInformationRequest request = new FoodGroupInformationRequest();
        request.setName("FOOD_GROUP");
        List<GroupDTO> foodGroup = foodGroupService.createFoodGroup(shop.getId(), request);
        assertEquals(request.getName(), foodGroup.get(0).getName());
        Group createdGroup = groupRepository.findById(foodGroup.get(0).getId()).orElseThrow(EntityExceptionSuppliers.groupNotFound);
        assertEquals(request.getName(), createdGroup.getName());
    }

    @Test
    @DisplayName("Group join test.")
    void joinGroup() {
        Food food = foodRepository.save(Food.builder()
                .name("Chicken Burrito")
                .discountAmount(1500)
                .discountType(DiscountType.FIXED)
                .price(4500)
                .status(FoodStatus.UNAVAILABLE)
                .shop(shop)
                .shortDesc("Burrito made of chicken!").build());
        Group group1 = groupRepository.save(Group.builder()
                .name("FRIED_DISHES")
                .shop(shop).build());
        Group group2 = groupRepository.save(Group.builder()
                .name("WELL_DISHES")
                .shop(shop).build());

        List<GroupDTO> joinedGroups = foodGroupService.joinFoodGroup(food.getId(), group1.getId());
        assertEquals(group1.getId(), joinedGroups.get(0).getId());

        assertTrue(groupRepository.findById(group1.getId()).orElseThrow(EntityExceptionSuppliers.groupNotFound)
                .getIncludingFoods().stream().map(FoodGroup::getFood).anyMatch(food::equals));
        assertTrue(foodRepository.findById(food.getId()).orElseThrow(EntityExceptionSuppliers.foodNotFound)
                .getJoinedGroups().stream().map(FoodGroup::getGroup).anyMatch(group1::equals));

        joinedGroups = foodGroupService.joinFoodGroup(food.getId(), group2.getId());
        assertTrue(joinedGroups.stream().anyMatch(groupDTO -> groupDTO.getId() == group2.getId()));

        assertTrue(groupRepository.findById(group2.getId()).orElseThrow(EntityExceptionSuppliers.groupNotFound)
                .getIncludingFoods().stream().map(FoodGroup::getFood).anyMatch(food::equals));
        assertTrue(foodRepository.findById(food.getId()).orElseThrow(EntityExceptionSuppliers.foodNotFound)
                .getJoinedGroups().stream().map(FoodGroup::getGroup).anyMatch(group2::equals));
    }

    @Test
    @DisplayName("Group withdraw test.")
    void withdrawGroup() {
        Group group1 = Group.builder()
                .name("FRIED_DISHES")
                .shop(shop).build();
        Group group2 = Group.builder()
                .name("WELL_DISHES")
                .shop(shop).build();

        Food food = Food.builder()
                .name("Chicken Burrito")
                .discountAmount(1500)
                .discountType(DiscountType.FIXED)
                .price(4500)
                .status(FoodStatus.UNAVAILABLE)
                .shop(shop)
                .shortDesc("Burrito made of chicken!").build();

        food.joinGroup(group1);
        food.joinGroup(group2);
        foodRepository.save(food);

        List<GroupDTO> joinedGroups = foodGroupService.withdrawFoodGroup(food.getId(), group1.getId());
        assertTrue(joinedGroups.stream().noneMatch(groupDTO -> groupDTO.getId() == group1.getId()));
        assertTrue(joinedGroups.stream().anyMatch(groupDTO -> groupDTO.getId() == group2.getId()));

        List<Group> groups = foodRepository.findById(food.getId()).orElseThrow(EntityExceptionSuppliers.foodNotFound).getJoinedGroups()
                .stream().map(FoodGroup::getGroup).collect(Collectors.toList());
        assertTrue(groups.stream().noneMatch(group1::equals));
        assertTrue(groups.stream().anyMatch(group2::equals));

        assertTrue(foodGroupRepository.findByFoodAndGroup(food, group1).isEmpty());
        assertTrue(foodGroupRepository.findByFoodAndGroup(food, group2).isPresent());
    }
}
