package com.programmers.devcourse.vaemin.shop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.programmers.devcourse.vaemin.shop.entity.Category;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
public class CategoryDto {
    @JsonProperty("id")
    private final long id;

    @NotNull(message = "Name is mandatory")
    private final String name;
}
