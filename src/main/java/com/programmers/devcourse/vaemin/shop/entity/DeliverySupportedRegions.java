package com.programmers.devcourse.vaemin.shop.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class DeliverySupportedRegions {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "location_code", nullable = false)
    private String locationCode;
    @ManyToOne
    @JoinColumn(name = "id")
    private Shop shop;
}
