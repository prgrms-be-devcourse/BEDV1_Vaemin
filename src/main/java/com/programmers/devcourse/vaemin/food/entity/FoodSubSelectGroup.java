package com.programmers.devcourse.vaemin.food.entity;

import com.programmers.devcourse.vaemin.root.IdentifiableEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class FoodSubSelectGroup extends IdentifiableEntity {
    @Column(name = "group_name", nullable = false, length = 30)
    private String groupName;

    @Column(name = "multiselect", nullable = false)
    private boolean multiSelect;

    @Column(name = "required", nullable = false)
    private boolean required;
}
