package com.programmers.devcourse.vaemin.food.controller.bind;

import com.programmers.devcourse.vaemin.food.entity.DiscountType;
import com.programmers.devcourse.vaemin.food.entity.FoodStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
public class FoodInformationRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String shortDescription;

    @Positive
    private int price;

    @NotNull
    private DiscountType discountType;

    @PositiveOrZero
    private int discountAmount;

    @NotNull
    private FoodStatus status;
}
