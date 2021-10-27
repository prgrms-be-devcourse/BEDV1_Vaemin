package com.programmers.devcourse.vaemin.food.entity;

import javax.persistence.*;

@Entity
public class Group {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;
}
