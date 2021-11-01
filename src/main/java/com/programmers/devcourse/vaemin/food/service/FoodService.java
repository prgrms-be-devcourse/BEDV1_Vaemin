package com.programmers.devcourse.vaemin.food.service;

import com.programmers.devcourse.vaemin.food.controller.bind.FoodInformationRequest;
import com.programmers.devcourse.vaemin.food.entity.Food;
import com.programmers.devcourse.vaemin.food.entity.dto.FoodDTO;
import com.programmers.devcourse.vaemin.food.repository.FoodRepository;
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
public class FoodService {
    private final ShopRepository shopRepository;
    private final FoodRepository foodRepository;


    public List<FoodDTO> createFood(long shopId, FoodInformationRequest request) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(ShopExceptionSuppliers.shopNotFound);
        Food food = Food.builder()
                .name(request.getName())
                .shortDesc(request.getShortDescription())
                .price(request.getPrice())
                .discountType(request.getDiscountType())
                .discountAmount(request.getDiscountAmount())
                .shop(shop)
                .status(request.getStatus()).build();
        foodRepository.save(food);
        return foodRepository.findAllByShop(shop).stream()
                .map(FoodDTO::new)
                .collect(Collectors.toList());
    }

    public List<FoodDTO> deleteFood(long foodId) {
        Food food = foodRepository.findById(foodId).orElseThrow(EntityExceptionSuppliers.foodNotFound);
        foodRepository.delete(food);
        return food.getShop().getFoods().stream().map(FoodDTO::new).collect(Collectors.toList());
    }

    public FoodDTO updateFood(long shopId, long foodId, FoodInformationRequest request) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(ShopExceptionSuppliers.shopNotFound);
        Food food = foodRepository.findByIdAndShop(foodId, shop).orElseThrow(EntityExceptionSuppliers.foodNotFound);
        food.changeName(request.getName());
        food.changeDescription(request.getShortDescription());
        food.changePrice(request.getPrice());
        food.changeDiscountType(request.getDiscountType());
        food.changeDiscountAmount(request.getDiscountAmount());
        food.changeFoodStatus(request.getStatus());
        return new FoodDTO(food);
    }
}
