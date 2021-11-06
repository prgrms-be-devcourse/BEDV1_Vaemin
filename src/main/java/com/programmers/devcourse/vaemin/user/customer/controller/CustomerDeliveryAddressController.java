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

    @GetMapping("/list")
    public ApiResponse<List<CustomerDeliveryAddressResponse>> getAddressList(
            @PathVariable Long customerId
    ) {
        List<CustomerDeliveryAddressResponse> addresses = addressService.getAddressList(customerId);
        return ApiResponse.success(addresses);
    }

    @PostMapping
    public ApiResponse<CustomerDeliveryAddressResponse> addAndUpdateAddress(
            @PathVariable Long customerId,
            @RequestBody CustomerDeliveryAddressRequest request
            ) {
        CustomerDeliveryAddressResponse address = addressService.addAndUpdateAddress(customerId, request);
        return ApiResponse.success(address);
    }

    @DeleteMapping("/list/{addressId}")
    public ApiResponse<Object> deleteAddress(
            @PathVariable Long customerId,
            @PathVariable Long addressId
    ) {
        addressService.deleteAddress(customerId, addressId);
        return ApiResponse.success(null);
    }

}
