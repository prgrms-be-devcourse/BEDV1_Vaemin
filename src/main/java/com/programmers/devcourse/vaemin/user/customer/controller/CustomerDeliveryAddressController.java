package com.programmers.devcourse.vaemin.user.customer.controller;

import com.programmers.devcourse.vaemin.root.ApiResponse;
import com.programmers.devcourse.vaemin.user.customer.dto.CustomerDeliveryAddressRequest;
import com.programmers.devcourse.vaemin.user.customer.entity.CustomerDeliveryAddress;
import com.programmers.devcourse.vaemin.user.customer.service.CustomerDeliveryAddressService;
import javassist.NotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers/{customerId}/address")
public class CustomerDeliveryAddressController {
    private CustomerDeliveryAddressService addressService;

    public CustomerDeliveryAddressController(CustomerDeliveryAddressService addressService) {
        this.addressService = addressService;
    }

    @ExceptionHandler(NotFoundException.class)
    public ApiResponse<String> notFoundHandler (NotFoundException e) {
        return ApiResponse.fail(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<String> internalServerErrorHandler (Exception e) {
        return ApiResponse.fail(e.getMessage());
    }

    @PostMapping
    public ApiResponse<CustomerDeliveryAddress> createCustomer(
            @PathVariable Long customerId,
            @RequestBody CustomerDeliveryAddressRequest request
    ) throws IllegalAccessException {
        CustomerDeliveryAddress address = addressService.createAddress(customerId, request);
        return ApiResponse.success(address);
    }
}
