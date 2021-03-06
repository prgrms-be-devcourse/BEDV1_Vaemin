package com.programmers.devcourse.vaemin.shop.repository;

import com.programmers.devcourse.vaemin.shop.entity.Shop;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopRepository extends CrudRepository<Shop, Long> {

    List<Shop> findByNameContaining(String shopName);
}
