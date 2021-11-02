package com.programmers.devcourse.vaemin.shop.service;

import com.programmers.devcourse.vaemin.shop.dto.ShopDto;
import com.programmers.devcourse.vaemin.shop.dto.ShopSearchResponse;
import com.programmers.devcourse.vaemin.shop.entity.Shop;
import com.programmers.devcourse.vaemin.shop.exception.ShopExceptionSuppliers;
import com.programmers.devcourse.vaemin.shop.repository.ShopRepository;
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

    private final ShopRepository shopRepository;

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
                .owner(shopDto.getOwner())
                .doroAddress(shopDto.getDoroAddress())
                .doroIndex(shopDto.getDoroIndex())
                .detailAddress(shopDto.getDetailAddress())
                .build();

        return shopRepository.save(shop).getId();
    }

    public void deleteShop(Long id) {
        Shop shop = shopRepository.findById(id).orElseThrow(ShopExceptionSuppliers.shopNotFound);
        shopRepository.delete(shop);
    }

    public ShopDto findShop(Long id) {
        Shop shop = shopRepository.findById(id).orElseThrow(ShopExceptionSuppliers.shopNotFound);
        return new ShopDto(shop);
    }

    public Long updateShop(Long id, ShopDto shopDto) {
        Shop shop = shopRepository.findById(id).orElseThrow(ShopExceptionSuppliers.shopNotFound);
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

    public List<ShopSearchResponse> findShopsByName(String shopName) {
        List<ShopSearchResponse> shopResponses = shopRepository.findByNameContaining(shopName).stream()
                .map(ShopSearchResponse::new)
                .collect(Collectors.toList());
        return shopResponses;
    }
}
