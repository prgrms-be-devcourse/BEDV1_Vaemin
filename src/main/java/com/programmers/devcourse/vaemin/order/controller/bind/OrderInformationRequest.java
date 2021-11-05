package com.programmers.devcourse.vaemin.order.controller.bind;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderInformationRequest {
    private long customerId;
    private long shopId;
    private List<FoodItemRequest> foodItems = new ArrayList<>();
    private List<FoodSubItemRequest> foodSubItems = new ArrayList<>();
    private long appliedCouponId;
}
