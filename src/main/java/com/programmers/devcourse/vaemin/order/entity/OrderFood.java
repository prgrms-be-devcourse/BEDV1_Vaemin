package com.programmers.devcourse.vaemin.order.entity;

import com.programmers.devcourse.vaemin.food.entity.Food;
import com.programmers.devcourse.vaemin.food.entity.FoodSub;
import com.programmers.devcourse.vaemin.root.IdentifiableEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class OrderFood extends IdentifiableEntity {
    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "food_id", referencedColumnName = "id")
    private Food food;

    @Column(name = "food_count", nullable = false)
    private int foodCount;

    @ManyToOne
    @JoinColumn(name = "food_sub_id", referencedColumnName = "id")
    private FoodSub foodSub;

    @Column(name = "food_sub_count", nullable = false)
    private int foodSubCount;
}
