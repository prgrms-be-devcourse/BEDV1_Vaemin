package com.programmers.devcourse.vaemin.food.entity;

import com.programmers.devcourse.vaemin.root.IdentifiableEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "food_group_mapping")
@NoArgsConstructor
public class FoodGroup extends IdentifiableEntity {
    @ManyToOne(cascade = CascadeType.PERSIST)
    // if cascade to all, when food-group entity is deleted, food is deleted too.
    @JoinColumn(name = "food_id", referencedColumnName = "id")
    private Food food;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;


    @Builder
    public FoodGroup(Food food, Group group) {
        this.food = food;
        this.group = group;
    }
}
