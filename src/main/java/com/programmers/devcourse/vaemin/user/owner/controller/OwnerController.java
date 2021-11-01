package com.programmers.devcourse.vaemin.user.owner.controller;

import com.programmers.devcourse.vaemin.root.ApiResponse;
import com.programmers.devcourse.vaemin.user.owner.dto.OwnerCreateRequest;
import com.programmers.devcourse.vaemin.user.owner.dto.OwnerDetailResponse;
import com.programmers.devcourse.vaemin.user.owner.dto.OwnerUpdateRequest;
import com.programmers.devcourse.vaemin.user.owner.entity.Owner;
import com.programmers.devcourse.vaemin.user.owner.service.OwnerService;
import javassist.NotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owners")
public class OwnerController {
    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
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
    public ApiResponse<Owner> createOwner(
            @RequestBody OwnerCreateRequest request
            ) {
        Owner newOwner = ownerService.createOwner(request);
        return ApiResponse.success(newOwner);
    }

    @GetMapping("/{ownerId}")
    public ApiResponse<OwnerDetailResponse> getOwner(
            @PathVariable Long ownerId
    )  {
        OwnerDetailResponse owner = ownerService.findOneOwner(ownerId);
        return ApiResponse.success(owner);
    }

    @PutMapping("/{ownerId}")
    public ApiResponse<Owner> updateCustomer(
            @PathVariable Long ownerId,
            @RequestBody OwnerUpdateRequest request
            )  {
        Owner owner = ownerService.updateOwner(ownerId, request);
        return ApiResponse.success(owner);
    }

    @DeleteMapping("/{ownerId}")
    public ApiResponse<Void> deleteOwner(
            @PathVariable Long ownerId
    ) {
        ownerService.deleteOwner(ownerId);
        return ApiResponse.success();
    }
}
