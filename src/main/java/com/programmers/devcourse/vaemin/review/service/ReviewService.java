package com.programmers.devcourse.vaemin.review.service;

import com.programmers.devcourse.vaemin.order.entity.Order;
import com.programmers.devcourse.vaemin.order.repository.OrderRepository;
import com.programmers.devcourse.vaemin.review.controller.bind.ReviewInformationRequest;
import com.programmers.devcourse.vaemin.review.dto.ReviewDto;
import com.programmers.devcourse.vaemin.review.entity.Review;
import com.programmers.devcourse.vaemin.review.repository.ReviewRepository;
import com.programmers.devcourse.vaemin.root.exception.EntityExceptionSuppliers;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import com.programmers.devcourse.vaemin.user.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ReviewRepository reviewRepository;

    public ReviewDto createReview(ReviewInformationRequest request, long customerId, long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityExceptionSuppliers.orderNotFound);
        Customer customer = customerRepository.findById(customerId).orElseThrow(EntityExceptionSuppliers.customerNotFound);
        Review review = reviewRepository.save(Review.builder()
                .order(order)
                .shop(order.getShop())
                .customer(customer)
                .text(request.getText())
                .starPoint(request.getStarPoint())
                .build());
        return new ReviewDto(review);
    }

    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(EntityExceptionSuppliers.reviewNotFound);
        reviewRepository.delete(review);
    }

    public ReviewDto findReview(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(EntityExceptionSuppliers.reviewNotFound);
        return new ReviewDto(review);
    }

    public ReviewDto updateReview(long reviewId, ReviewInformationRequest request) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(EntityExceptionSuppliers.reviewNotFound);
        review.changeText(request.getText());
        review.changeStarPoint(request.getStarPoint());
        return new ReviewDto(review);
    }
}
