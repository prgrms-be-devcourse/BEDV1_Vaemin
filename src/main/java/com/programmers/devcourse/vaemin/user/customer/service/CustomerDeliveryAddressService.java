package com.programmers.devcourse.vaemin.user.customer.service;

import com.programmers.devcourse.vaemin.root.exception.EntityExceptionSuppliers;
import com.programmers.devcourse.vaemin.user.customer.dto.CustomerDeliveryAddressRequest;
import com.programmers.devcourse.vaemin.user.customer.dto.CustomerDeliveryAddressResponse;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import com.programmers.devcourse.vaemin.user.customer.entity.CustomerDeliveryAddress;
import com.programmers.devcourse.vaemin.user.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerDeliveryAddressService {
    private final CustomerRepository customerRepository;

    public CustomerDeliveryAddressService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public CustomerDeliveryAddressResponse getAddress(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(EntityExceptionSuppliers.customerNotFound);
        return new CustomerDeliveryAddressResponse(customer.getCurrentAddress().getLocationCode(), customer.getCurrentAddress().getLocationCode());
    }

    // TODO: 2021-11-19 getAllAddress , addAddress, deleteAddress, changeAddress

    @Transactional
    public CustomerDeliveryAddressResponse updateAddress(Long customerId, CustomerDeliveryAddressRequest request) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(EntityExceptionSuppliers.customerNotFound);
        CustomerDeliveryAddress newAddress = new CustomerDeliveryAddress(request.getLocationCode(), request.getAddressDetail());
        customer.changeAddress(newAddress);
        customerRepository.save(customer);   // 주소 수정한 customer
        return new CustomerDeliveryAddressResponse(newAddress);
    }

}
