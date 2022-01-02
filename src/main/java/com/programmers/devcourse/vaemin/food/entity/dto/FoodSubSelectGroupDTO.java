package com.programmers.devcourse.vaemin.food.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.programmers.devcourse.vaemin.food.entity.FoodSubSelectGroup;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FoodSubSelectGroupDTO {
    @JsonProperty("id")
    private long id;

    @JsonProperty("groupName")
    private String groupName;

    @JsonProperty("multiselect")
    private boolean multiselect;

    @JsonProperty("required")
    private boolean required;


    public FoodSubSelectGroupDTO(FoodSubSelectGroup group) {
        this.id = group.getId();
        this.groupName = group.getGroupName();
        this.multiselect = group.isMultiSelect();
        this.required = group.isRequired();
    }
}
