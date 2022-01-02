package com.programmers.devcourse.vaemin.order.service.util;

import com.programmers.devcourse.vaemin.food.entity.Food;
import com.programmers.devcourse.vaemin.food.entity.FoodSubSelectGroup;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class FoodItemsContainer {
    private Food food;
    private int foodCount;
    private Map<FoodSubSelectGroup, List<FoodSubItemsContainer>> foodSubs = new HashMap<>();

    public int getTotalPrice() {
        int foodPrice = food.getDiscountType().discount(food.getPrice(), food.getDiscountAmount()) * foodCount;
        int foodSubPrice = foodSubs.values().stream()
                .map(sub -> sub.stream()
                        .map(FoodSubItemsContainer::getTotalPrice)
                        .reduce(0, Integer::sum) // sum food subs of group
                )
                .reduce(0, Integer::sum); // sum of groups
        return foodPrice + foodSubPrice;
    }

    private void validateFoodSubSelectGroup() {
        food.getSubFoodGroups().forEach(group -> {
            List<FoodSubItemsContainer> subFoods = foodSubs.get(group);
            if (group.isRequired() && subFoods.isEmpty() ||
                    !group.isMultiSelect() && subFoods.size() > 1) {
                throw new IllegalArgumentException("Food sub's group constraints not met.");
            }
        });
    }

    public void validateFoodOrder() {
        validateFoodSubSelectGroup();
    }

    public FoodItemsContainer(Food food, int foodCount) {
        if(foodCount < 1) throw new IllegalArgumentException("Food must be included in order.");
        this.food = food;
        this.foodCount = foodCount;
    }
}
