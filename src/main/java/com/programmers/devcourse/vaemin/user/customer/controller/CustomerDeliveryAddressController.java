package com.programmers.devcourse.vaemin.user.customer.controller;

import com.programmers.devcourse.vaemin.root.ApiResponse;
import com.programmers.devcourse.vaemin.user.customer.dto.CustomerDeliveryAddressRequest;
import com.programmers.devcourse.vaemin.user.customer.dto.CustomerDeliveryAddressResponse;
import com.programmers.devcourse.vaemin.user.customer.service.CustomerDeliveryAddressService;
import javassist.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ApiResponse<CustomerDeliveryAddressResponse> getAddress(
            @PathVariable Long customerId
    ) {
        CustomerDeliveryAddressResponse address = addressService.getAddress(customerId);
        return ApiResponse.success(address);
    }

    @PostMapping
    public ApiResponse<CustomerDeliveryAddressResponse> updateAddress(
            @PathVariable Long customerId,
            @RequestBody CustomerDeliveryAddressRequest request
            ) {
        CustomerDeliveryAddressResponse address = addressService.updateAddress(customerId, request);
        return ApiResponse.success(address);
    }

    // TODO: 2021-11-19 changeAddress,  addAddress, getAddressList, deleteAddress

}
