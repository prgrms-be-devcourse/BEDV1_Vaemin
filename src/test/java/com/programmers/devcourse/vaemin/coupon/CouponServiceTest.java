package com.programmers.devcourse.vaemin.coupon;

import com.programmers.devcourse.vaemin.coupon.controller.bind.CouponInformationRequest;
import com.programmers.devcourse.vaemin.coupon.entity.Coupon;
import com.programmers.devcourse.vaemin.coupon.entity.CouponDiscountType;
import com.programmers.devcourse.vaemin.coupon.entity.dto.CouponDTO;
import com.programmers.devcourse.vaemin.coupon.repository.CouponRepository;
import com.programmers.devcourse.vaemin.coupon.service.CouponService;
import com.programmers.devcourse.vaemin.shop.entity.Shop;
import com.programmers.devcourse.vaemin.shop.entity.ShopStatus;
import com.programmers.devcourse.vaemin.shop.entity.ShopSupportedOrderType;
import com.programmers.devcourse.vaemin.shop.entity.ShopSupportedPayment;
import com.programmers.devcourse.vaemin.shop.repository.ShopRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CouponServiceTest {
    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private ShopRepository shopRepository;

    Owner owner;
    Shop shop;

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
    }

    @Test
    @DisplayName("Coupon create test.")
    void createCoupon() {
        CouponInformationRequest request = new CouponInformationRequest();
        request.setName("COUPON_NAME");
        request.setDiscountType(CouponDiscountType.FIXED);
        request.setDiscountAmount(5000);
        request.setExpirationDate(LocalDateTime.now());
        request.setMinimumOrderPrice(15000);
        CouponDTO coupon = couponService.createCoupon(shop.getId(), request);
        assertEquals(request.getName(), coupon.getName());
        assertEquals(request.getDiscountType(), coupon.getDiscountType());
        assertEquals(request.getDiscountAmount(), coupon.getDiscountAmount());
        assertEquals(request.getExpirationDate(), coupon.getExpirationDate());
        assertEquals(request.getMinimumOrderPrice(), coupon.getMinimumOrderPrice());
        assertEquals(shop.getId(), coupon.getShop().getId());
    }

    @Test
    @DisplayName("Coupon read test.")
    void readCoupon() {
        LocalDateTime expirationDate = LocalDateTime.now();
        Coupon coupon = couponRepository.save(Coupon.builder()
                .name("COUPON_NAME")
                .discountType(CouponDiscountType.FIXED)
                .discountAmount(5000)
                .expirationDate(expirationDate)
                .minimumOrderPrice(15000)
                .shop(shop).build());
        shop.getCoupons().add(coupon);

        CouponDTO couponDTO = couponService.readCoupon(shop.getId(), coupon.getId());
        assertEquals("COUPON_NAME", couponDTO.getName());
        assertEquals(CouponDiscountType.FIXED, couponDTO.getDiscountType());
        assertEquals(5000, couponDTO.getDiscountAmount());
        assertEquals(expirationDate, couponDTO.getExpirationDate());
        assertEquals(15000, couponDTO.getMinimumOrderPrice());
        assertEquals(shop.getId(), couponDTO.getShop().getId());
    }

    @Test
    @DisplayName("Coupon update test.")
    void updateCoupon() {
        Coupon coupon = couponRepository.save(Coupon.builder()
                .name("COUPON_NAME")
                .discountType(CouponDiscountType.FIXED)
                .discountAmount(5000)
                .expirationDate(LocalDateTime.of(1970,1,1,1,1,1))
                .minimumOrderPrice(15000)
                .shop(shop).build());

        CouponInformationRequest request = new CouponInformationRequest();
        request.setName("UPDATED_COUPON_NAME");
        request.setDiscountType(CouponDiscountType.PERCENTAGE);
        request.setDiscountAmount(50);
        request.setExpirationDate(LocalDateTime.now());
        request.setMinimumOrderPrice(5000);

        CouponDTO updatedCoupon = couponService.updateCoupon(coupon.getId(), request);
        assertEquals(request.getName(), updatedCoupon.getName());
        assertEquals(request.getDiscountType(), updatedCoupon.getDiscountType());
        assertEquals(request.getDiscountAmount(), updatedCoupon.getDiscountAmount());
        assertEquals(request.getExpirationDate(), updatedCoupon.getExpirationDate());
        assertEquals(request.getMinimumOrderPrice(), updatedCoupon.getMinimumOrderPrice());
    }

    @Test
    @DisplayName("Delete coupon")
    void deleteCoupon() {
        Coupon coupon1 = couponRepository.save(Coupon.builder()
                .name("COUPON_NAME1")
                .discountType(CouponDiscountType.FIXED)
                .discountAmount(5000)
                .expirationDate(LocalDateTime.of(1970, 1, 1, 1, 1, 1))
                .minimumOrderPrice(15000)
                .shop(shop).build());
        Coupon coupon2 = couponRepository.save(Coupon.builder()
                .name("COUPON_NAME2")
                .discountType(CouponDiscountType.PERCENTAGE)
                .discountAmount(50)
                .expirationDate(LocalDateTime.now())
                .minimumOrderPrice(15000)
                .shop(shop).build());

        shop.getCoupons().add(coupon1);
        shop.getCoupons().add(coupon2);

        List<CouponDTO> couponDTOS = couponService.deleteCoupon(shop.getId(), coupon1.getId());
        assertEquals(1, couponDTOS.size());
        assertEquals(coupon2.getName(), couponDTOS.get(0).getName());
        assertEquals(coupon2.getDiscountType(), couponDTOS.get(0).getDiscountType());
        assertEquals(coupon2.getDiscountAmount(), couponDTOS.get(0).getDiscountAmount());
        assertEquals(coupon2.getExpirationDate(), couponDTOS.get(0).getExpirationDate());
        assertEquals(coupon2.getMinimumOrderPrice(), couponDTOS.get(0).getMinimumOrderPrice());
        assertEquals(coupon2.getShop().getId(), couponDTOS.get(0).getShop().getId());
    }

    @Test
    @DisplayName("List created coupons of shop.")
    void listCreatedCoupons() {
        Coupon coupon1 = couponRepository.save(Coupon.builder()
                .name("COUPON_NAME1")
                .discountType(CouponDiscountType.FIXED)
                .discountAmount(5000)
                .expirationDate(LocalDateTime.of(1970,1,1,1,1,1))
                .minimumOrderPrice(15000)
                .shop(shop).build());
        Coupon coupon2 = couponRepository.save(Coupon.builder()
                .name("COUPON_NAME2")
                .discountType(CouponDiscountType.PERCENTAGE)
                .discountAmount(50)
                .expirationDate(LocalDateTime.now())
                .minimumOrderPrice(15000)
                .shop(shop).build());
        shop.getCoupons().add(coupon1);
        shop.getCoupons().add(coupon2);

        List<CouponDTO> couponDTOS = couponService.listCreatedCoupons(shop.getId());
        Optional<CouponDTO> readCoupon1 = couponDTOS.stream()
                .filter(couponDTO -> couponDTO.getId() == coupon1.getId() &&
                        couponDTO.getName().equals(coupon1.getName()) &&
                        couponDTO.getDiscountType().equals(coupon1.getDiscountType()) &&
                        couponDTO.getDiscountAmount() == coupon1.getDiscountAmount() &&
                        couponDTO.getExpirationDate().equals(coupon1.getExpirationDate()) &&
                        couponDTO.getMinimumOrderPrice() == coupon1.getMinimumOrderPrice())
                .findAny();
        assertFalse(readCoupon1.isEmpty());

        Optional<CouponDTO> readCoupon2 = couponDTOS.stream()
                .filter(couponDTO -> couponDTO.getId() == coupon2.getId() &&
                        couponDTO.getName().equals(coupon2.getName()) &&
                        couponDTO.getDiscountType().equals(coupon2.getDiscountType()) &&
                        couponDTO.getDiscountAmount() == coupon2.getDiscountAmount() &&
                        couponDTO.getExpirationDate().equals(coupon2.getExpirationDate()) &&
                        couponDTO.getMinimumOrderPrice() == coupon2.getMinimumOrderPrice())
                .findAny();
        assertFalse(readCoupon2.isEmpty());
    }
}
