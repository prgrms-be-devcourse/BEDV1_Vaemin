package com.programmers.devcourse.vaemin.coupon.repository;

import com.programmers.devcourse.vaemin.coupon.entity.CustomerCoupon;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerCouponRepository extends CrudRepository<CustomerCoupon, Long> {
    List<CustomerCoupon> findAllByCustomer(Customer customer);
}
