package com.programmers.devcourse.vaemin.user.customer.service;

import com.programmers.devcourse.vaemin.user.customer.dto.CustomerCreateRequest;
import com.programmers.devcourse.vaemin.user.customer.dto.CustomerDetailResponse;
import com.programmers.devcourse.vaemin.user.customer.dto.CustomerUpdateRequest;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import com.programmers.devcourse.vaemin.user.customer.exception.CustomerExceptionSuppliers;
import com.programmers.devcourse.vaemin.user.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // question : 반환타입은 뭘로 해야하나? (멘토님이 update같은 경우는 객체를 반환하는 게 좋다고 하던데)
    @Transactional
    public Customer createCustomer(CustomerCreateRequest request) {
        Customer newCustomer = new Customer(request.getUserName(),
                request.getEmail(),
                request.getPhoneNum());
        return customerRepository.save(newCustomer);
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
