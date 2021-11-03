package com.programmers.devcourse.vaemin.review.entity;

import com.programmers.devcourse.vaemin.order.entity.Order;
import com.programmers.devcourse.vaemin.root.AuditableEntity;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "review")
@NoArgsConstructor
public class Review extends AuditableEntity {
    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @Column(name = "text", nullable = false, length = 1024)
    private String text;

    @Column(name = "star_point", nullable = false)
    private int starPoint;

    public void changeText(@NonNull String text) {
        if(text.isBlank()) return;
        this.text = text;
    }

    public void changeStarPoint(@NonNull int starPoint) {
        if(starPoint < 0 || starPoint > 10) return;
        this.starPoint = starPoint;
    }

    @Builder
    public Review(
            Order order,
            Customer customer,
            String text,
            int starPoint) {
        this.order = order;
        this.customer = customer;
        this.text = text;
        this.starPoint = starPoint;
    }
}
