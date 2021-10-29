package com.programmers.devcourse.vaemin.food.controller.bind;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class FoodGroupInformationRequest {
    @NotBlank
    private String name;
}
