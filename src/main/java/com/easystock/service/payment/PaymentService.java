package com.easystock.service.payment;

import com.easystock.dto.payment.PaymentRequestDto;
import com.easystock.dto.payment.PaymentResponseDto;

public interface PaymentService {
    PaymentResponseDto createPayment(PaymentRequestDto dto);
    PaymentResponseDto cashInPayment(Long paymentId);
    PaymentResponseDto rejectPayment(Long paymentId);
}