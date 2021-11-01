package com.programmers.devcourse.vaemin.food.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.programmers.devcourse.vaemin.food.entity.DiscountType;
import com.programmers.devcourse.vaemin.food.entity.Food;
import com.programmers.devcourse.vaemin.food.entity.FoodStatus;
import lombok.Getter;

@Getter
public class FoodDTO {
    @JsonProperty("id")
    private final long id;

    @JsonProperty("foodName")
    private final String name;

    @JsonProperty("shortDescription")
    private final String shortDescription;

    @JsonProperty("price")
    private final int price;

    @JsonProperty("discountType")
    private final DiscountType discountType;

    @JsonProperty("discountAmount")
    private final int amount;

    @JsonProperty("status")
    private final FoodStatus status;


    public FoodDTO(Food food) {
        this.id = food.getId();
        this.name = food.getName();
        this.shortDescription = food.getShortDescription();
        this.price = food.getPrice();
        this.discountType = food.getDiscountType();
        this.amount = food.getDiscountAmount();
        this.status = food.getStatus();
    }
}
