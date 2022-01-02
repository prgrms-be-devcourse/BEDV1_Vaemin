package com.programmers.devcourse.vaemin.payment.service;

import com.programmers.devcourse.vaemin.order.entity.OrderStatus;
import com.programmers.devcourse.vaemin.payment.dto.PaymentDto;
import com.programmers.devcourse.vaemin.payment.entity.Payment;
import com.programmers.devcourse.vaemin.payment.entity.PaymentStatus;
import com.programmers.devcourse.vaemin.payment.repository.PaymentRepository;
import com.programmers.devcourse.vaemin.root.exception.EntityExceptionSuppliers;
import com.programmers.devcourse.vaemin.user.customer.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Long createPayment(PaymentDto paymentDto) {

        PaymentStatus status;
        // payment 생성 -> payed, 포인트 차감
        if(paymentDto.getCustomer().getPoint() < paymentDto.getPrice()) {
            status = PaymentStatus.REJECTED;
        } else {
            status = PaymentStatus.PAYED;
            Customer customer = paymentDto.getCustomer();
            customer.changePoint(customer.getPoint()-paymentDto.getPrice());
        }

        Payment payment = Payment.builder()
                .price(paymentDto.getPrice())
                .customer(paymentDto.getCustomer())
                .paymentStatus(status)
                .build();

        return paymentRepository.save(payment).getId();
    }

    public PaymentDto findPayment(Long id) {
        Payment payment = paymentRepository.findById(id).orElseThrow(EntityExceptionSuppliers.paymentNotFound);
        return new PaymentDto(payment);
    }

    public Long updatePayment(Long id) {
        Payment payment = paymentRepository.findById(id).orElseThrow(EntityExceptionSuppliers.paymentNotFound);

        // Order 의 orderStatus -> REJECTED 변경시
        if(payment.getOrder().getOrderStatus() == OrderStatus.REJECTED) {
            payment.changeStatus(PaymentStatus.REFUND);
            Customer customer = payment.getCustomer();
            customer.changePoint(customer.getPoint()+payment.getPrice());
        }

        return payment.getId();
    }
}
