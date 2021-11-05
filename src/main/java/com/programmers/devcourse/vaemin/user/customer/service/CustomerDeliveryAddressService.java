package com.programmers.devcourse.vaemin.user.customer.service;

import com.programmers.devcourse.vaemin.user.customer.dto.CustomerDeliveryAddressRequest;
import com.programmers.devcourse.vaemin.user.customer.entity.CustomerDeliveryAddress;
import com.programmers.devcourse.vaemin.user.customer.repository.CustomerDeliveryAddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerDeliveryAddressService {
    private final CustomerDeliveryAddressRepository addressRepository;

    public CustomerDeliveryAddressService(CustomerDeliveryAddressRepository customerDeliveryAddressRepository) {
        this.addressRepository = customerDeliveryAddressRepository;
    }

    @Transactional
    public CustomerDeliveryAddress createAddress(Long customerId, CustomerDeliveryAddressRequest request) throws IllegalAccessException {
        if (!customerId.equals(request.getCustomer().getId())) {
            throw new IllegalAccessException("This Customer doesn't match with this request");
        }
        CustomerDeliveryAddress address = new CustomerDeliveryAddress(request.getLocationCode(),
                request.getAddressDetail(),
                request.getCustomer().getUsername());
        return addressRepository.save(address);
    }
}
