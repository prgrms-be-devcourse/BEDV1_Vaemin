package com.programmers.devcourse.vaemin.review.controller;

import com.programmers.devcourse.vaemin.review.controller.bind.ReviewInformationRequest;
import com.programmers.devcourse.vaemin.review.dto.ReviewDto;
import com.programmers.devcourse.vaemin.review.service.ReviewService;
import com.programmers.devcourse.vaemin.root.ApiResponse;
import com.programmers.devcourse.vaemin.shop.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers/{userId}/orders/{orderId}/reviews")
public class CustomerReviewController {

    private final ReviewService reviewService;
    private final ShopService shopService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewDto>> createReview(
            @Valid @RequestBody ReviewInformationRequest request,
            @PathVariable Long userId,
            @PathVariable Long orderId) {
        ReviewDto review = reviewService.createReview(request, userId, orderId);
        return ResponseEntity
                .created(URI.create(String.format("/customers/%d/orders/%d/reviews/%d",
                        userId, orderId, review.getId())))
                .body(ApiResponse.success(review));
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewDto>> getReview(
            @PathVariable Long reviewId,
            @PathVariable Long userId,
            @PathVariable Long orderId) {
        ReviewDto review = reviewService.findReview(reviewId);
        return ResponseEntity.ok(ApiResponse.success(review));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewDto>> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewInformationRequest request,
            @PathVariable Long userId,
            @PathVariable Long orderId) {
        ReviewDto updatedReview = reviewService.updateReview(reviewId, request);
        return ResponseEntity.ok(ApiResponse.success(updatedReview));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Object>> deleteReview(@PathVariable Long reviewId, @PathVariable Long userId, @PathVariable Long orderId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
