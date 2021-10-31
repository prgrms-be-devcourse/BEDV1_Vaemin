package com.programmers.devcourse.vaemin.user.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmers.devcourse.vaemin.user.customer.dto.CustomerCreateRequest;
import com.programmers.devcourse.vaemin.user.customer.dto.CustomerUpdateRequest;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import com.programmers.devcourse.vaemin.user.customer.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerService customerService;

    @Autowired
    ObjectMapper objectMapper;

    private static CustomerCreateRequest customerCreateRequest;
    private static Customer setCustomer;
    private static Long setCustomerId;

    @BeforeEach
    void setUp() {
        customerCreateRequest = new CustomerCreateRequest("set customer name",
                "setcustomer@gmail.com",
                "01000000000");
        setCustomer = customerService.createCustomer(customerCreateRequest);
        setCustomerId = setCustomer.getId();
    }

    @Test
    void createCustomer() throws Exception {
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CustomerCreateRequest("new customer name",
                        "newCustomer@gmail.com",
                        "01000000000"))))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getCustomer() throws Exception {
        mockMvc.perform(get("/customers/{customerId}", setCustomerId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void updateCustomer() throws Exception {
        mockMvc.perform(put("/customers/{customerId}", setCustomerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CustomerUpdateRequest("updated customer name",
                        "updatedCustomer@gmail.com",
                        "01010101010"))))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void deleteCustomer() throws Exception {
        mockMvc.perform(delete("/customers/{customerId}", setCustomerId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
