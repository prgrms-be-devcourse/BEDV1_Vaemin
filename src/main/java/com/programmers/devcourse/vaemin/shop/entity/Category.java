package com.programmers.devcourse.vaemin.shop.entity;

import com.programmers.devcourse.vaemin.root.IdentifiableEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Category extends IdentifiableEntity {
    @Column(name = "category_name", length = 20)
    private String name;
}
