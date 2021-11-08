package com.programmers.devcourse.vaemin.order.service.util;

import com.programmers.devcourse.vaemin.food.entity.FoodSub;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FoodSubItemsContainer {
    private FoodSub foodSub;
    private int count;

    public int getTotalPrice() {
        return foodSub.getPrice() * count;
    }

    public FoodSubItemsContainer(FoodSub foodSub, int count) {
        if(count < 1) throw new IllegalArgumentException("Food sub with 0 cannot be included.");
        this.foodSub = foodSub;
        this.count = count;
    }
}
