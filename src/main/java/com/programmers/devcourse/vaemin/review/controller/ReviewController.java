package com.programmers.devcourse.vaemin.review.controller;

import com.programmers.devcourse.vaemin.review.dto.ReviewDto;
import com.programmers.devcourse.vaemin.review.service.ReviewService;
import com.programmers.devcourse.vaemin.root.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/customers/{userId}/orders/{orderId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ApiResponse<Long> createReview(@Valid @RequestBody ReviewDto reviewDto, @PathVariable Long userId, @PathVariable Long orderId) {
        return ApiResponse.success(reviewService.createReview(reviewDto));
    }

    @GetMapping("/{reviewId}")
    public ApiResponse<ReviewDto> getReview(@PathVariable Long reviewId, @PathVariable Long userId, @PathVariable Long orderId) {
        ReviewDto review = reviewService.findReview(reviewId);
        return ApiResponse.success(review);
    }

    @PutMapping("/{reviewId}")
    public ApiResponse<Long> updateReview(@PathVariable Long reviewId, @Valid @RequestBody ReviewDto reviewDto, @PathVariable Long userId, @PathVariable Long orderId) {
        return ApiResponse.success(reviewService.updateReview(reviewId, reviewDto));
    }

    @DeleteMapping("/{reviewId}")
    public ApiResponse<Long> deleteReview(@PathVariable Long reviewId, @PathVariable Long userId, @PathVariable Long orderId) {
        reviewService.deleteReview(reviewId);
        return ApiResponse.success(reviewId);
    }
}
