package com.programmers.devcourse.vaemin.shop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
public class DeliverySupportedRegionsDto {
    @JsonProperty("id")
    private final long id;

    @NotNull(message = "LocationCode is mandatory")
    private final String locationCode;
}
