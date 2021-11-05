package com.programmers.devcourse.vaemin.order.repository;

import com.programmers.devcourse.vaemin.order.entity.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
}
