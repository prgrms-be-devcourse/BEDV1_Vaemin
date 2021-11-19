package com.programmers.devcourse.vaemin.shop.service;

import com.programmers.devcourse.vaemin.food.entity.Food;
import com.programmers.devcourse.vaemin.food.repository.FoodRepository;
import com.programmers.devcourse.vaemin.root.exception.EntityExceptionSuppliers;
import com.programmers.devcourse.vaemin.shop.dto.ShopDto;
import com.programmers.devcourse.vaemin.shop.dto.ShopSearchResponse;
import com.programmers.devcourse.vaemin.shop.entity.Shop;
import com.programmers.devcourse.vaemin.shop.entity.ShopCategory;
import com.programmers.devcourse.vaemin.shop.repository.ShopCategoryRepository;
import com.programmers.devcourse.vaemin.shop.repository.ShopRepository;
import com.programmers.devcourse.vaemin.user.owner.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ShopService {
    private final OwnerRepository ownerRepository;
    private final ShopRepository shopRepository;
    private final ShopCategoryRepository shopCategoryRepository;
    private final FoodRepository foodRepository;

    public Long createShop(ShopDto shopDto) {
        Shop shop = Shop.builder()
                .name(shopDto.getName())
                .phoneNum(shopDto.getPhoneNum())
                .shortDesc(shopDto.getShortDescription())
                .longDesc(shopDto.getLongDescription())
                .orderType(shopDto.getSupportedOrderType())
                .payment(shopDto.getSupportedPayment())
                .openTime(shopDto.getOpenTime())
                .closeTime(shopDto.getCloseTime())
                .deliveryFee(shopDto.getDeliveryFee())
                .minOrderPrice(shopDto.getMinOrderPrice())
                .shopStatus(shopDto.getShopStatus())
                .registerNumber(shopDto.getRegisterNumber())
                .owner(ownerRepository.findById(shopDto.getOwnerId()).orElseThrow(() ->
                        new IllegalArgumentException("Owner " + shopDto.getOwnerId() + " not found.")))
                .doroAddress(shopDto.getDoroAddress())
                .doroIndex(shopDto.getDoroIndex())
                .detailAddress(shopDto.getDetailAddress())
                .build();

        return shopRepository.save(shop).getId();
    }

    public void deleteShop(Long id) {
        Shop shop = shopRepository.findById(id).orElseThrow(EntityExceptionSuppliers.shopNotFound);
        shopRepository.delete(shop);
    }

    public ShopDto findShop(Long id) {
        Shop shop = shopRepository.findById(id).orElseThrow(EntityExceptionSuppliers.shopNotFound);
        return new ShopDto(shop);
    }

    public Long updateShop(Long id, ShopDto shopDto) {
        Shop shop = shopRepository.findById(id).orElseThrow(EntityExceptionSuppliers.shopNotFound);
        shop.changeName(shopDto.getName());
        shop.changePhoneNum(shopDto.getPhoneNum());
        shop.changeShortDescription(shopDto.getShortDescription());
        shop.changeLongDescription(shopDto.getLongDescription());
        shop.changeSupportedOrderType(shopDto.getSupportedOrderType());
        shop.changeSupportedPayment(shopDto.getSupportedPayment());
        shop.changeOpenTime(shopDto.getOpenTime());
        shop.changeCloseTime(shopDto.getCloseTime());
        shop.changeDeliveryFee(shopDto.getDeliveryFee());
        shop.changeMinOrderPrice(shopDto.getMinOrderPrice());
        shop.changeShopStatus(shopDto.getShopStatus());
        shop.changeRegisterNumber(shopDto.getRegisterNumber());
        shop.changeAddress(shopDto.getDoroAddress(), shopDto.getDoroIndex(), shopDto.getDetailAddress());

        return shop.getId();
    }

    // question : jpa repository vs crud repository
    public List<ShopSearchResponse> findAllShops() {
        List<ShopSearchResponse> shopResponses = new ArrayList<>();
        shopRepository.findAll().forEach(shop -> shopResponses.add(new ShopSearchResponse(shop)));
        return shopResponses;
    }

    public List<ShopSearchResponse> findByName(String shopName) {
        return shopRepository.findByNameContaining(shopName).stream()
                .map(ShopSearchResponse::new)
                .collect(Collectors.toList());
    }

    public List<ShopSearchResponse> findByCategory(Long categoryId) {
        List<ShopCategory> shopCategories = shopCategoryRepository.findAllByCategoryId(categoryId);
        return shopCategories.stream()
                .map(ShopCategory::getShop)
                .map(ShopSearchResponse::new)
                .collect(Collectors.toList());
    }

    public List<ShopSearchResponse> findByFoodName(String foodName) {
        return foodRepository.findByName(foodName).stream()
                .map(Food::getShop)
                .map(ShopSearchResponse::new)
                .collect(Collectors.toList());
    }
}
