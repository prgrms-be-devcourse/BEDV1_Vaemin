package com.programmers.devcourse.vaemin.coupon.service;

import com.programmers.devcourse.vaemin.coupon.controller.bind.CouponInformationRequest;
import com.programmers.devcourse.vaemin.coupon.entity.Coupon;
import com.programmers.devcourse.vaemin.coupon.entity.dto.CouponDTO;
import com.programmers.devcourse.vaemin.coupon.repository.CouponRepository;
import com.programmers.devcourse.vaemin.root.exception.EntityExceptionSuppliers;
import com.programmers.devcourse.vaemin.shop.entity.Shop;
import com.programmers.devcourse.vaemin.shop.repository.ShopRepository;
import com.programmers.devcourse.vaemin.user.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponService {
    private final ShopRepository shopRepository;
    private final CouponRepository couponRepository;
    private final CustomerRepository customerRepository;

    public CouponDTO readCoupon(long shopId, long couponId) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(EntityExceptionSuppliers.shopNotFound);
        Coupon coupon = shop.getCoupons().stream()
                .filter(c -> c.getId() == couponId)
                .findAny().orElseThrow(EntityExceptionSuppliers.noCouponFound);
        return new CouponDTO(coupon);
    }

    public CouponDTO createCoupon(long shopId, CouponInformationRequest request) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(EntityExceptionSuppliers.shopNotFound);
        Coupon coupon = couponRepository.save(Coupon.builder()
                .name(request.getName())
                .discountType(request.getDiscountType())
                .discountAmount(request.getDiscountAmount())
                .minimumOrderPrice(request.getMinimumOrderPrice())
                .expirationDate(request.getExpirationDate())
                .shop(shop).build());
        shop.getCoupons().add(coupon);
        return new CouponDTO(coupon);
    }

    public CouponDTO updateCoupon(long couponId, CouponInformationRequest request) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(EntityExceptionSuppliers.noCouponFound);
        coupon.changeName(request.getName());
        coupon.changeDiscountType(request.getDiscountType());
        coupon.changeDiscountAmount(request.getDiscountAmount());
        coupon.changeExpirationDate(request.getExpirationDate());
        coupon.changeMinimumOrderPrice(request.getMinimumOrderPrice());
        return new CouponDTO(coupon);
    }

    public List<CouponDTO> deleteCoupon(long shopId, long couponId) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(EntityExceptionSuppliers.shopNotFound);
        Coupon coupon = shop.getCoupons().stream()
                .filter(c -> c.getId() == couponId)
                .findAny().orElseThrow(EntityExceptionSuppliers.noCouponFound);
        shop.getCoupons().remove(coupon);
        couponRepository.delete(coupon);
        return shop.getCoupons().stream()
                .map(CouponDTO::new)
                .collect(Collectors.toList());
    }

    public List<CouponDTO> listCreatedCoupons(long shopId) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(EntityExceptionSuppliers.shopNotFound);
        return shop.getCoupons().stream()
                .map(CouponDTO::new)
                .collect(Collectors.toList());
    }
}
