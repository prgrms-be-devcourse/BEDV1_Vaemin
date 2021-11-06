package com.programmers.devcourse.vaemin.user.customer.service;

import com.programmers.devcourse.vaemin.user.customer.dto.CustomerCreateRequest;
import com.programmers.devcourse.vaemin.user.customer.dto.CustomerDetailResponse;
import com.programmers.devcourse.vaemin.user.customer.dto.CustomerUpdateRequest;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import com.programmers.devcourse.vaemin.user.customer.entity.CustomerDeliveryAddress;
import com.programmers.devcourse.vaemin.user.customer.exception.CustomerExceptionSuppliers;
import com.programmers.devcourse.vaemin.user.customer.repository.CustomerDeliveryAddressRepository;
import com.programmers.devcourse.vaemin.user.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerDeliveryAddressRepository addressRepository;

    public CustomerService(CustomerRepository customerRepository, CustomerDeliveryAddressRepository addressRepository) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
    }

    @Transactional
    public Customer createCustomer(CustomerCreateRequest request) {
        Customer newCustomer = customerRepository.save(new Customer(request.getUserName(),
                request.getEmail(),
                request.getPhoneNum(),
                request.getLocationCode(),
                request.getAddressDetail()));
        addressRepository.save(
                new CustomerDeliveryAddress(
                        request.getLocationCode(),
                        request.getAddressDetail(),
                        newCustomer));
        return newCustomer;
    }

    @Transactional
    public CustomerDetailResponse findOneCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(CustomerExceptionSuppliers.customerNotFound);
        return new CustomerDetailResponse(customer);
    }

    @Transactional
    public Customer updateCustomer(Long customerId, CustomerUpdateRequest request) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(CustomerExceptionSuppliers.customerNotFound);
        customer.changeName(request.getUserName());
        customer.changeEmail(request.getEmail());
        customer.changePhoneNum(request.getPhoneNum());
        customer.changeUpdatedAt(request.getUpdatedAt());
        return customer;
    }

    @Transactional
    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }
}
