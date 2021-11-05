package com.programmers.devcourse.vaemin.shop.dto;

import com.programmers.devcourse.vaemin.shop.entity.Category;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CategoryDto {
    @NotNull(message = "Name is mandatory")
    private String name;

    public CategoryDto(Category category) {
        this.name = category.getName();
    }

    public CategoryDto(String name) {
        this.name = name;
    }
}
