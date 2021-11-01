package com.programmers.devcourse.vaemin.shop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.programmers.devcourse.vaemin.shop.entity.DeliverySupportedRegions;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class DeliverySupportedRegionsDto {
    @JsonProperty("id")
    private final long id;

    @NotNull(message = "LocationCode is mandatory")
    private final String locationCode;

    public DeliverySupportedRegionsDto(DeliverySupportedRegions deliverySupportedRegions) {
        this.id = deliverySupportedRegions.getId();
        this.locationCode = deliverySupportedRegions.getLocationCode();
    }
}
