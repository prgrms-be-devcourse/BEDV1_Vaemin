package com.programmers.devcourse.vaemin.food.repository;

import com.programmers.devcourse.vaemin.food.entity.Food;
import com.programmers.devcourse.vaemin.shop.entity.Shop;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends CrudRepository<Food, Long> {
    Optional<Food> findByIdAndShop(long id, Shop shop);
    List<Food> findAllByShop(Shop shop);
}
