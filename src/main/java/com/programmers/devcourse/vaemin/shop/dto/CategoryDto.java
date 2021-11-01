package com.programmers.devcourse.vaemin.shop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.programmers.devcourse.vaemin.shop.entity.Category;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class CategoryDto {
    @JsonProperty("id")
    private final long id;

    @NotNull(message = "Name is mandatory")
    private final String name;

    public CategoryDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }
}
