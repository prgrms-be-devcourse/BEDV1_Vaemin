package com.programmers.devcourse.vaemin.user.customer.entity;

import com.programmers.devcourse.vaemin.user.User;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Customer extends User {
    @Column(name = "point", nullable = false)
    private int point; // mileage

    // question : 처음 생성했을 때 updatedAt이 null일텐데 왜 nullable = false?
    public Customer(String userName, String email, String phoneNum) {
        this.username = userName;
        this.email = email;
        this.phoneNum = phoneNum;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.point = 0;
    }

    public Customer() {

    }

    public void changeName(String userName) {
        this.username = userName;
    }

    public void changeEmail(String email) {
        this.email = email;
    }


    public void changePhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void changeUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
