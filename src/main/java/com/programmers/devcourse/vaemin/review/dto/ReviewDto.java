package com.programmers.devcourse.vaemin.review.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.programmers.devcourse.vaemin.order.entity.Order;
import com.programmers.devcourse.vaemin.review.entity.Review;
import com.programmers.devcourse.vaemin.shop.entity.Shop;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Getter
public class ReviewDto {

    @JsonProperty("id")
    private final long id;

    @NotNull(message = "Order is mandatory")
    private final Order order;

    @NotNull(message = "Customer is mandatory")
    private final Customer customer;

    @NotNull(message = "Shop is mandatory")
    private final Shop shop;

    @NotNull(message = "Text is mandatory")
    private final String text;

    @Range(min=0, max=10)
    private final int starPoint;

    public ReviewDto(Review review) {
        this.id = review.getId();
        this.order = review.getOrder();
        this.customer = review.getCustomer();
        this.shop = review.getShop();
        this.text = review.getText();
        this.starPoint = review.getStarPoint();
    }
}
