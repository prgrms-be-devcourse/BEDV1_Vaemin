package com.programmers.devcourse.vaemin.food.controller.bind;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class FoodSubInformationRequest {
    @NotBlank
    private String name;

    @Positive
    private int price;

    @Positive
    private Long group; // id of food-sub's select group.
}
