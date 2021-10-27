package com.programmers.devcourse.vaemin.shop.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 20)
    private String name;
}
