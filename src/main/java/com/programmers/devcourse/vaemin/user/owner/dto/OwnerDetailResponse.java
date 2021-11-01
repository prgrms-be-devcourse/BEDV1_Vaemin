package com.programmers.devcourse.vaemin.user.owner.dto;

import com.programmers.devcourse.vaemin.user.owner.entity.Owner;
import lombok.Getter;

@Getter
public class OwnerDetailResponse {
    private String userName;
    private String email;

    public OwnerDetailResponse(Owner owner) {
        this.userName = owner.getUsername();
        this.email = owner.getEmail();
    }
}
