package com.programmers.devcourse.vaemin.shop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.programmers.devcourse.vaemin.review.dto.ReviewDto;
import com.programmers.devcourse.vaemin.review.entity.Review;
import com.programmers.devcourse.vaemin.shop.entity.*;
import com.programmers.devcourse.vaemin.user.owner.entity.Owner;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class ShopDto {
    @JsonProperty("id")
    private long id;

    @NotNull(message = "LocationCode is mandatory")
    private String name;

    @JsonProperty("phoneNum")
    private String phoneNum;

    @Range(min = 1, max = 100, message = "Short Description is between 0 and 100")
    private String shortDescription;

    @Range(min = 1, max = 1000, message = "Long Description is between 0 and 1000")
    private String longDescription;

    @JsonProperty("supportedOrderType")
    private ShopSupportedOrderType supportedOrderType;

    @JsonProperty("supportedPayment")
    private ShopSupportedPayment supportedPayment;

    @JsonProperty("openTime")
    private LocalTime openTime;

    @JsonProperty("closeTime")
    private LocalTime closeTime;

    @Min(0)
    private int deliveryFee;

    @Min(0)
    private int minOrderPrice;

    @JsonProperty("shopStatus")
    private ShopStatus shopStatus;

    @NotNull(message = "RegisterNumber is mandatory")
    private String registerNumber;

    @NotNull(message = "Owner is mandatory")
    private Owner owner;

    @JsonProperty("doroAddress")
    private String doroAddress;

    @JsonProperty("doroIndex")
    private int doroIndex;

    @JsonProperty("detailAddress")
    private String detailAddress;

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
