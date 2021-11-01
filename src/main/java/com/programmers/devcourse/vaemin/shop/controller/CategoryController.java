package com.programmers.devcourse.vaemin.shop.controller;

import com.programmers.devcourse.vaemin.root.ApiResponse;
import com.programmers.devcourse.vaemin.shop.dto.CategoryDto;
import com.programmers.devcourse.vaemin.shop.service.CategoryService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @ExceptionHandler(NotFoundException.class)
    public ApiResponse<String> notFoundHandler(NotFoundException e) {
        return ApiResponse.fail(404, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<String> internalServerErrorHandler(Exception e) {
        return ApiResponse.fail(500, e.getMessage());
    }
    
    @PostMapping
    public ApiResponse<Long> createCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable Long shopId, @PathVariable Long ownerId) {
        return ApiResponse.ok(categoryService.createCategory(categoryDto));
    }

    @DeleteMapping("/{categoryId}")
    public ApiResponse<Long> deleteCategory(@PathVariable Long categoryId, @PathVariable Long shopId, @PathVariable Long ownerId) {
        categoryService.deleteCategory(categoryId);
        return ApiResponse.ok(categoryId);
    }

    @PutMapping("/{categoryId}")
    public ApiResponse<Long> updateCategory(@PathVariable Long categoryId, @Valid @RequestBody CategoryDto categoryDto, @PathVariable Long shopId, @PathVariable Long ownerId) {
        return ApiResponse.ok(categoryService.updateCategory(categoryId, categoryDto));
    }
}
