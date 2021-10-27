package com.programmers.devcourse.vaemin.user;

import com.programmers.devcourse.vaemin.root.AuditableEntity;
import lombok.Getter;

import javax.persistence.*;

@MappedSuperclass
@Getter
public class User extends AuditableEntity {
    @Column(name = "username", nullable = false, length = 50)
    protected String username;

    @Column(name = "email", nullable = false, length = 100)
    protected String email;

    @Column(name = "phone_num", nullable = false, length = 15)
    protected String phoneNum;
}
