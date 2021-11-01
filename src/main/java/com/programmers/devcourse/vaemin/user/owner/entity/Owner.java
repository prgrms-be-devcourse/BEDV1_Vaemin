package com.programmers.devcourse.vaemin.user.owner.entity;

import com.programmers.devcourse.vaemin.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor
public class Owner extends User {
    public Owner(String username, String email, String phoneNum) {
        super(username, email, phoneNum);
    }
}
