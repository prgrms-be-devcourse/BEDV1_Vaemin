package com.programmers.devcourse.vaemin.food.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.programmers.devcourse.vaemin.food.entity.FoodSub;
import lombok.Getter;

@Getter
public class FoodSubDTO {
    @JsonProperty("id")
    private final long id;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("price")
    private final int price;

    @JsonProperty("group")
    private final FoodSubSelectGroupDTO group;


    public FoodSubDTO(FoodSub foodSub) {
        this.id = foodSub.getId();
        this.name = foodSub.getName();
        this.price = foodSub.getPrice();
        this.group = new FoodSubSelectGroupDTO(foodSub.getSelectGroup());
    }
}
