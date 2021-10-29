package com.programmers.devcourse.vaemin.food.entity;

import com.programmers.devcourse.vaemin.root.IdentifiableEntity;
import com.programmers.devcourse.vaemin.shop.entity.Shop;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "food_group")
@Getter
@NoArgsConstructor
public class Group extends IdentifiableEntity {
    @Column(name = "group_name", nullable = false, length = 30)
    private String name;

    @ManyToOne
    @JoinColumn(name = "shop_id", referencedColumnName = "id")
    private Shop shop;

    @OneToMany(mappedBy = "group")
    private final List<FoodGroup> foodGroup = new ArrayList<>();


    public void changeName(@NonNull String name) {
        if(name.isBlank()) return;
        this.name = name;
    }


    @Builder
    public Group(String name, Shop shop) {
        this.name = name;
        this.shop = shop;
    }
}
