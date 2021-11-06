package com.programmers.devcourse.vaemin.user.customer.service;

import com.programmers.devcourse.vaemin.user.customer.dto.CustomerDeliveryAddressRequest;
import com.programmers.devcourse.vaemin.user.customer.dto.CustomerDeliveryAddressResponse;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import com.programmers.devcourse.vaemin.user.customer.entity.CustomerDeliveryAddress;
import com.programmers.devcourse.vaemin.user.customer.exception.CustomerAddressExceptionSuppliers;
import com.programmers.devcourse.vaemin.user.customer.exception.CustomerExceptionSuppliers;
import com.programmers.devcourse.vaemin.user.customer.repository.CustomerDeliveryAddressRepository;
import com.programmers.devcourse.vaemin.user.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerDeliveryAddressService {
    private final CustomerDeliveryAddressRepository addressRepository;
    // question : customerRepository를 가져오는 게 낫나 customerService를 가져오는 게 낫나?
    private final CustomerRepository customerRepository;

    public CustomerDeliveryAddressService(CustomerDeliveryAddressRepository customerDeliveryAddressRepository, CustomerRepository customerRepository) {
        this.addressRepository = customerDeliveryAddressRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public CustomerDeliveryAddressResponse getAddress(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(CustomerExceptionSuppliers.customerNotFound);
        return new CustomerDeliveryAddressResponse(customer.getLocationCode(), customer.getAddressDetail());
    }

    @Transactional
    public List<CustomerDeliveryAddressResponse> getAddressList(Long customerId) {
        List<CustomerDeliveryAddressResponse> addresses = addressRepository.findAllByCustomerId(customerId)
                .orElseThrow(CustomerAddressExceptionSuppliers.customerAddressNotFound)
                .stream()
                .map(CustomerDeliveryAddressResponse::new)
                .collect(Collectors.toList());
        return addresses;
    }

    @Transactional
    public CustomerDeliveryAddressResponse addAndUpdateAddress(Long customerId, CustomerDeliveryAddressRequest request) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(CustomerExceptionSuppliers.customerNotFound);
        customer.changeLocationCode(request.getLocationCode());
        customer.changeAddressDetail(request.getAddressDetail());
        Customer updatedCustomer = customerRepository.save(customer);   // 주소 수정한 customer
        CustomerDeliveryAddress address = addressRepository.save(new CustomerDeliveryAddress(request.getLocationCode(), request.getAddressDetail(), updatedCustomer));
        return new CustomerDeliveryAddressResponse(address);
    }

}
