package com.programmers.devcourse.vaemin.shop.service;

import com.programmers.devcourse.vaemin.root.exception.EntityExceptionSuppliers;
import com.programmers.devcourse.vaemin.shop.dto.CategoryDto;
import com.programmers.devcourse.vaemin.shop.entity.Category;
import com.programmers.devcourse.vaemin.shop.entity.Shop;
import com.programmers.devcourse.vaemin.shop.entity.ShopCategory;
import com.programmers.devcourse.vaemin.shop.repository.CategoryRepository;
import com.programmers.devcourse.vaemin.shop.repository.ShopCategoryRepository;
import com.programmers.devcourse.vaemin.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ShopRepository shopRepository;
    private final ShopCategoryRepository shopCategoryRepository;

    // question : categoryDto에 id가 있는데 이렇게 되면 생성된 category랑 categoryDto id가 달라질 거 같은데
    public Long createCategory(CategoryDto categoryDto) {
        Category category = Category.builder()
                .name(categoryDto.getName())
                .build();
        return categoryRepository.save(category).getId();
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(EntityExceptionSuppliers.categoryNotFound);
        categoryRepository.delete(category);
    }

    public CategoryDto findCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(EntityExceptionSuppliers.categoryNotFound);
        return new CategoryDto(category);
    }

    public Long updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(EntityExceptionSuppliers.categoryNotFound);
        category.changeName(categoryDto.getName());
        return category.getId();
    }

    public Long joinShopCategory(long shopId, long categoryId) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(EntityExceptionSuppliers.shopNotFound);
        Category category = categoryRepository.findById(categoryId).orElseThrow(EntityExceptionSuppliers.categoryNotFound);

        if(shopCategoryRepository.existsByShopAndCategory(shop, category)) {
            ShopCategory shopCategory = shopCategoryRepository.findByShopAndCategory(shop, category);
            return shopCategory.getId();
        }
        ShopCategory shopCategory = ShopCategory.builder()
                .shop(shop)
                .category(category)
                .build();

        shopCategoryRepository.save(shopCategory);

        return shopCategory.getId();
    }

    public void withdrawShopCategory(long shopId, long categoryId) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(EntityExceptionSuppliers.shopNotFound);
        Category category = categoryRepository.findById(categoryId).orElseThrow(EntityExceptionSuppliers.categoryNotFound);
        shopCategoryRepository.deleteByShopAndCategory(shop, category);
    }
}
