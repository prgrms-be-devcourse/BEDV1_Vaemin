package com.programmers.devcourse.vaemin.food.controller.bind;

import com.programmers.devcourse.vaemin.food.entity.DiscountType;
import com.programmers.devcourse.vaemin.food.entity.FoodStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@NoArgsConstructor
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

    @Builder
    public FoodInformationRequest(String name, String shortDescription, int price, DiscountType discountType, int discountAmount, FoodStatus status) {
        this.name = name;
        this.shortDescription = shortDescription;
        this.price = price;
        this.discountType = discountType;
        this.discountAmount = discountAmount;
        this.status = status;
    }
}
