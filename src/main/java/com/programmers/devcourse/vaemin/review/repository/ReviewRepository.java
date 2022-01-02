package com.programmers.devcourse.vaemin.review.repository;

import com.programmers.devcourse.vaemin.review.entity.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {
}
