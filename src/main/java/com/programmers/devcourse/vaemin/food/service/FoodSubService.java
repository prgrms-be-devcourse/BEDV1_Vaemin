package com.programmers.devcourse.vaemin.food.service;

import com.programmers.devcourse.vaemin.food.controller.bind.FoodSubInformationRequest;
import com.programmers.devcourse.vaemin.food.entity.Food;
import com.programmers.devcourse.vaemin.food.entity.FoodSub;
import com.programmers.devcourse.vaemin.food.entity.FoodSubSelectGroup;
import com.programmers.devcourse.vaemin.food.entity.dto.FoodSubDTO;
import com.programmers.devcourse.vaemin.food.repository.FoodRepository;
import com.programmers.devcourse.vaemin.food.repository.FoodSubRepository;
import com.programmers.devcourse.vaemin.food.repository.FoodSubSelectGroupRepository;
import com.programmers.devcourse.vaemin.root.exception.EntityExceptionSuppliers;
import com.programmers.devcourse.vaemin.shop.entity.Shop;
import com.programmers.devcourse.vaemin.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FoodSubService {
    private final ShopRepository shopRepository;
    private final FoodRepository foodRepository;
    private final FoodSubRepository foodSubRepository;
    private final FoodSubSelectGroupRepository foodSubSelectGroupRepository;

    public List<FoodSubDTO> createFoodSub(long shopId, long foodId, FoodSubInformationRequest request) {
        Food food = foodRepository.findById(foodId).orElseThrow(EntityExceptionSuppliers.foodNotFound);
        Shop shop = shopRepository.findById(shopId).orElseThrow(EntityExceptionSuppliers.shopNotFound);
        FoodSub.FoodSubBuilder foodSubBuilder = FoodSub.builder()
                .shop(shop)
                .food(food)
                .name(request.getName())
                .price(request.getPrice());

        if(request.getGroup() != null) {
            foodSubSelectGroupRepository.findById(request.getGroup())
                    .ifPresent(foodSubBuilder::group);
        }

        FoodSub foodSub = foodSubBuilder.build();
        foodSubRepository.save(foodSub);
        return foodSubRepository.findAllByFood(food).stream()
                .map(FoodSubDTO::new)
                .collect(Collectors.toList());
    }

    public List<FoodSubDTO> deleteFoodSub(long foodSubId) {
        FoodSub foodSub = foodSubRepository.findById(foodSubId).orElseThrow(EntityExceptionSuppliers.foodSubNotFound);
        foodSub.withdrawGroup();
        foodSub.getFood().getSubFoods().remove(foodSub);
        foodSubRepository.delete(foodSub);
        return foodSub.getFood().getSubFoods().stream()
                .map(FoodSubDTO::new)
                .collect(Collectors.toList());
    }

    public FoodSubDTO updateFoodSub(long foodSubId, FoodSubInformationRequest request) {
        FoodSub foodSub = foodSubRepository.findById(foodSubId).orElseThrow(EntityExceptionSuppliers.foodSubNotFound);
        foodSub.changeName(request.getName());
        foodSub.changePrice(request.getPrice());
        if(request.getGroup() != null) {
            FoodSubSelectGroup group = foodSubSelectGroupRepository.findById(request.getGroup()).orElseThrow(EntityExceptionSuppliers.foodSubSelectGroupNotFound);
            foodSub.changeGroup(group);
        }
        return new FoodSubDTO(foodSub);
    }

    public List<FoodSubDTO> getFoodSubFromSubGroup(long groupId) {
        FoodSubSelectGroup group = foodSubSelectGroupRepository.findById(groupId).orElseThrow(EntityExceptionSuppliers.foodSubSelectGroupNotFound);
        return group.getFoods().stream()
                .map(FoodSubDTO::new)
                .collect(Collectors.toList());
    }
}
