package com.programmers.devcourse.vaemin.shop.entity;

import com.programmers.devcourse.vaemin.root.IdentifiableEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "delivey_supported_regions")
@NoArgsConstructor
public class DeliverySupportedRegions extends IdentifiableEntity {
    @Column(name = "location_code", nullable = false)
    private String locationCode;

    @ManyToOne
    @JoinColumn(name = "shop_id", referencedColumnName = "id")
    private Shop shop;

    public void changeLocationCode(@NonNull String locationCode) {
        if(locationCode.isBlank()) return;
        this.locationCode = locationCode;
    }

    @Builder
    public DeliverySupportedRegions(String locationCode) {
        this.locationCode = locationCode;
    }
}
