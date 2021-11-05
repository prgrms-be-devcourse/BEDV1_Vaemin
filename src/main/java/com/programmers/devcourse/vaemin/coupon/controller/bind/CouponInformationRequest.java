package com.programmers.devcourse.vaemin.coupon.controller.bind;

import com.programmers.devcourse.vaemin.coupon.entity.CouponDiscountType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Getter
@Setter
public class CouponInformationRequest {
    @NotBlank
    private String name;

    @NotNull
    private CouponDiscountType discountType;

    @PositiveOrZero
    private int discountAmount;

    @PositiveOrZero
    private int minimumOrderPrice;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime expirationDate;
}
