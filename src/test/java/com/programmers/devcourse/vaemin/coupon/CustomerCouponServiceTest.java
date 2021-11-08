package com.programmers.devcourse.vaemin.coupon;

import com.programmers.devcourse.vaemin.coupon.entity.Coupon;
import com.programmers.devcourse.vaemin.coupon.entity.CouponDiscountType;
import com.programmers.devcourse.vaemin.coupon.entity.dto.CouponDTO;
import com.programmers.devcourse.vaemin.coupon.repository.CouponRepository;
import com.programmers.devcourse.vaemin.coupon.service.CustomerCouponService;
import com.programmers.devcourse.vaemin.shop.entity.Shop;
import com.programmers.devcourse.vaemin.shop.entity.ShopStatus;
import com.programmers.devcourse.vaemin.shop.entity.ShopSupportedOrderType;
import com.programmers.devcourse.vaemin.shop.entity.ShopSupportedPayment;
import com.programmers.devcourse.vaemin.shop.repository.ShopRepository;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import com.programmers.devcourse.vaemin.user.customer.repository.CustomerRepository;
import com.programmers.devcourse.vaemin.user.owner.entity.Owner;
import com.programmers.devcourse.vaemin.user.owner.repository.OwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class CustomerCouponServiceTest {
    @Autowired
    CustomerCouponService customerCouponService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private ShopRepository shopRepository;

    Owner owner;
    Shop shop;
    Customer customer;
    Coupon coupon;

    @BeforeEach
    void init() {
        owner = ownerRepository.save(new Owner("USERNAME", "email@domain.com", "010-1234-5678"));
        shop = new Shop(
                "shop_test",
                "012-345-6789",
                "Shop for food service test.",
                "Shop for food service test.",
                ShopSupportedOrderType.BOTH,
                ShopSupportedPayment.CARD,
                LocalTime.now(),
                LocalTime.now(),
                2000,
                15000,
                ShopStatus.DEACTIVATED,
                "123-456-7890",
                owner,
                "Doro-ro",
                123,
                "Seoul");
        shopRepository.save(shop);
        customer = customerRepository.save(new Customer("USERNAME", "email@domain.com", "010-1234-5678", "location code", "address detail"));
        coupon = couponRepository.save(Coupon.builder()
                .name("COUPON_NAME")
                .discountType(CouponDiscountType.FIXED)
                .discountAmount(5000)
                .expirationDate(LocalDateTime.now())
                .minimumOrderPrice(15000)
                .shop(shop).build());
    }


    @Test
    @DisplayName("Add coupon to customer")
    void addCoupon() {
        List<CouponDTO> couponDTOS = customerCouponService.acquireCoupon(customer.getId(), coupon.getId());
        assertEquals(1, couponDTOS.size());
        assertEquals(coupon.getName(), couponDTOS.get(0).getName());
        assertEquals(coupon.getDiscountType(), couponDTOS.get(0).getDiscountType());
        assertEquals(coupon.getDiscountAmount(), couponDTOS.get(0).getDiscountAmount());
        assertEquals(coupon.getExpirationDate(), couponDTOS.get(0).getExpirationDate());
        assertEquals(coupon.getMinimumOrderPrice(), couponDTOS.get(0).getMinimumOrderPrice());
//        assertEquals(coupon.getShop().getId(), couponDTOS.get(0).getShop().getId());
    }

    @Test
    @DisplayName("List customer's coupons")
    void listAllCoupons() {
        customer.addCoupon(coupon);
        Coupon coupon2 = couponRepository.save(Coupon.builder()
                .name("COUPON_NAME2")
                .discountType(CouponDiscountType.PERCENTAGE)
                .discountAmount(5)
                .expirationDate(LocalDateTime.now())
                .minimumOrderPrice(5000)
                .shop(shop).build());
        customer.addCoupon(coupon2);

        List<CouponDTO> couponDTOS = customerCouponService.listCustomerCoupons(customer.getId());
        assertEquals(2, couponDTOS.size());
        assertTrue(couponDTOS.stream()
                .anyMatch(couponDTO -> couponDTO.getId() == coupon.getId() &&
                        couponDTO.getName().equals(coupon.getName()) &&
                        couponDTO.getDiscountType().equals(coupon.getDiscountType()) &&
                        couponDTO.getDiscountAmount() == coupon.getDiscountAmount() &&
                        couponDTO.getExpirationDate().equals(coupon.getExpirationDate()) &&
                        couponDTO.getMinimumOrderPrice() == coupon.getMinimumOrderPrice()));
        assertTrue(couponDTOS.stream()
                .anyMatch(couponDTO -> couponDTO.getId() == coupon2.getId() &&
                        couponDTO.getName().equals(coupon2.getName()) &&
                        couponDTO.getDiscountType().equals(coupon2.getDiscountType()) &&
                        couponDTO.getDiscountAmount() == coupon2.getDiscountAmount() &&
                        couponDTO.getExpirationDate().equals(coupon2.getExpirationDate()) &&
                        couponDTO.getMinimumOrderPrice() == coupon2.getMinimumOrderPrice()));
    }

    @Test
    @DisplayName("List available coupons")
    void listAvailableCoupons() {
        customer.addCoupon(coupon);

        Shop shop2 = new Shop(
                "shop_test2",
                "111-1111-1111",
                "Shop2 for food service test.",
                "Shop2 for food service test.",
                ShopSupportedOrderType.DELIVERY,
                ShopSupportedPayment.CARD,
                LocalTime.now(),
                LocalTime.now(),
                1000,
                10000,
                ShopStatus.PAUSED,
                "000-000-0000",
                owner,
                "Doro-ro",
                123,
                "Seoul");
        shopRepository.save(shop2);
        Coupon couponNotForShop = couponRepository.save(Coupon.builder()
                .shop(shop2)
                .name("COUPON_NOT_FOR_SHOP")
                .expirationDate(LocalDateTime.now())
                .minimumOrderPrice(5000)
                .discountType(CouponDiscountType.FIXED)
                .discountAmount(2500)
                .shop(shop2).build());
        shop2.getCoupons().add(couponNotForShop);
        customer.addCoupon(couponNotForShop);

        List<CouponDTO> couponDTOS = customerCouponService.listAvailableCoupons(customer.getId(), shop.getId());
        assertEquals(1, couponDTOS.size());
        assertEquals(coupon.getId(), couponDTOS.get(0).getId());
        assertEquals(coupon.getName(), couponDTOS.get(0).getName());
//        assertEquals(coupon.getShop().getId(), couponDTOS.get(0).getShop().getId());
        assertEquals(coupon.getDiscountType(), couponDTOS.get(0).getDiscountType());
        assertEquals(coupon.getDiscountAmount(), couponDTOS.get(0).getDiscountAmount());
        assertEquals(coupon.getMinimumOrderPrice(), couponDTOS.get(0).getMinimumOrderPrice());
        assertEquals(coupon.getExpirationDate(), couponDTOS.get(0).getExpirationDate());
    }
}
