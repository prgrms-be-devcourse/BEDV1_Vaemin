package com.programmers.devcourse.vaemin.shop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.programmers.devcourse.vaemin.shop.entity.DeliverySupportedRegions;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class DeliverySupportedRegionsDto {
    @JsonProperty("id")
    private long id;

    @NotNull(message = "LocationCode is mandatory")
    private String locationCode;

    public DeliverySupportedRegionsDto(DeliverySupportedRegions deliverySupportedRegions) {
        this.id = deliverySupportedRegions.getId();
        this.locationCode = deliverySupportedRegions.getLocationCode();
    }
}
