package com.programmers.devcourse.vaemin.review.controller;

import com.programmers.devcourse.vaemin.review.dto.ReviewDto;
import com.programmers.devcourse.vaemin.root.ApiResponse;
import com.programmers.devcourse.vaemin.shop.dto.ShopDto;
import com.programmers.devcourse.vaemin.shop.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ShopReviewController {
    private final ShopService shopService;

    @GetMapping("/shops/{shopId}/reviews")
    public ApiResponse<List<ReviewDto>> getReviews(@PathVariable Long shopId) {
        ShopDto shop = shopService.findShop(shopId);
        return ApiResponse.success(shop.getReviews());
    }
}
