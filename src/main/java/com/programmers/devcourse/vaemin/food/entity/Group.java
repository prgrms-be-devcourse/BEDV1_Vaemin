package com.programmers.devcourse.vaemin.food.entity;

import com.programmers.devcourse.vaemin.root.IdentifiableEntity;
import com.programmers.devcourse.vaemin.shop.entity.Shop;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private final List<FoodGroup> includingFoods = new ArrayList<>();


    public void addFood(Food food) {
        FoodGroup addingFoodGroup = FoodGroup.builder()
                .food(food)
                .group(this).build();
        this.includingFoods.add(addingFoodGroup);
        food.getJoinedGroups().add(addingFoodGroup);
    }

    public void removeFood(Food food) {
        FoodGroup removingFoodGroup = this.includingFoods.stream()
                .filter(fg -> fg.getFood().equals(food))
                .findAny().orElseThrow(() -> new IllegalArgumentException("Not added food."));
        this.includingFoods.remove(removingFoodGroup);
        food.getJoinedGroups().remove(removingFoodGroup);
    }

    public void cleanup() {
        shop.getGroups().remove(this);
        includingFoods.forEach(foodGroup -> foodGroup.getFood().getJoinedGroups().remove(foodGroup));
    }

    public void changeName(@NonNull String name) {
        if(name.isBlank()) return;
        this.name = name;
    }


    @Builder
    public Group(String name, Shop shop) {
        this.name = name;
        this.shop = shop;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Group group = (Group) o;
        return id != null && Objects.equals(id, group.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, shop, includingFoods);
    }
}
