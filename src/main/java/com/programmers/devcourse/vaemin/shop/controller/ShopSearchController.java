package com.programmers.devcourse.vaemin.shop.controller;

import com.programmers.devcourse.vaemin.root.ApiResponse;
import com.programmers.devcourse.vaemin.shop.dto.ShopSearchResponse;
import com.programmers.devcourse.vaemin.shop.service.ShopService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shops/search")
public class ShopSearchController {
    private final ShopService shopService;

    public ShopSearchController(ShopService shopService) {
        this.shopService = shopService;
    }

    // question : valid annotation, @RequestBody로 사용자의 지역을 받아와야하나?
    @GetMapping
    public ApiResponse<List<ShopSearchResponse>> getAllShops() {
        return ApiResponse.success(shopService.findAllShops());
    }

    @GetMapping("/name/{shopName}")
    public ApiResponse<List<ShopSearchResponse>> getByName(
            @PathVariable String shopName
    ) {
        return ApiResponse.success(shopService.findByName(shopName));
    }

    @GetMapping("/category/{categoryId}")
    public ApiResponse<List<ShopSearchResponse>> getByCategory(
            @PathVariable Long categoryId
    ) {
        return ApiResponse.success(shopService.findByCategory(categoryId));
    }

    @GetMapping("/food/{foodName}")
    public ApiResponse<List<ShopSearchResponse>> getByFoodName(
            @PathVariable String foodName
    ) {
        return ApiResponse.success(shopService.findByFoodName(foodName));
    }
}
