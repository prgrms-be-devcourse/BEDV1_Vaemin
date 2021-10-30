package com.programmers.devcourse.vaemin.shop.controller;

import com.programmers.devcourse.vaemin.root.ApiResponse;
import com.programmers.devcourse.vaemin.shop.dto.ShopDto;
import com.programmers.devcourse.vaemin.shop.service.ShopService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/owners/{ownerId}/shops")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @ExceptionHandler(NotFoundException.class)
    public ApiResponse<String> notFoundHandler(NotFoundException e) {
        return ApiResponse.fail(404, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<String> internalServerErrorHandler(Exception e) {
        return ApiResponse.fail(500, e.getMessage());
    }

    @PostMapping
    public ApiResponse<Long> createShop(@Valid @RequestBody ShopDto shopDto) {
        return ApiResponse.ok(shopService.createShop(shopDto));
    }

    @DeleteMapping("/{shopId}")
    public ApiResponse<Long> deleteShop(@Valid @RequestBody ShopDto shopDto) {
        return ApiResponse.ok(shopService.deleteShop(shopDto.getId()));
    }

    @GetMapping("/{shopId}")
    public ApiResponse<ShopDto> getShop(@PathVariable Long shopId) {
        ShopDto shop = shopService.findShop(shopId);
        return ApiResponse.ok(shop);
    }

    @PutMapping("/{shopId}")
    public ApiResponse<Long> updateShop(@PathVariable Long shopId, @Valid @RequestBody ShopDto shopDto) {
        return ApiResponse.ok(shopService.updateShop(shopId, shopDto));
    }
}