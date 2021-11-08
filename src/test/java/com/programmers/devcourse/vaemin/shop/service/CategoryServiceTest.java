package com.programmers.devcourse.vaemin.shop.service;

import com.programmers.devcourse.vaemin.root.exception.EntityExceptionSuppliers;
import com.programmers.devcourse.vaemin.shop.dto.CategoryDto;
import com.programmers.devcourse.vaemin.shop.entity.*;
import com.programmers.devcourse.vaemin.shop.repository.CategoryRepository;
import com.programmers.devcourse.vaemin.shop.repository.ShopCategoryRepository;
import com.programmers.devcourse.vaemin.shop.repository.ShopRepository;
import com.programmers.devcourse.vaemin.user.owner.entity.Owner;
import com.programmers.devcourse.vaemin.user.owner.repository.OwnerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CategoryServiceTest {

    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    OwnerRepository ownerRepository;
    @Autowired
    ShopRepository shopRepository;
    @Autowired
    ShopCategoryRepository shopCategoryRepository;

    Owner owner;
    Shop shop;
    Category category;
    Long categoryId;

    @BeforeEach
    void save_test() {
        // Given
        owner = new Owner("username", "email", "phoneNum");
        ownerRepository.save(owner);
        shop = shopRepository.save(Shop.builder()
                .name("test")
                .phoneNum("010-1234-5678")
                .shortDesc("shop service test")
                .longDesc("test is success")
                .orderType(ShopSupportedOrderType.BOTH)
                .payment(ShopSupportedPayment.CARD)
                .openTime(LocalTime.now())
                .closeTime(LocalTime.now())
                .deliveryFee(3000)
                .minOrderPrice(15000)
                .shopStatus(ShopStatus.DEACTIVATED)
                .registerNumber("123-456-7890")
                .owner(owner)
                .doroAddress("Doro-ro")
                .doroIndex(123)
                .detailAddress("Seoul")
                .build());

        CategoryDto categoryDto = new CategoryDto("fast food");

        // When
        categoryId = categoryService.createCategory(categoryDto);

        // Then
        category = categoryRepository.findById(categoryId).orElseThrow(EntityExceptionSuppliers.categoryNotFound);
        assertEquals(categoryDto.getName(), categoryDto.getName());
    }

    @AfterEach
    void tearDown() {
        categoryRepository.deleteAll();
    }

    @Test
    void findCategoryTest() {
        // When
        CategoryDto one = categoryService.findCategory(categoryId);

        // Then
        assertThat(one.getName()).isEqualTo(category.getName());
    }

    @Test
    void deleteCategoryTest() {
        // When
        categoryService.deleteCategory(categoryId);

        // Then
        assertFalse(categoryRepository.existsById(categoryId));
    }

    @Test
    void updateCategoryTest() {
        // Given
        CategoryDto categoryDto = categoryService.findCategory(categoryId);
        categoryDto.setName("korean food");

        // When
        Long updatedCategoryId = categoryService.updateCategory(categoryId, categoryDto);

        // Then
        Category updated = categoryRepository.findById(updatedCategoryId).orElseThrow(EntityExceptionSuppliers.categoryNotFound);
        assertEquals("korean food", updated.getName());
    }

    @Test
    void joinShopCategoryTest() {
        // Given
        Long shopId = shop.getId();

        // When
        Long shopCategoryId = categoryService.joinShopCategory(shopId, categoryId);

        // Then
        ShopCategory shopCategory = shopCategoryRepository.findById(shopCategoryId).orElseThrow(EntityExceptionSuppliers.shopCategoryNotFound);
        assertEquals(shopId, shopCategory.getShop().getId());
        assertEquals(categoryId, shopCategory.getCategory().getId());
    }

    @Test
    void withdrawShopCategoryTest() {
        // Given
        Long shopId = shop.getId();
        ShopCategory shopCategory = ShopCategory.builder()
                .shop(shop)
                .category(category)
                .build();
        shopCategoryRepository.save(shopCategory);

        // When
        categoryService.withdrawShopCategory(shopId, categoryId);

        // Then
        assertFalse(shopCategoryRepository.existsByShopAndCategory(shop, category));
    }
}
