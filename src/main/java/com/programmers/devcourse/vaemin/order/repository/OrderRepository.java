package com.programmers.devcourse.vaemin.order.repository;

import com.programmers.devcourse.vaemin.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByCustomerId(Long customerId);

}
