package com.programmers.devcourse.vaemin.order.entity;

import com.programmers.devcourse.vaemin.food.entity.FoodSub;
import com.programmers.devcourse.vaemin.root.IdentifiableEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "order_food_sub")
public class OrderFoodSub extends IdentifiableEntity {
    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "food_sub_id", referencedColumnName = "id")
    private FoodSub foodSub;

    @Column(name = "food_sub_count", nullable = false)
    private int foodSubCount;

    @Builder
    public OrderFoodSub(Order order, FoodSub foodSub, int foodSubCount) {
        this.order = order;
        this.foodSub = foodSub;
        this.foodSubCount = foodSubCount;
    }
}
