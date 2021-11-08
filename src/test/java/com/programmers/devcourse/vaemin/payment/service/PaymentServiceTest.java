package com.programmers.devcourse.vaemin.payment.service;

import com.programmers.devcourse.vaemin.payment.dto.PaymentDto;
import com.programmers.devcourse.vaemin.payment.entity.Payment;
import com.programmers.devcourse.vaemin.payment.entity.PaymentStatus;
import com.programmers.devcourse.vaemin.payment.repository.PaymentRepository;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import com.programmers.devcourse.vaemin.user.customer.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PaymentServiceTest {
    @Autowired
    PaymentService paymentService;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    PaymentRepository paymentRepository;

    Long paymentId;
    Customer customer;
    Payment payment;

    @BeforeEach
    void save_test() {
        customer = customerRepository.save(Customer.builder()
                .userName("USERNAME")
                .email("email@domain.com")
                .phoneNum("010-1234-5678")
                .locationCode("LOCATION_CODE")
                .addressDetail("ADDR_DETAIL").build());
        payment = Payment.builder()
                .paymentStatus(PaymentStatus.REJECTED)
                .price(10000)
                .customer(customer).build();

        PaymentDto paymentDto = new PaymentDto(payment);

        // When
        paymentId = paymentService.createPayment(paymentDto);

        // Then
        assertThat(paymentRepository.count()).isEqualTo(1);
    }

    @AfterEach
    void tearDown() {
        paymentRepository.deleteAll();
    }

    @Test
    void findPaymentTest() {
        // When
        PaymentDto one = paymentService.findPayment(paymentId);

        // Then
        assertThat(one.getCustomer()).isEqualTo(customer);
        assertThat(one.getPrice()).isEqualTo(payment.getPrice());
        assertThat(one.getPaymentStatus()).isEqualTo(payment.getPaymentStatus());
    }
}