package com.programmers.devcourse.vaemin.root;

import lombok.Getter;

import javax.persistence.*;

@MappedSuperclass
@Getter
public class IdentifiableEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
}
