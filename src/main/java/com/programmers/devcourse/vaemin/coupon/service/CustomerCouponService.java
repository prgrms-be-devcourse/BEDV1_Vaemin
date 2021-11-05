package com.programmers.devcourse.vaemin.coupon.service;

import com.programmers.devcourse.vaemin.coupon.entity.Coupon;
import com.programmers.devcourse.vaemin.coupon.entity.CustomerCoupon;
import com.programmers.devcourse.vaemin.coupon.entity.dto.CouponDTO;
import com.programmers.devcourse.vaemin.coupon.exception.CouponExceptionSuppliers;
import com.programmers.devcourse.vaemin.coupon.repository.CouponRepository;
import com.programmers.devcourse.vaemin.shop.entity.Shop;
import com.programmers.devcourse.vaemin.shop.exception.ShopExceptionSuppliers;
import com.programmers.devcourse.vaemin.shop.repository.ShopRepository;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import com.programmers.devcourse.vaemin.user.customer.exception.CustomerExceptionSuppliers;
import com.programmers.devcourse.vaemin.user.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerCouponService {
    private final CustomerRepository customerRepository;
    private final ShopRepository shopRepository;
    private final CouponRepository couponRepository;

    public CouponDTO readCustomerCoupon(long customerId, long couponId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(CustomerExceptionSuppliers.customerNotFound);
        Coupon coupon = customer.getCoupons().stream()
                .map(CustomerCoupon::getCoupon)
                .filter(c -> c.getId() == couponId)
                .findAny().orElseThrow(CouponExceptionSuppliers.noCouponFound);
        return new CouponDTO(coupon);
    }

    public List<CouponDTO> listCustomerCoupons(long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(CustomerExceptionSuppliers.customerNotFound);
        return customer.getCoupons().stream()
                .map(CustomerCoupon::getCoupon)
                .map(CouponDTO::new)
                .collect(Collectors.toList());
    }

    public List<CouponDTO> listAvailableCoupons(long customerId, long shopId) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(ShopExceptionSuppliers.shopNotFound);
        Customer customer = customerRepository.findById(customerId).orElseThrow(CustomerExceptionSuppliers.customerNotFound);
        return customer.getCoupons().stream()
                .map(CustomerCoupon::getCoupon)
                .filter(coupon -> coupon.getShop().equals(shop))
                .map(CouponDTO::new)
                .collect(Collectors.toList());
    }

    public List<CouponDTO> acquireCoupon(long customerId, long couponId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(CustomerExceptionSuppliers.customerNotFound);
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(CouponExceptionSuppliers.noCouponFound);
        customer.addCoupon(coupon);
        return customer.getCoupons().stream()
                .map(CustomerCoupon::getCoupon)
                .map(CouponDTO::new)
                .collect(Collectors.toList());
    }
}
