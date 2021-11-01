package com.programmers.devcourse.vaemin.user.owner.service;

import com.programmers.devcourse.vaemin.user.owner.dto.OwnerCreateRequest;
import com.programmers.devcourse.vaemin.user.owner.dto.OwnerDetailResponse;
import com.programmers.devcourse.vaemin.user.owner.dto.OwnerUpdateRequest;
import com.programmers.devcourse.vaemin.user.owner.entity.Owner;
import com.programmers.devcourse.vaemin.user.owner.exception.OwnerExceptionSuppliers;
import com.programmers.devcourse.vaemin.user.owner.repository.OwnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OwnerService {
    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Transactional
    public Owner createOwner(OwnerCreateRequest request) {
        Owner owner = new Owner(request.getUserName(),
                request.getEmail(),
                request.getPhoneNum());
        return ownerRepository.save(owner);
    }

    @Transactional
    public OwnerDetailResponse findOneOwner(Long ownerId) {
        Owner owner = ownerRepository.findById(ownerId).orElseThrow(OwnerExceptionSuppliers.ownerNotFound);
        return new OwnerDetailResponse(owner);
    }

    @Transactional
    public Owner updateOwner(Long ownerId, OwnerUpdateRequest request) {
        Owner owner = ownerRepository.findById(ownerId).orElseThrow(OwnerExceptionSuppliers.ownerNotFound);
        owner.changeName(request.getUserName());
        owner.changeEmail(request.getEmail());
        owner.changePhoneNum(request.getPhoneNum());
        owner.changeUpdatedAt(request.getUpdatedAt());
        return owner;
    }

    @Transactional
    public void deleteOwner(Long ownerId) {
        ownerRepository.deleteById(ownerId);
    }
}
