package com.programmers.devcourse.vaemin.food.entity;

import com.programmers.devcourse.vaemin.root.IdentifiableEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "food_sub_select_group")
public class FoodSubSelectGroup extends IdentifiableEntity {
    @Column(name = "group_name", nullable = false, length = 30)
    private String groupName;

    @Column(name = "multiselect", nullable = false)
    private boolean multiSelect;

    @Column(name = "required", nullable = false)
    private boolean required;

    @ManyToOne
    @JoinColumn(name = "parent_food_id", referencedColumnName = "id")
    private Food parentFood;

    @OneToMany(mappedBy = "selectGroup", cascade = CascadeType.PERSIST) // orphan removal may delete food subs?
    private final List<FoodSub> foods = new ArrayList<>();


    public void includeFood(FoodSub foodSub) {
        this.foods.add(foodSub);
    }

    public void excludeFood(FoodSub foodSub) {
        this.foods.remove(foodSub);
    }

    public void changeGroupName(@NonNull String groupName) {
        this.groupName = groupName;
    }

    public void changeMultiSelect(boolean multiSelect) {
        this.multiSelect = multiSelect;
    }

    public void changeRequired(boolean required) {
        this.required = required;
    }


    @Builder
    public FoodSubSelectGroup(String groupName, boolean multiSelect, boolean required, Food food) {
        this.groupName = groupName;
        this.multiSelect = multiSelect;
        this.required = required;
        this.parentFood = food;
    }
}
