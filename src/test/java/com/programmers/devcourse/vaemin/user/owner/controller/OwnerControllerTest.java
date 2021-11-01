package com.programmers.devcourse.vaemin.user.owner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmers.devcourse.vaemin.user.owner.dto.OwnerCreateRequest;
import com.programmers.devcourse.vaemin.user.owner.dto.OwnerUpdateRequest;
import com.programmers.devcourse.vaemin.user.owner.entity.Owner;
import com.programmers.devcourse.vaemin.user.owner.service.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OwnerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    ObjectMapper objectMapper;

    private static OwnerCreateRequest ownerCreateRequest;
    private static Owner setOwner;
    private static Long setOwnerId;

    @BeforeEach
    void setUp() {
        ownerCreateRequest = new OwnerCreateRequest("set owner name",
                "setowner@gmail.com",
                "01000000000");
        setOwner = ownerService.createOwner(ownerCreateRequest);
        setOwnerId = setOwner.getId();
    }

    @Test
    void createOwner() throws Exception {
        mockMvc.perform(post("/owners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new OwnerCreateRequest("new owner name",
                        "newOwner@gmail.com",
                        "01000000000"))))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getOwner() throws Exception {
        mockMvc.perform(get("/owners/{ownerId}", setOwnerId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void updateOwner() throws Exception {
        mockMvc.perform(put("/owners/{ownerId}", setOwnerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new OwnerUpdateRequest("updated owner name",
                        "updatedOwner@gmail.com",
                        "01010101010"))))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void deleteOwner() throws Exception {
        mockMvc.perform(delete("/owners/{ownerId}", setOwnerId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}