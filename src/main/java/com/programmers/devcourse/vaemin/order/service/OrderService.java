package com.programmers.devcourse.vaemin.order.service;

import com.programmers.devcourse.vaemin.order.dto.OrderDetailResponse;
import com.programmers.devcourse.vaemin.order.dto.OrderResponse;
import com.programmers.devcourse.vaemin.order.entity.Order;
import com.programmers.devcourse.vaemin.order.exception.OrderExceptionSupplier;
import com.programmers.devcourse.vaemin.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public List<OrderResponse> getAllByCustomerId(Long customerId) {
        List<OrderResponse> orders = orderRepository.findAllByCustomerId(customerId).stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
        return orders;
    }

    @Transactional
    public OrderDetailResponse getOneById(Long customerId, Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderExceptionSupplier.orderNotFound);
        if (customerId.equals(order.getCustomer().getId())) {
            throw new IllegalArgumentException("This Customer has no access to this order.");
        }
        return new OrderDetailResponse(order);
    }
}
