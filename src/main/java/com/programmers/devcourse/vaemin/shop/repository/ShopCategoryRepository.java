package com.programmers.devcourse.vaemin.shop.repository;

import com.programmers.devcourse.vaemin.shop.entity.Category;
import com.programmers.devcourse.vaemin.shop.entity.Shop;
import com.programmers.devcourse.vaemin.shop.entity.ShopCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ShopCategoryRepository extends CrudRepository<ShopCategory, Long> {
    boolean existsByShopAndCategory(Shop shop, Category category);

    ShopCategory findByShopAndCategory(Shop shop, Category category);
    
    void deleteByShopAndCategory(Shop shop, Category category);

    List<ShopCategory> findAllByCategoryId(@Param("categoryId") Long categoryId);
}
