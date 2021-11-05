package com.programmers.devcourse.vaemin.order.entity;

import com.programmers.devcourse.vaemin.food.entity.Food;
import com.programmers.devcourse.vaemin.root.IdentifiableEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "order_food")
public class OrderFood extends IdentifiableEntity {
    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "food_id", referencedColumnName = "id")
    private Food food;

    @Column(name = "food_count", nullable = false)
    private int foodCount;

    @Builder
    public OrderFood(Order order, Food food, int foodCount) {
        this.order = order;
        this.food = food;
        this.foodCount = foodCount;
    }
}
