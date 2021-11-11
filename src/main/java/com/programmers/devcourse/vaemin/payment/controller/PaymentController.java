package com.programmers.devcourse.vaemin.payment.controller;

import com.programmers.devcourse.vaemin.payment.dto.PaymentDto;
import com.programmers.devcourse.vaemin.payment.service.PaymentService;
import com.programmers.devcourse.vaemin.root.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/payment")
    public ApiResponse<Long> createPayment(@RequestBody PaymentDto paymentDto) {
        return ApiResponse.success(paymentService.createPayment(paymentDto));
    }

    @GetMapping("/payment/{paymentId}")
    public ApiResponse<PaymentDto> getPayment(@PathVariable Long paymentId) {
        PaymentDto payment = paymentService.findPayment(paymentId);
        return ApiResponse.success(payment);
    }
}
