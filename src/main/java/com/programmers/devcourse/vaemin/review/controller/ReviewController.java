package com.programmers.devcourse.vaemin.review.controller;

import com.programmers.devcourse.vaemin.review.dto.ReviewDto;
import com.programmers.devcourse.vaemin.review.service.ReviewService;
import com.programmers.devcourse.vaemin.root.ApiResponse;
import com.programmers.devcourse.vaemin.shop.dto.ShopDto;
import com.programmers.devcourse.vaemin.shop.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ShopService shopService;

    @PostMapping("/customers/{userId}/orders/{orderId}/reviews")
    public ApiResponse<Long> createReview(@RequestBody ReviewDto reviewDto, @PathVariable Long userId, @PathVariable Long orderId) {
        return ApiResponse.success(reviewService.createReview(reviewDto));
    }

    @GetMapping("/customers/{userId}/orders/{orderId}/reviews/{reviewId}")
    public ApiResponse<ReviewDto> getReview(@PathVariable Long reviewId, @PathVariable Long userId, @PathVariable Long orderId) {
        ReviewDto review = reviewService.findReview(reviewId);
        return ApiResponse.success(review);
    }

    @PutMapping("/customers/{userId}/orders/{orderId}/reviews/{reviewId}")
    public ApiResponse<Long> updateReview(@PathVariable Long reviewId, @RequestBody ReviewDto reviewDto, @PathVariable Long userId, @PathVariable Long orderId) {
        return ApiResponse.success(reviewService.updateReview(reviewId, reviewDto));
    }

    @DeleteMapping("/customers/{userId}/orders/{orderId}/reviews/{reviewId}")
    public ApiResponse<Long> deleteReview(@PathVariable Long reviewId, @PathVariable Long userId, @PathVariable Long orderId) {
        reviewService.deleteReview(reviewId);
        return ApiResponse.success(reviewId);
    }

    @GetMapping("/shops/{shopId}/reviews")
    public ApiResponse<List<ReviewDto>> getReviews(@PathVariable Long shopId) {
        ShopDto shop = shopService.findShop(shopId);
        return ApiResponse.success(shop.getReviews());
    }
}
