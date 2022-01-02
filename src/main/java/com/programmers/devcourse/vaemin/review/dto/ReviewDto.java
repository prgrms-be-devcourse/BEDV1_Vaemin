package com.programmers.devcourse.vaemin.review.dto;

import com.programmers.devcourse.vaemin.order.entity.dto.CustomerOrderDTO;
import com.programmers.devcourse.vaemin.review.entity.Review;
import com.programmers.devcourse.vaemin.user.customer.dto.CustomerDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class ReviewDto {
    private long id;

    @NotNull(message = "Order is mandatory")
    private CustomerOrderDTO order;

    @NotNull(message = "Customer is mandatory")
    private CustomerDTO customer;

    @NotNull(message = "Text is mandatory")
    private String text;

    @Range(min=0, max=10)
    private int starPoint;

    public ReviewDto(Review review) {
        this.id = review.getId();
        this.order = new CustomerOrderDTO(review.getOrder());
        this.customer = new CustomerDTO(review.getCustomer());
        this.text = review.getText();
        this.starPoint = review.getStarPoint();
    }
}
