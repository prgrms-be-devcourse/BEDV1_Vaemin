package com.programmers.devcourse.vaemin.user.customer.entity;

import com.programmers.devcourse.vaemin.user.User;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Customer extends User {
    @Column(name = "point", nullable = false)
    private int point; // mileage
}
