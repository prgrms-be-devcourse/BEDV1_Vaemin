package com.programmers.devcourse.vaemin.food.service;

import com.programmers.devcourse.vaemin.food.controller.bind.FoodGroupInformationRequest;
import com.programmers.devcourse.vaemin.food.entity.Food;
import com.programmers.devcourse.vaemin.food.entity.FoodGroup;
import com.programmers.devcourse.vaemin.food.entity.Group;
import com.programmers.devcourse.vaemin.food.entity.dto.GroupDTO;
import com.programmers.devcourse.vaemin.food.repository.FoodGroupRepository;
import com.programmers.devcourse.vaemin.food.repository.FoodRepository;
import com.programmers.devcourse.vaemin.food.repository.GroupRepository;
import com.programmers.devcourse.vaemin.shop.entity.Shop;
import com.programmers.devcourse.vaemin.shop.exception.ShopExceptionSuppliers;
import com.programmers.devcourse.vaemin.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FoodGroupService {
    private final ShopRepository shopRepository;
    private final FoodRepository foodRepository;
    private final GroupRepository groupRepository;
    private final FoodGroupRepository foodGroupRepository;


    public List<GroupDTO> createFoodGroup(long shopId, FoodGroupInformationRequest request) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(ShopExceptionSuppliers.shopNotFound);
        Group group = Group.builder()
                .name(request.getName())
                .shop(shop).build();
        groupRepository.save(group);
        shop.getGroups().add(group);
        return shop.getGroups().stream().map(GroupDTO::new).collect(Collectors.toList());
    }

    public GroupDTO updateFoodGroup(long groupId, FoodGroupInformationRequest request) {
        Group group = groupRepository.findById(groupId).orElseThrow(EntityExceptionSuppliers.groupNotFound);
        group.changeName(request.getName());
        return new GroupDTO(group);
    }

    public List<GroupDTO> deleteFoodGroup(long shopId, long groupId) {
        foodGroupRepository.deleteById(groupId);
        Shop shop = shopRepository.findById(shopId).orElseThrow(ShopExceptionSuppliers.shopNotFound);
        return shop.getGroups().stream().map(GroupDTO::new).collect(Collectors.toList());
    }

    public List<GroupDTO> joinFoodGroup(long foodId, long groupId) {
        Food food = foodRepository.findById(foodId).orElseThrow(EntityExceptionSuppliers.foodNotFound);
        Group group = groupRepository.findById(groupId).orElseThrow(EntityExceptionSuppliers.groupNotFound);

        if(!foodGroupRepository.existsByFoodAndGroup(food, group)) {
            FoodGroup foodGroup = FoodGroup.builder()
                    .food(food)
                    .group(group).build();
            foodGroupRepository.save(foodGroup);
        }

        return foodGroupRepository.findAllByFood(food).stream()
                .map(FoodGroup::getGroup)
                .map(GroupDTO::new)
                .collect(Collectors.toList());
    }

    public List<GroupDTO> withdrawFoodGroup(long foodId, long groupId) {
        Food food = foodRepository.findById(foodId).orElseThrow(EntityExceptionSuppliers.foodNotFound);
        Group group = groupRepository.findById(groupId).orElseThrow(EntityExceptionSuppliers.groupNotFound);
        foodGroupRepository.deleteByFoodAndGroup(food, group);
        return food.getJoinedGroups().stream()
                .map(FoodGroup::getGroup)
                .map(GroupDTO::new)
                .collect(Collectors.toList());
    }

}
