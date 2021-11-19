package com.programmers.devcourse.vaemin.user.customer.service;

import com.programmers.devcourse.vaemin.root.exception.EntityExceptionSuppliers;
import com.programmers.devcourse.vaemin.user.customer.dto.CustomerCreateRequest;
import com.programmers.devcourse.vaemin.user.customer.dto.CustomerDeliveryAddressRequest;
import com.programmers.devcourse.vaemin.user.customer.dto.CustomerDeliveryAddressResponse;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import com.programmers.devcourse.vaemin.user.customer.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CustomerDeliveryAddressServiceTest {
    @Autowired
    private CustomerDeliveryAddressService addressService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    private static Customer setCustomer;

    @BeforeEach
    void createCustomer() {
        setCustomer = customerService.createCustomer(new CustomerCreateRequest(
                "set user",
                "setUser@gmail.com",
                "01000000000",
                "set location code",
                "set address detail"));
    }

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
    }

    @Test
    void getAddress() {
        // Given
        Long setCustomerId = setCustomer.getId();
        // When
        CustomerDeliveryAddressResponse response = addressService.getAddress(setCustomerId);
        // Then
        assertThat(response.getLocationCode()).isEqualTo(setCustomer.getCurrentAddress().getLocationCode());
        assertThat(response.getAddressDetail()).isEqualTo(setCustomer.getCurrentAddress().getAddressDetail());
    }

    @Test
    void updateAddress() {
        // Given
        Long setCustomerId = setCustomer.getId();
        // When
        CustomerDeliveryAddressResponse response = addressService.updateAddress(setCustomerId,
                new CustomerDeliveryAddressRequest(
                        "updated L.C",
                        "updated A.D"
                ));
        Customer updatedCustomer = customerRepository.findById(setCustomerId).orElseThrow(EntityExceptionSuppliers.customerNotFound);
        // Then
        assertThat(response.getLocationCode()).isEqualTo(updatedCustomer.getCurrentAddress().getLocationCode());
        assertThat(response.getAddressDetail()).isEqualTo(updatedCustomer.getCurrentAddress().getAddressDetail());
    }
}