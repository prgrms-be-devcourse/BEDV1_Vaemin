package com.programmers.devcourse.vaemin.user.customer.controller;

import com.programmers.devcourse.vaemin.root.ApiResponse;
import com.programmers.devcourse.vaemin.user.customer.dto.CustomerCreateRequest;
import com.programmers.devcourse.vaemin.user.customer.dto.CustomerDetailResponse;
import com.programmers.devcourse.vaemin.user.customer.dto.CustomerUpdateRequest;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import com.programmers.devcourse.vaemin.user.customer.service.CustomerService;
import javassist.NotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // question : @ExceptionHandler 어떻게 쓰지?
    @ExceptionHandler(NotFoundException.class)
    public ApiResponse<String> notFoundHandler (NotFoundException e) {
        return ApiResponse.fail(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<String> internalServerErrorHandler (Exception e) {
        return ApiResponse.fail(e.getMessage());
    }

    @PostMapping
    public ApiResponse<Customer> createCustomer(
            @RequestBody CustomerCreateRequest request
            ) {
        Customer newCustomer = customerService.createCustomer(request);
        return ApiResponse.success(newCustomer);
    }

    @GetMapping("/{customerId}")
    public ApiResponse<CustomerDetailResponse> getCustomer(
            @PathVariable Long customerId
    )  {
        CustomerDetailResponse customer = customerService.findOneCustomer(customerId);
        return ApiResponse.success(customer);
    }

    @PutMapping("/{customerId}")
    public ApiResponse<Customer> updateCustomer(
            @PathVariable Long customerId,
            @RequestBody CustomerUpdateRequest request
            )  {
        Customer customer = customerService.updateCustomer(customerId, request);
        return ApiResponse.success(customer);
    }

    @DeleteMapping("/{customerId}")
    public ApiResponse<Void> deleteCustomer(
            @PathVariable Long customerId
    ) {
        customerService.deleteCustomer(customerId);
        return ApiResponse.success();
    }
}
