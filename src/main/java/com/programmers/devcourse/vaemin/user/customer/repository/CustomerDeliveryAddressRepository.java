package com.programmers.devcourse.vaemin.user.customer.repository;

import com.programmers.devcourse.vaemin.user.customer.entity.CustomerDeliveryAddress;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerDeliveryAddressRepository extends CrudRepository<CustomerDeliveryAddress, Long> {
    Optional<List<CustomerDeliveryAddress>> findAllByCustomerId(Long customerId);
}
