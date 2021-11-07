package com.programmers.devcourse.vaemin.order.controller;

import com.programmers.devcourse.vaemin.order.controller.bind.OrderInformationRequest;
import com.programmers.devcourse.vaemin.order.entity.dto.CustomerOrderDTO;
import com.programmers.devcourse.vaemin.order.service.CustomerOrderService;
import com.programmers.devcourse.vaemin.root.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers/{customerId}/orders")
public class CustomerOrderController {
    private final CustomerOrderService customerOrderService;

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerOrderDTO>> createOrder(OrderInformationRequest request) {
        CustomerOrderDTO order = customerOrderService.createOrder(request);
        return ResponseEntity.created(URI.create(
                String.format("/customers/%d/orders/%d", request.getCustomerId(), order.getId())))
                .body(ApiResponse.success(order));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerOrderDTO>>> listCustomerOrders(
            @PathVariable("customerId") long customerId) {
        List<CustomerOrderDTO> customerOrderDTOS = customerOrderService.listCustomerOrders(customerId);
        return ResponseEntity.ok(ApiResponse.success(customerOrderDTOS));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<CustomerOrderDTO>> readCustomerOrder(
            @PathVariable("customerId") long customerId,
            @PathVariable("orderId") long orderId) {
        CustomerOrderDTO customerOrderDTO = customerOrderService.readCustomerOrder(customerId, orderId);
        return ResponseEntity.ok(ApiResponse.success(customerOrderDTO));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse<CustomerOrderDTO>> revokeCustomerOrder(
            @PathVariable("customerId") long customerId,
            @PathVariable("orderId") long orderId) {
        CustomerOrderDTO customerOrderDTO = customerOrderService.revokeCustomerOrder(customerId, orderId);
        return ResponseEntity.ok(ApiResponse.success(customerOrderDTO));
    }
}
