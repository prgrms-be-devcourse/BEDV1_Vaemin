package com.programmers.devcourse.vaemin.food.service;

import com.programmers.devcourse.vaemin.food.controller.bind.FoodGroupInformationRequest;
import com.programmers.devcourse.vaemin.food.entity.Food;
import com.programmers.devcourse.vaemin.food.entity.FoodGroup;
import com.programmers.devcourse.vaemin.food.entity.Group;
import com.programmers.devcourse.vaemin.food.entity.dto.GroupDTO;
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


    public List<GroupDTO> createFoodGroup(long shopId, FoodGroupInformationRequest request) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(ShopExceptionSuppliers.shopNotFound);
        Group group = Group.builder()
                .name(request.getName())
                .shop(shop).build();
        shop.getGroups().add(group);
        groupRepository.save(group);
        return shop.getGroups().stream().map(GroupDTO::new).collect(Collectors.toList());
    }

    public GroupDTO updateFoodGroup(long groupId, FoodGroupInformationRequest request) {
        Group group = groupRepository.findById(groupId).orElseThrow(FoodEntityExceptionSuppliers.groupNotFound);
        group.changeName(request.getName());
        return new GroupDTO(group);
    }

    public List<GroupDTO> deleteFoodGroup(long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(FoodEntityExceptionSuppliers.groupNotFound);
        group.cleanup();
        groupRepository.delete(group);
        // 읽어오는 시점의 레코드만 있고 다른 엔티티의 연관관계가 변형되어도 데이터베이스에 쓰기 전까지는 반영되지 않는듯?
        return group.getShop().getGroups().stream()
                .map(GroupDTO::new).collect(Collectors.toList());
    }

    public List<GroupDTO> joinFoodGroup(long foodId, long groupId) {
        Food food = foodRepository.findById(foodId).orElseThrow(FoodEntityExceptionSuppliers.foodNotFound);
        Group group = groupRepository.findById(groupId).orElseThrow(FoodEntityExceptionSuppliers.groupNotFound);
        group.addFood(food);
        return food.getJoinedGroups().stream()
                .map(FoodGroup::getGroup)
                .map(GroupDTO::new)
                .collect(Collectors.toList());
    }

    public List<GroupDTO> withdrawFoodGroup(long foodId, long groupId) {
        Food food = foodRepository.findById(foodId).orElseThrow(FoodEntityExceptionSuppliers.foodNotFound);
        Group group = groupRepository.findById(groupId).orElseThrow(FoodEntityExceptionSuppliers.groupNotFound);
        group.removeFood(food);
        return food.getJoinedGroups().stream()
                .map(FoodGroup::getGroup)
                .map(GroupDTO::new)
                .collect(Collectors.toList());
    }

}
