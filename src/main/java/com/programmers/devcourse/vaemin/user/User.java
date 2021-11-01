package com.programmers.devcourse.vaemin.user;

import com.programmers.devcourse.vaemin.root.AuditableEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@NoArgsConstructor
@Getter
public class User extends AuditableEntity {
    @Column(name = "username", nullable = false, length = 50)
    protected String username;

    @Column(name = "email", nullable = false, length = 100)
    protected String email;

    @Column(name = "phone_num", nullable = false, length = 15)
    protected String phoneNum;


    public User(String username, String email, String phoneNum) {
        this.username = username;
        this.email = email;
        this.phoneNum = phoneNum;
    }

    public void changeName(@NonNull String name) {
        if(name.isBlank()) throw new IllegalArgumentException("Username cannot be blank.");
        this.username = name;
    }

    public void changeEmail(@NonNull String email) {
        if(email.isBlank()) throw new IllegalArgumentException("Email cannot be blank.");
        this.email = email;
    }

    public void changePhoneNum(@NonNull String phoneNum) {
        if(phoneNum.isBlank()) throw new IllegalArgumentException("Phone number cannot be blank.");
        this.phoneNum = phoneNum;
    }

    public void changeUpdatedAt(@NonNull LocalDateTime time) {
        this.updatedAt = time;
    }
}
