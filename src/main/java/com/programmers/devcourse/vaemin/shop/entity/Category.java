package com.programmers.devcourse.vaemin.shop.entity;

import com.programmers.devcourse.vaemin.root.IdentifiableEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "category")
@NoArgsConstructor
public class Category extends IdentifiableEntity {
    @Column(name = "category_name", length = 20, unique = true)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private final List<ShopCategory> shopCategories = new ArrayList<>();

    public void changeName(@NonNull String name) {
        if(name.isBlank()) return;
        this.name = name;
    }

    @Builder
    public Category(String name) {
        this.name = name;
    }
}
