package com.programmers.devcourse.vaemin.order.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.programmers.devcourse.vaemin.food.entity.dto.FoodDTO;
import com.programmers.devcourse.vaemin.order.entity.OrderFood;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class OrderFoodDTO {
    @JsonProperty("food")
    private final FoodDTO foodDTO;

    @JsonProperty("foodCount")
    private final int foodCount;

    @JsonProperty("foodSub")
    private final List<OrderFoodSubDTO> foodSubDTOs = new ArrayList<>();

    public OrderFoodDTO(OrderFood orderFood) {
        this.foodDTO = new FoodDTO(orderFood.getFood());
        this.foodCount = orderFood.getFoodCount();
        orderFood.getFoodSubs().forEach(orderFoodSub -> foodSubDTOs.add(new OrderFoodSubDTO(orderFoodSub)));
    }
}
