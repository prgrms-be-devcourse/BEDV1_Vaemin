package com.programmers.devcourse.vaemin.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmers.devcourse.vaemin.payment.dto.PaymentDto;
import com.programmers.devcourse.vaemin.payment.entity.Payment;
import com.programmers.devcourse.vaemin.payment.entity.PaymentStatus;
import com.programmers.devcourse.vaemin.payment.service.PaymentService;
import com.programmers.devcourse.vaemin.user.customer.dto.CustomerCreateRequest;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import com.programmers.devcourse.vaemin.user.customer.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    ObjectMapper objectMapper;

    PaymentDto paymentDto;
    Payment payment;
    Long paymentId;

    @BeforeEach
    void setUp() {
        CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest("set customer name",
                "setcustomer@gmail.com",
                "01000000000",
                "set location code",
                "set address detail");
        Customer customer = customerService.createCustomer(customerCreateRequest);
        payment = Payment.builder()
                .price(1000)
                .customer(customer)
                .paymentStatus(PaymentStatus.NOT_PAYED)
                .build();
        paymentDto = new PaymentDto(payment);
        paymentId = paymentService.createPayment(paymentDto);
        }

    @Test
    void createPayment() throws Exception {
        mockMvc.perform(post("/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new PaymentDto(payment))))
                .andExpect(status().isOk()) // 200
                .andDo(print());
    }

    @Test
    void getPayment() throws Exception {
        mockMvc.perform(get("/payment/{paymentId}", paymentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}