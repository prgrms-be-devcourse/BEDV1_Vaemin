package com.programmers.devcourse.vaemin.root;

import javax.persistence.*;

@MappedSuperclass
public class IdentifiableEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // question : test코드 때문에 getter 만들었는데...
    public Long getId() {
        return id;
    }
}
