package com.programmers.devcourse.vaemin.user.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmers.devcourse.vaemin.user.customer.dto.CustomerCreateRequest;
import com.programmers.devcourse.vaemin.user.customer.dto.CustomerDeliveryAddressRequest;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import com.programmers.devcourse.vaemin.user.customer.service.CustomerDeliveryAddressService;
import com.programmers.devcourse.vaemin.user.customer.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerDeliveryAddressControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerDeliveryAddressService addressService;

    @Autowired
    ObjectMapper objectMapper;

    private static Customer setCustomer;

    @BeforeEach
    void setUp() {
        setCustomer = customerService.createCustomer(
                new CustomerCreateRequest("set customer name",
                        "setcustomer@gmail.com",
                        "01000000000",
                        "set location code",
                        "set address detail"));
    }

    @Test
    void getAddress() throws Exception {
        mockMvc.perform(get("/customers/{customerId}/address", setCustomer.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void addAndUpdateAddress() throws Exception {
        mockMvc.perform(post("/customers/{customerId}/address", setCustomer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CustomerDeliveryAddressRequest("updated L.C",
                        "updated A.D"))))
                .andExpect(status().isOk())
                .andDo(print());
    }


}