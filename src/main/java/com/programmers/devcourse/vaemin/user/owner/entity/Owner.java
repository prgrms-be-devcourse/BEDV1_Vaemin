package com.programmers.devcourse.vaemin.user.owner.entity;

import com.programmers.devcourse.vaemin.shop.entity.Shop;
import com.programmers.devcourse.vaemin.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Owner extends User {
    @OneToMany(mappedBy = "owner")
    private final List<Shop> shops = new ArrayList<>();

    public void addShop(Shop shop) {
        this.shops.add(shop);
    }

    @Builder
    public Owner(String username, String email, String phoneNum) {
        super(username, email, phoneNum);
    }
}
