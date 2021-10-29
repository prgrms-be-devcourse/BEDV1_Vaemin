package com.programmers.devcourse.vaemin.food.entity;

import com.programmers.devcourse.vaemin.root.IdentifiableEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Getter
@Table(name = "food_group_mapping")
@NoArgsConstructor
public class FoodGroup extends IdentifiableEntity {
    @ManyToOne
    @JoinColumn(name = "food_id", referencedColumnName = "id")
    private Food food;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;


    @Builder
    public FoodGroup(Food food, Group group) {
        this.food = food;
        this.group = group;
    }
}
