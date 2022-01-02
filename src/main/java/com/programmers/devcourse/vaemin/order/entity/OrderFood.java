package com.programmers.devcourse.vaemin.order.entity;

import com.programmers.devcourse.vaemin.food.entity.Food;
import com.programmers.devcourse.vaemin.food.entity.FoodSub;
import com.programmers.devcourse.vaemin.root.IdentifiableEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "orderFood", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<OrderFoodSub> foodSubs = new ArrayList<>();


    public void addFoodSub(FoodSub foodSub, int count) {
        OrderFoodSub orderFoodSub = OrderFoodSub.builder()
                .order(order)
                .orderFood(this)
                .foodSub(foodSub)
                .foodSubCount(count).build();
        foodSubs.add(orderFoodSub);
    }

    @Builder
    public OrderFood(Order order, Food food, int foodCount) {
        this.order = order;
        this.food = food;
        this.foodCount = foodCount;
    }
}
