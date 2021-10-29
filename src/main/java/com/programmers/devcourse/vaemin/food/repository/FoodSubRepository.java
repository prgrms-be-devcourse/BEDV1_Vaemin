package com.programmers.devcourse.vaemin.food.repository;

import com.programmers.devcourse.vaemin.food.entity.Food;
import com.programmers.devcourse.vaemin.food.entity.FoodSub;
import com.programmers.devcourse.vaemin.food.entity.FoodSubSelectGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodSubRepository extends CrudRepository<FoodSub, Long> {
    List<FoodSub> findAllBySelectGroup(FoodSubSelectGroup group);

    List<FoodSub> findAllByFood(Food food);
}
