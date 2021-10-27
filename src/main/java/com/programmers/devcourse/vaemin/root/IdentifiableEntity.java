package com.programmers.devcourse.vaemin.root;

import javax.persistence.*;

@MappedSuperclass
public class IdentifiableEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
}
