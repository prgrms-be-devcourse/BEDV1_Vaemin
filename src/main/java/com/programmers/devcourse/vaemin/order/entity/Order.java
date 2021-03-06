package com.programmers.devcourse.vaemin.order.entity;

import com.programmers.devcourse.vaemin.coupon.entity.Coupon;
import com.programmers.devcourse.vaemin.food.entity.Food;
import com.programmers.devcourse.vaemin.food.entity.FoodSub;
import com.programmers.devcourse.vaemin.payment.entity.Payment;
import com.programmers.devcourse.vaemin.review.entity.Review;
import com.programmers.devcourse.vaemin.root.AuditableEntity;
import com.programmers.devcourse.vaemin.shop.entity.Shop;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Table(name = "food_order")
@NoArgsConstructor
public class Order extends AuditableEntity {
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "shop_id", referencedColumnName = "id")
    private Shop shop;

    @OneToOne
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    private Payment payment;

    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "total_price", nullable = false)
    private int totalPrice;

    @OneToMany(mappedBy = "order")
    private final List<OrderFood> orderFoodItems = new ArrayList<>();

    @OneToOne(mappedBy = "order")
    private Review review;

    @OneToOne
    @JoinColumn(name = "applied_coupon_id", referencedColumnName = "id")
    private Coupon appliedCoupon;

    public void registerReview(Review review) {
        if(this.review != null) throw new IllegalArgumentException("Review already written.");
        this.review = review;
    }

    public void deleteReview() {
        this.review = null;
    }

    public void addFoodItems(Food food, int count, Map<FoodSub, Integer> foodSubs) {
        OrderFood orderFood = OrderFood.builder()
                .food(food)
                .foodCount(count)
                .order(this).build();
        foodSubs.forEach(orderFood::addFoodSub);
        this.orderFoodItems.add(orderFood);
    }

    public void changeCustomer(@NonNull Customer customer) {
        this.customer = customer;
    }

    public void changeOrderStatus(@NonNull OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void changeTotalPrice(int totalPrice) {
        if(totalPrice < 0) throw new IllegalArgumentException("Total price cannot be negative.");
        this.totalPrice = totalPrice;
    }

    public void changeAppliedCoupon(@NonNull Coupon coupon) {
        this.appliedCoupon = coupon;
    }


    @Builder
    public Order(Customer customer,
                 Shop shop,
                 Payment payment,
                 OrderStatus orderStatus,
                 int totalPrice,
                 Coupon appliedCoupon) {
        this.customer = customer;
        this.shop = shop;
        this.payment = payment;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.appliedCoupon = appliedCoupon;
    }
}
