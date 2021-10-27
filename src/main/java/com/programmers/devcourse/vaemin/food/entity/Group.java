package com.programmers.devcourse.vaemin.food.entity;

import com.programmers.devcourse.vaemin.root.IdentifiableEntity;

import javax.persistence.*;

@Entity
@Table(name = "food_group")
public class Group extends IdentifiableEntity {
    @Column(name = "group_name", nullable = false, length = 30)
    private String name;
}
