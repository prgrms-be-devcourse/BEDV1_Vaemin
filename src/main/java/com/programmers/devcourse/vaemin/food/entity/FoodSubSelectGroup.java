package com.programmers.devcourse.vaemin.food.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class FoodSubSelectGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_name", nullable = false, length = 30)
    private String groupName;

    @Column(name = "multiselect", nullable = false)
    private boolean multiSelect;

    @Column(name = "required", nullable = false)
    private boolean required;
}
