package com.programmers.devcourse.vaemin.food.repository;

import com.programmers.devcourse.vaemin.food.entity.Food;
import com.programmers.devcourse.vaemin.food.entity.FoodSubSelectGroup;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FoodSubSelectGroupRepository extends CrudRepository<FoodSubSelectGroup, Long> {
    List<FoodSubSelectGroup> findAllByParentFood(Food food);
}
