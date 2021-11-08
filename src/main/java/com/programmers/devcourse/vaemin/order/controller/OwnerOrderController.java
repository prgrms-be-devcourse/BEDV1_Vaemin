package com.programmers.devcourse.vaemin.order.controller;

import com.programmers.devcourse.vaemin.order.controller.bind.OrderInformationRequest;
import com.programmers.devcourse.vaemin.order.controller.bind.OrderStatusRequest;
import com.programmers.devcourse.vaemin.order.entity.dto.OwnerOrderDTO;
import com.programmers.devcourse.vaemin.order.service.ShopOrderService;
import com.programmers.devcourse.vaemin.root.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/owners/{ownerId}/shops/{shopId}/orders")
public class OwnerOrderController {
    private final ShopOrderService shopOrderService;


    @GetMapping
    public ResponseEntity<ApiResponse<List<OwnerOrderDTO>>> listShopOrders(
            @PathVariable("ownerId") long ownerId,
            @PathVariable("shopId") long shopId) {
        List<OwnerOrderDTO> ownerOrderDTOS = shopOrderService.listShopOrders(ownerId, shopId);
        return ResponseEntity.ok(ApiResponse.success(ownerOrderDTOS));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OwnerOrderDTO>> readShopOrder(
            @PathVariable("ownerId") long ownerId,
            @PathVariable("shopId") long shopId,
            @PathVariable("orderId") long orderId) {
        OwnerOrderDTO ownerOrderDTO = shopOrderService.readShopOrder(ownerId, shopId, orderId);
        return ResponseEntity.ok(ApiResponse.success(ownerOrderDTO));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OwnerOrderDTO>> receiveOrder(
            @PathVariable("ownerId") long ownerId,
            @PathVariable("shopId") long shopId,
            @PathVariable("orderId") long orderId,
            @RequestBody OrderStatusRequest request) {
        OwnerOrderDTO ownerOrderDTO = shopOrderService.receiveOrder(ownerId, shopId, orderId, request);
        return ResponseEntity.ok(ApiResponse.success(ownerOrderDTO));
    }

    @GetMapping("/waiting")
    public ResponseEntity<ApiResponse<List<OwnerOrderDTO>>> getWaitingOrders(
            @PathVariable("ownerId") long ownerId,
            @PathVariable("shopId") long shopId) {
        List<OwnerOrderDTO> ownerOrderDTOS = shopOrderService.listWaitingOrders(ownerId, shopId);
        return ResponseEntity.ok(ApiResponse.success(ownerOrderDTOS));
    }

}
