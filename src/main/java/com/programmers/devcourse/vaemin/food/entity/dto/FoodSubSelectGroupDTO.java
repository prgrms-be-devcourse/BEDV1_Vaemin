package com.programmers.devcourse.vaemin.food.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.programmers.devcourse.vaemin.food.entity.FoodSubSelectGroup;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

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


    // experimental usage of beanutils.
    public FoodSubSelectGroupDTO(FoodSubSelectGroup group) {
        BeanUtils.copyProperties(group, this);
    }
}
