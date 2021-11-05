package com.programmers.devcourse.vaemin.shop.controller;

import com.programmers.devcourse.vaemin.review.dto.ReviewDto;
import com.programmers.devcourse.vaemin.root.ApiResponse;
import com.programmers.devcourse.vaemin.shop.dto.ShopDto;
import com.programmers.devcourse.vaemin.shop.service.CategoryService;
import com.programmers.devcourse.vaemin.shop.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/owners/{ownerId}/shops")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;
    private final CategoryService categoryService;

    @PostMapping
    public ApiResponse<Long> createShop(@Valid @RequestBody ShopDto shopDto, @PathVariable Long ownerId) {
        return ApiResponse.success(shopService.createShop(shopDto));
    }

    @DeleteMapping("/{shopId}")
    public ApiResponse<Long> deleteShop(@Valid @PathVariable Long ownerId, @PathVariable Long shopId) {
        shopService.deleteShop(shopId);
        return ApiResponse.success(shopId);
    }

    @GetMapping("/{shopId}")
    public ApiResponse<ShopDto> getShop(@PathVariable Long shopId, @PathVariable Long ownerId) {
        ShopDto shop = shopService.findShop(shopId);
        return ApiResponse.success(shop);
    }

    @PutMapping("/{shopId}")
    public ApiResponse<Long> updateShop(@PathVariable Long shopId, @Valid @RequestBody ShopDto shopDto, @PathVariable Long ownerId) {
        return ApiResponse.success(shopService.updateShop(shopId, shopDto));
    }

    @GetMapping("/{shopId}/reviews")
    public ApiResponse<List<ReviewDto>> getReviews(@PathVariable Long shopId, @PathVariable Long ownerId) {
        ShopDto shop = shopService.findShop(shopId);
        List<ReviewDto> reviews = shop.getReviews();
        return ApiResponse.success(reviews);
    }

    @PutMapping("/{shopId}/categories/{categoryId}")
    public ApiResponse<Long> joinShopCategory(@PathVariable Long shopId, @PathVariable Long categoryId, @PathVariable Long ownerId) {
        return ApiResponse.success(categoryService.joinShopCategory(shopId, categoryId));
    }

    @DeleteMapping("/{shopId}/categories/{categoryId}")
    public ApiResponse<Long> withdrawShopCategory(@PathVariable Long shopId, @PathVariable Long categoryId, @PathVariable Long ownerId) {
        categoryService.withdrawShopCategory(shopId, categoryId);
        return ApiResponse.success(null, String.format("Category %d deleted.", categoryId));
    }

}