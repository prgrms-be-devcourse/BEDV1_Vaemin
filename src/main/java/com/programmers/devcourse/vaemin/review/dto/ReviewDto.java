package com.programmers.devcourse.vaemin.review.dto;

import com.programmers.devcourse.vaemin.order.entity.Order;
import com.programmers.devcourse.vaemin.review.entity.Review;
import com.programmers.devcourse.vaemin.shop.entity.Shop;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class ReviewDto {

    @NotNull(message = "Order is mandatory")
    private Order order;

    @NotNull(message = "Customer is mandatory")
    private Customer customer;

    @NotNull(message = "Shop is mandatory")
    private Shop shop;

    @NotNull(message = "Text is mandatory")
    private String text;

    @Range(min=0, max=10)
    private int starPoint;

    public ReviewDto(Review review) {
        this.order = review.getOrder();
        this.customer = review.getCustomer();
        this.shop = review.getShop();
        this.text = review.getText();
        this.starPoint = review.getStarPoint();
    }
}
