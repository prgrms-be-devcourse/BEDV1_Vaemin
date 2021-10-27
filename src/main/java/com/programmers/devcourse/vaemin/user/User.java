package com.programmers.devcourse.vaemin.user;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "username", nullable = false, length = 50)
    protected String username;

    @Column(name = "email", nullable = false, length = 100)
    protected String email;

    @Column(name = "phone_num", nullable = false, length = 15)
    protected String phoneNum;

    @Column(name = "registered_at", nullable = false)
    protected LocalDateTime registeredAt;

    @Column(name = "updated_at", nullable = false)
    protected LocalDateTime updatedAt;
}
