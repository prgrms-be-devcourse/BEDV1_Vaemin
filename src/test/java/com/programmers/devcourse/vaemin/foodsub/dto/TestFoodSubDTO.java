package com.programmers.devcourse.vaemin.foodsub.dto;

public class TestFoodSubDTO {
    private long id;

    private String name;

    private int price;

    private TestFoodSubSelectGroupDTO group;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public TestFoodSubSelectGroupDTO getGroup() {
        return group;
    }

    public void setGroup(TestFoodSubSelectGroupDTO group) {
        this.group = group;
    }
}
