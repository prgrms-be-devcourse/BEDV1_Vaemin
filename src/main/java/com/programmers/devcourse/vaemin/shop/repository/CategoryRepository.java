package com.programmers.devcourse.vaemin.shop.repository;

import com.programmers.devcourse.vaemin.shop.entity.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
}