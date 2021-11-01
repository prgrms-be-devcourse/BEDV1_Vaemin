package com.programmers.devcourse.vaemin.user.customer.repository;

import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
