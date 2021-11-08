package com.programmers.devcourse.vaemin.user.customer.repository;

import com.programmers.devcourse.vaemin.user.customer.entity.CustomerDeliveryAddress;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerDeliveryAddressRepository extends CrudRepository<CustomerDeliveryAddress, Long> {
    List<CustomerDeliveryAddress> findAllByCustomerId(Long customerId);
}
