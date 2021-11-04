package com.programmers.devcourse.vaemin.user.customer.controller;

import com.programmers.devcourse.vaemin.order.dto.OrderResponse;
import com.programmers.devcourse.vaemin.order.service.OrderService;
import com.programmers.devcourse.vaemin.root.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customers/{customerId}/order")
public class CustomerOrderController {
    private final OrderService orderService;

    public CustomerOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ApiResponse<List<OrderResponse>> getAllOrders(
            @PathVariable Long customerId
    ) {
        List<OrderResponse> orders = orderService.getAllByCustomerId(customerId);
        return ApiResponse.success(orders);
    }

}