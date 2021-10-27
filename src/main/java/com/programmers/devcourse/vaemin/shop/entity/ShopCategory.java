package com.programmers.devcourse.vaemin.shop.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class ShopCategory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shop_id", referencedColumnName = "id")
    private Shop shop;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;
}
