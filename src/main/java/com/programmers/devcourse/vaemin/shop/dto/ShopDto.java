package com.programmers.devcourse.vaemin.shop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.programmers.devcourse.vaemin.shop.entity.ShopStatus;
import com.programmers.devcourse.vaemin.shop.entity.ShopSupportedOrderType;
import com.programmers.devcourse.vaemin.shop.entity.ShopSupportedPayment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ShopDto {
    @JsonProperty("id")
    private final long id;

    @NotNull(message = "LocationCode is mandatory")
    private final String name;

    @JsonProperty("phoneNum")
    private final String phoneNum;

    @Range(min = 1, max = 100, message = "Short Description is between 0 and 100")
    private final String shortDescription;

    @Range(min = 1, max = 1000, message = "Long Description is between 0 and 1000")
    private final String longDescription;

    @JsonProperty("supportedOrderType")
    private final ShopSupportedOrderType supportedOrderType;

    @JsonProperty("supportedPayment")
    private final ShopSupportedPayment supportedPayment;

    @JsonProperty("openTime")
    private final LocalDateTime openTime;

    @JsonProperty("closeTime")
    private final LocalDateTime closeTime;

    @Min(0)
    private final int deliveryFee;

    @Min(0)
    private final int minOrderPrice;

    @JsonProperty("shopStatus")
    private final ShopStatus shopStatus;

    @NotNull(message = "RegisterNumber is mandatory")
    private final String registerNumber;
}
