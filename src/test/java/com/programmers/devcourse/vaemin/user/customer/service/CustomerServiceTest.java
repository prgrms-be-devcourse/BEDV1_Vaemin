package com.programmers.devcourse.vaemin.user.customer.service;

import com.programmers.devcourse.vaemin.user.customer.dto.CustomerCreateRequest;
import com.programmers.devcourse.vaemin.user.customer.dto.CustomerDetailResponse;
import com.programmers.devcourse.vaemin.user.customer.dto.CustomerUpdateRequest;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import com.programmers.devcourse.vaemin.user.customer.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class CustomerServiceTest {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRepository customerRepository;

    private static Customer customer1;

    @BeforeEach
    void createCustomer() {
        CustomerCreateRequest request = new CustomerCreateRequest("customer1", "customer1@naver.com", "01000000000", "123123", "동동시 동동구 동동로 1길 11");
        customer1 = customerService.createCustomer(request);
        assertThat(customer1.getUsername()).isEqualTo("customer1");
    }

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
    }

    @Test
    void findOneCustomer() {
        // Given
        Long customerId = customer1.getId();
        // When
        CustomerDetailResponse response = customerService.findOneCustomer(customerId);
        // Then
        assertThat(response.getUserName()).isEqualTo(customer1.getUsername());
    }

    @Test
    void updateCustomer() {
        // Given
        Long customerId = customer1.getId();
        CustomerUpdateRequest request = new CustomerUpdateRequest("updated customer1", "updatedCustomer1@naver.com", "01000000000");
        // When
        Customer updateCustomer = customerService.updateCustomer(customerId, request);
        CustomerDetailResponse oneCustomer = customerService.findOneCustomer(customerId);
        // Then
        assertThat(updateCustomer.getUsername()).isEqualTo(oneCustomer.getUserName());
    }

    @Test
    void deleteCustomer() {
        // Given
        Long customer1Id = customer1.getId();
        // When
        customerService.deleteCustomer(customer1Id);
        // Then
        assertFalse(customerRepository.existsById(customer1Id));
    }
}