package com.programmers.devcourse.vaemin.shop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.programmers.devcourse.vaemin.review.dto.ReviewDto;
import com.programmers.devcourse.vaemin.review.entity.Review;
import com.programmers.devcourse.vaemin.shop.entity.*;
import com.programmers.devcourse.vaemin.user.owner.entity.Owner;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.List;

@Getter
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
    private final LocalTime openTime;

    @JsonProperty("closeTime")
    private final LocalTime closeTime;

    @Min(0)
    private final int deliveryFee;

    @Min(0)
    private final int minOrderPrice;

    @JsonProperty("shopStatus")
    private final ShopStatus shopStatus;

    @NotNull(message = "RegisterNumber is mandatory")
    private final String registerNumber;

    @NotNull(message = "Owner is mandatory")
    private final Owner owner;

    @JsonProperty("doroAddress")
    private final String doroAddress;

    @JsonProperty("doroIndex")
    private final int doroIndex;

    @JsonProperty("detailAddress")
    private final String detailAddress;

    @JsonProperty("reviews")
    private List<ReviewDto> reviews;

    public ShopDto(Shop shop) {
        this.id = shop.getId();
        this.name = shop.getName();
        this.phoneNum = shop.getPhoneNum();
        this.shortDescription = shop.getShortDescription();
        this.longDescription = shop.getLongDescription();
        this.supportedOrderType = shop.getSupportedOrderType();
        this.supportedPayment = shop.getSupportedPayment();
        this.openTime = shop.getOpenTime();
        this.closeTime = shop.getCloseTime();
        this.deliveryFee = shop.getDeliveryFee();
        this.minOrderPrice = shop.getMinOrderPrice();
        this.shopStatus = shop.getShopStatus();
        this.registerNumber = shop.getRegisterNumber();
        this.owner = shop.getOwner();
        this.doroAddress = shop.getDoroAddress();
        this.doroIndex = shop.getDoroIndex();
        this.detailAddress = shop.getDetailAddress();
        List<Review> reviewList = shop.getReviews();
        for(Review review : reviewList) {
            this.reviews.add(new ReviewDto(review));
        }
    }
}
