package com.programmers.devcourse.vaemin.food.controller.bind;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class FoodSubSelectGroupInformationRequest {
    @NotBlank
    private String groupName;

    private boolean multiSelect;

    private boolean required;
}
