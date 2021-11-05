package com.programmers.devcourse.vaemin.review.service;

import com.programmers.devcourse.vaemin.review.dto.ReviewDto;
import com.programmers.devcourse.vaemin.review.entity.Review;
import com.programmers.devcourse.vaemin.review.exception.ReviewExceptionSuppliers;
import com.programmers.devcourse.vaemin.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Long createReview(ReviewDto reviewDto) {
        Review review = Review.builder()
                .order(reviewDto.getOrder())
                .customer(reviewDto.getCustomer())
                .text(reviewDto.getText())
                .starPoint(reviewDto.getStarPoint())
                .build();

        return reviewRepository.save(review).getId();
    }

    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(ReviewExceptionSuppliers.reviewNotFound);
        reviewRepository.delete(review);
    }

    public ReviewDto findReview(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(ReviewExceptionSuppliers.reviewNotFound);
        return new ReviewDto(review);
    }

    public Long updateReview(Long id, ReviewDto reviewDto) {
        Review review = reviewRepository.findById(id).orElseThrow(ReviewExceptionSuppliers.reviewNotFound);
        review.changeText(reviewDto.getText());
        review.changeStarPoint(reviewDto.getStarPoint());

        return review.getId();
    }

}
