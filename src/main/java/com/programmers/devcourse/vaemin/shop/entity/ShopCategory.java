package com.programmers.devcourse.vaemin.shop.entity;

import com.programmers.devcourse.vaemin.root.IdentifiableEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "shop_category")
@NoArgsConstructor
public class ShopCategory extends IdentifiableEntity {
    @ManyToOne
    @JoinColumn(name = "shop_id", referencedColumnName = "id")
    private Shop shop;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @Builder
    public ShopCategory(Shop shop, Category category) {
        this.shop = shop;
        this.category = category;
    }
}
