package com.programmers.devcourse.vaemin.user.owner.service;

import com.programmers.devcourse.vaemin.user.owner.dto.OwnerCreateRequest;
import com.programmers.devcourse.vaemin.user.owner.dto.OwnerDetailResponse;
import com.programmers.devcourse.vaemin.user.owner.dto.OwnerUpdateRequest;
import com.programmers.devcourse.vaemin.user.owner.entity.Owner;
import com.programmers.devcourse.vaemin.user.owner.repository.OwnerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class OwnerServiceTest {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private OwnerRepository ownerRepository;

    private static Owner owner1;


    @BeforeEach
    void createOwner() {
        OwnerCreateRequest ownerRequest1 = new OwnerCreateRequest("owner1", "owner1@naver.com", "01000000000");
        owner1 = ownerService.createOwner(ownerRequest1);
        assertThat(owner1.getUsername()).isEqualTo("owner1");
    }

    @AfterEach
    void tearDown() {
        ownerRepository.deleteAll();
    }

    @Test
    void findOneOwner() {
        // Given
        Long owner1Id = owner1.getId();
        // When
        OwnerDetailResponse ownerResponse1 = ownerService.findOneOwner(owner1Id);
        // Then
        assertThat(ownerResponse1.getUserName()).isEqualTo(owner1.getUsername());
    }

    @Test
    void updateOwner() {
        // Given
        Long owner1Id = owner1.getId();
        OwnerUpdateRequest owner1Request = new OwnerUpdateRequest("updated owner1", "updatedOwner1@naver.com", "01000000000");
        // When
        Owner updatedOwner = ownerService.updateOwner(owner1Id, owner1Request);
        OwnerDetailResponse oneOwner = ownerService.findOneOwner(owner1Id);
        // Then
        assertThat(updatedOwner.getUsername()).isEqualTo(oneOwner.getUserName());
    }

    @Test
    void deleteOwner() {
        // Given
        Long owner1Id = owner1.getId();
        // When
        ownerService.deleteOwner(owner1Id);
        // Then
        assertFalse(ownerRepository.existsById(owner1Id));
    }
}