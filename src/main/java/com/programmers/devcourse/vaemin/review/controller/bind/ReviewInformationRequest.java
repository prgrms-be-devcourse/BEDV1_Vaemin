package com.programmers.devcourse.vaemin.review.controller.bind;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class ReviewInformationRequest {
    @NotBlank
    private String text;
    @Positive
    private int starPoint;
}
