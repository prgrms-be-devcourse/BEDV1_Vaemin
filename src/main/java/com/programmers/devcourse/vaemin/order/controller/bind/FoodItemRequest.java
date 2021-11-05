package com.programmers.devcourse.vaemin.order.controller.bind;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Positive;

@Getter
@Setter
public class FoodItemRequest {
    private long foodItemId;
    @Positive
    private int count;
}
