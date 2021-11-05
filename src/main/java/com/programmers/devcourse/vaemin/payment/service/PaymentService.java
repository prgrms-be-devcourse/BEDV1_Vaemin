package com.programmers.devcourse.vaemin.payment.service;

import com.programmers.devcourse.vaemin.payment.dto.PaymentDto;
import com.programmers.devcourse.vaemin.payment.entity.Payment;
import com.programmers.devcourse.vaemin.payment.exception.PaymentExceptionSupplier;
import com.programmers.devcourse.vaemin.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Long createPayment(PaymentDto paymentDto) {
        Payment payment = Payment.builder()
                .price(paymentDto.getPrice())
                .order(paymentDto.getOrder())
                .customer(paymentDto.getCustomer())
                .build();

        return paymentRepository.save(payment).getId();
    }

    public PaymentDto findPayment(Long id) {
        Payment payment = paymentRepository.findById(id).orElseThrow(PaymentExceptionSupplier.paymentNotFound);
        return new PaymentDto(payment);
    }

    // todo : 결제 수정이 되는걸로?
}
