package com.programmers.devcourse.vaemin.order.service;

import com.programmers.devcourse.vaemin.order.controller.bind.OrderStatusRequest;
import com.programmers.devcourse.vaemin.order.entity.Order;
import com.programmers.devcourse.vaemin.order.entity.OrderStatus;
import com.programmers.devcourse.vaemin.order.entity.dto.OwnerOrderDTO;
import com.programmers.devcourse.vaemin.root.exception.EntityExceptionSuppliers;
import com.programmers.devcourse.vaemin.shop.entity.Shop;
import com.programmers.devcourse.vaemin.user.owner.entity.Owner;
import com.programmers.devcourse.vaemin.user.owner.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ShopOrderService {
    private final OwnerRepository ownerRepository;

    private Owner readOwner(long ownerId) {
        return ownerRepository.findById(ownerId).orElseThrow(EntityExceptionSuppliers.ownerNotFound);
    }

    private Shop readShop(Owner owner, long shopId) {
        return owner.getShops().stream()
                .filter(s -> s.getId() == shopId)
                .findAny().orElseThrow(EntityExceptionSuppliers.shopNotFound);
    }

    private Order readOrder(Shop shop, long orderId) {
        return shop.getOrders().stream()
                .filter(o -> o.getId() == orderId)
                .findAny().orElseThrow(() -> new IllegalArgumentException("Order with given id not found."));
    }

    public List<OwnerOrderDTO> listShopOrders(long ownerId, long shopId) {
        Shop shop = readShop(readOwner(ownerId), shopId);
        return shop.getOrders().stream()
                .map(OwnerOrderDTO::new)
                .collect(Collectors.toList());
    }

    public OwnerOrderDTO readShopOrder(long ownerId, long shopId, long orderId) {
        Owner owner = readOwner(ownerId);
        Shop shop = readShop(owner, shopId);
        return new OwnerOrderDTO(readOrder(shop, orderId));
    }

    public List<OwnerOrderDTO> listWaitingOrders(long ownerId, long shopId) {
        Shop shop = readShop(readOwner(ownerId), shopId);
        return shop.getOrders().stream()
                .filter(order -> order.getOrderStatus().equals(OrderStatus.CREATED))
                .map(OwnerOrderDTO::new)
                .collect(Collectors.toList());
    }

    public OwnerOrderDTO receiveOrder(long ownerId, long shopId, long orderId, OrderStatusRequest request) {
        Order order = readOrder(readShop(readOwner(ownerId), shopId), orderId);
        if(order.getOrderStatus().equals(OrderStatus.CANCELLED)) {
            throw new IllegalArgumentException("Cancelled order cannot be accepted/rejected.");
        }
        order.changeOrderStatus(request.isAccept() ? OrderStatus.ACCEPTED : OrderStatus.REJECTED);
        return new OwnerOrderDTO(order);
    }
}
