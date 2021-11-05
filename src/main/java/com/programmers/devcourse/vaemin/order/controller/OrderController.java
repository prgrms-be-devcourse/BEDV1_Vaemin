package com.programmers.devcourse.vaemin.order.controller;

import com.programmers.devcourse.vaemin.order.controller.bind.OrderInformationRequest;
import com.programmers.devcourse.vaemin.order.entity.dto.CustomerOrderDTO;
import com.programmers.devcourse.vaemin.order.service.OrderService;
import com.programmers.devcourse.vaemin.root.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerOrderDTO>> createOrder(OrderInformationRequest request) {
        long paymentId = orderService.requestPayment(request);
        CustomerOrderDTO order = orderService.createOrder(request, paymentId);
        return ResponseEntity.created(URI.create(
                String.format("/customers/%d/orders/%d", request.getCustomerId(), order.getId())))
                .body(ApiResponse.success(order));
    }
}
