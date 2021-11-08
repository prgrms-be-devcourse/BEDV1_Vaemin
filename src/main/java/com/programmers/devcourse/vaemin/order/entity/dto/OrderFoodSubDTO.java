package com.programmers.devcourse.vaemin.order.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.programmers.devcourse.vaemin.food.entity.dto.FoodSubDTO;
import com.programmers.devcourse.vaemin.order.entity.OrderFoodSub;
import lombok.Getter;

@Getter
public class OrderFoodSubDTO {
    @JsonProperty("foodSub")
    private final FoodSubDTO foodSub;

    @JsonProperty("foodSubCount")
    private final int count;

    public OrderFoodSubDTO(OrderFoodSub orderFoodSub) {
        this.foodSub = new FoodSubDTO(orderFoodSub.getFoodSub());
        this.count = orderFoodSub.getFoodSubCount();
    }
}
