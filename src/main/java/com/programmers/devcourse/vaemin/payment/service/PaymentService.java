package com.programmers.devcourse.vaemin.payment.service;

import com.programmers.devcourse.vaemin.order.entity.OrderStatus;
import com.programmers.devcourse.vaemin.payment.dto.PaymentDto;
import com.programmers.devcourse.vaemin.payment.entity.Payment;
import com.programmers.devcourse.vaemin.payment.exception.PaymentExceptionSupplier;
import com.programmers.devcourse.vaemin.payment.repository.PaymentRepository;
import com.programmers.devcourse.vaemin.review.dto.ReviewDto;
import com.programmers.devcourse.vaemin.review.entity.Review;
import com.programmers.devcourse.vaemin.review.exception.ReviewExceptionSuppliers;
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

    public Long updatePayment(Long id) {
        Payment payment = paymentRepository.findById(id).orElseThrow(PaymentExceptionSupplier.paymentNotFound);

        // Order의 orderStatus -> REJECTED 변경시
        if(payment.getOrder().getOrderStatus() == OrderStatus.REJECTED) {
            payment.changePaymentStatus(payment.getPaymentStatus());
        }

        return payment.getId();
    }
}