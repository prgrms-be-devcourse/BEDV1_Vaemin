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

    @PostMapping
    public ApiResponse<Long> createCategory(@RequestBody CategoryDto categoryDto) {
        return ApiResponse.success(categoryService.createCategory(categoryDto));
    }

    @DeleteMapping("/{categoryId}")
    public ApiResponse<Long> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ApiResponse.success(categoryId);
    }

    @PutMapping("/{categoryId}")
    public ApiResponse<Long> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDto categoryDto) {
        return ApiResponse.success(categoryService.updateCategory(categoryId, categoryDto));
    }
}
