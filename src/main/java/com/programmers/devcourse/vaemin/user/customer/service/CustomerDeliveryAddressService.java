package com.programmers.devcourse.vaemin.user.customer.service;

import com.programmers.devcourse.vaemin.root.exception.EntityExceptionSuppliers;
import com.programmers.devcourse.vaemin.user.customer.dto.CustomerDeliveryAddressRequest;
import com.programmers.devcourse.vaemin.user.customer.dto.CustomerDeliveryAddressResponse;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import com.programmers.devcourse.vaemin.user.customer.entity.CustomerDeliveryAddress;
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
        Customer customer = customerRepository.findById(customerId).orElseThrow(EntityExceptionSuppliers.customerNotFound);
        return new CustomerDeliveryAddressResponse(customer.getLocationCode(), customer.getAddressDetail());
    }

    @Transactional
    public List<CustomerDeliveryAddressResponse> getAddressList(Long customerId) {
        return addressRepository.findAllByCustomerId(customerId)
                .stream()
                .map(CustomerDeliveryAddressResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public CustomerDeliveryAddressResponse addAndUpdateAddress(Long customerId, CustomerDeliveryAddressRequest request) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(EntityExceptionSuppliers.customerNotFound);
        customer.changeLocationCode(request.getLocationCode());
        customer.changeAddressDetail(request.getAddressDetail());
        Customer updatedCustomer = customerRepository.save(customer);   // 주소 수정한 customer
        CustomerDeliveryAddress address = addressRepository.save(new CustomerDeliveryAddress(request.getLocationCode(), request.getAddressDetail(), updatedCustomer));
        return new CustomerDeliveryAddressResponse(address);
    }

    @Transactional
    public void deleteAddress(Long customerId, Long addressId) {
        CustomerDeliveryAddress address = addressRepository.findById(addressId).orElseThrow(EntityExceptionSuppliers.customerAddressNotFound);
        if (!customerId.equals(address.getCustomer().getId())) {
            throw new IllegalArgumentException("This customer has no access to address.");
        }
        Customer customer = customerRepository.findById(customerId).orElseThrow(EntityExceptionSuppliers.customerNotFound);
        if (customer.getLocationCode().equals(address.getLocationCode()) &&
                customer.getAddressDetail().equals(address.getAddressDetail())) {
            // 현재 customer에 설정된 주소면 exception
            throw new IllegalArgumentException("This address is customer's current address");
        }
        addressRepository.deleteById(addressId);
    }

    @Transactional
    public CustomerDeliveryAddressResponse changeAddress(Long customerId, Long addressId) {
        CustomerDeliveryAddress address = addressRepository.findById(addressId).orElseThrow(EntityExceptionSuppliers.customerAddressNotFound);
        Customer customer = customerRepository.findById(customerId).orElseThrow(EntityExceptionSuppliers.customerAddressNotFound);
        customer.changeLocationCode(address.getLocationCode());
        customer.changeAddressDetail(address.getAddressDetail());
        customerRepository.save(customer);
        return new CustomerDeliveryAddressResponse(address);
    }
}
