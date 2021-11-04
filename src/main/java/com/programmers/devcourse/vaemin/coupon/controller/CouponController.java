package com.programmers.devcourse.vaemin.coupon.controller;

import com.programmers.devcourse.vaemin.coupon.controller.bind.CouponInformationRequest;
import com.programmers.devcourse.vaemin.coupon.entity.dto.CouponDTO;
import com.programmers.devcourse.vaemin.coupon.service.CouponService;
import com.programmers.devcourse.vaemin.root.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/owners/{ownerId}/shops/{shopId}/coupons")
public class CouponController {
    private final CouponService couponService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CouponDTO>>> listCreatedCouponsOfShop(@PathVariable(name = "shopId") long shopId) {
        List<CouponDTO> couponDTOS = couponService.listCreatedCoupons(shopId);
        return ResponseEntity.ok(ApiResponse.success(couponDTOS));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CouponDTO>> createCoupon(@PathVariable("ownerId") long ownerId,
                                                               @PathVariable("shopId") long shopId,
                                                               CouponInformationRequest request) {
        CouponDTO coupon = couponService.createCoupon(shopId, request);
        return ResponseEntity
                .created(URI.create(String.format("/owners/%d/shops/%d/coupons/%d",
                        ownerId, shopId, coupon.getId())))
                .body(ApiResponse.success(coupon));
    }

    @GetMapping("/{couponId}")
    public ResponseEntity<ApiResponse<CouponDTO>> getCouponInformation(@PathVariable("shopId") long shopId,
                                                                        @PathVariable("couponId") long couponId) {
        CouponDTO couponDTO = couponService.readCoupon(shopId, couponId);
        return ResponseEntity.ok(ApiResponse.success(couponDTO));
    }

    @DeleteMapping("/{couponId}")
    public ResponseEntity<ApiResponse<List<CouponDTO>>> revokeCoupon(@PathVariable("shopId") long shopId,
                                                                     @PathVariable("couponId") long couponId) {
        List<CouponDTO> couponDTOS = couponService.deleteCoupon(shopId, couponId);
        return ResponseEntity.ok(ApiResponse.success(couponDTOS));
    }
}
