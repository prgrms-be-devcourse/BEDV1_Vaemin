package com.programmers.devcourse.vaemin.coupon.controller;

import com.programmers.devcourse.vaemin.coupon.entity.dto.CouponDTO;
import com.programmers.devcourse.vaemin.coupon.service.CustomerCouponService;
import com.programmers.devcourse.vaemin.root.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers/{customerId}/coupons")
public class CustomerCouponController {
    private final CustomerCouponService customerCouponService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CouponDTO>>> listCoupons(@PathVariable("customerId") long customerId) {
        List<CouponDTO> couponDTOS = customerCouponService.listCustomerCoupons(customerId);
        return ResponseEntity.ok(ApiResponse.success(couponDTOS));
    }

    @GetMapping("/{couponId}")
    public ResponseEntity<ApiResponse<CouponDTO>> readCoupon(@PathVariable("customerId") long customerId,
                                                             @PathVariable("couponId") long couponId) {
        CouponDTO couponDTO = customerCouponService.readCustomerCoupon(customerId, couponId);
        return ResponseEntity.ok(ApiResponse.success(couponDTO));
    }

    @PutMapping("/{couponId}")
    public ResponseEntity<ApiResponse<List<CouponDTO>>> addCoupon(@PathVariable("customerId") long customerId,
                                                                  @PathVariable("couponId") long couponId) {
        List<CouponDTO> couponDTOS = customerCouponService.acquireCoupon(customerId, couponId);
        return ResponseEntity.ok(ApiResponse.success(couponDTOS));
    }
}
