package com.programmers.devcourse.vaemin.shop.repository;

import com.programmers.devcourse.vaemin.shop.entity.DeliverySupportedRegions;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliverySupportedRegionRepository extends CrudRepository<DeliverySupportedRegions, Long> {
}
