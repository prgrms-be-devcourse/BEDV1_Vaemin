package com.programmers.devcourse.vaemin.shop.entity;

import com.programmers.devcourse.vaemin.root.IdentifiableEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class ShopCategory extends IdentifiableEntity {
    @ManyToOne
    @JoinColumn(name = "shop_id", referencedColumnName = "id")
    private Shop shop;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;
}
