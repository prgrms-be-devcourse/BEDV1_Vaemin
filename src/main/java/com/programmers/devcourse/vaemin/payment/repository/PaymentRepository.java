package com.programmers.devcourse.vaemin.payment.repository;

import com.programmers.devcourse.vaemin.payment.entity.Payment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Long> {
}
