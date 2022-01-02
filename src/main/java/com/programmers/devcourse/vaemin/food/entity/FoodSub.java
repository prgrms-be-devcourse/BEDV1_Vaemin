package com.programmers.devcourse.vaemin.food.entity;

import com.programmers.devcourse.vaemin.root.AuditableEntity;
import com.programmers.devcourse.vaemin.shop.entity.Shop;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "food_sub")
public class FoodSub extends AuditableEntity {
    @Column(name = "food_sub_name", nullable = false, length = 30)
    private String name;

    @Column(name = "price", nullable = false)
    private int price;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "food_sub_group_id", referencedColumnName = "id")
    private FoodSubSelectGroup selectGroup;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "shop_id", referencedColumnName = "id")
    private Shop shop;

    @ManyToOne
    @JoinColumn(name = "parent_food_id", referencedColumnName = "id")
    private Food food;


    public void changeSelectGroup(FoodSubSelectGroup group) {
        this.selectGroup = group;
    }

    public void joinGroup(FoodSubSelectGroup group) {
        selectGroup = group;
        selectGroup.includeFood(this);
    }

    public void withdrawGroup() {
        if(selectGroup != null) {
            selectGroup.excludeFood(this);
        }
        selectGroup = null;
    }

    public void changeName(@NonNull String name) {
        if(name.isBlank()) return;
        this.name = name;
    }

    public void changePrice(int price) {
        if(price < 0) return;
        this.price = price;
    }

    public void changeGroup(@NonNull FoodSubSelectGroup selectGroup) {
        if(this.selectGroup != null) {
            this.selectGroup.excludeFood(this);
        }
        this.selectGroup = selectGroup;
        selectGroup.includeFood(this);
    }


    @Builder
    public FoodSub(String name, int price, FoodSubSelectGroup group, Shop shop, Food food) {
        this.name = name;
        this.price = price;
        this.selectGroup = group;
        if(group != null) {
            group.getFoods().add(this);
        }
        this.shop = shop;
        this.food = food;
        food.getSubFoods().add(this);
    }
}
