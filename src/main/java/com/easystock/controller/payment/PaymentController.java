package com.easystock.controller.payment;

import com.easystock.config.auth.Auth;
import com.easystock.dto.payment.PaymentRequestDto;
import com.easystock.dto.payment.PaymentResponseDto;
import com.easystock.entity.enums.UserRole;
import com.easystock.service.payment.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @Auth(allowedRoles = {UserRole.ADMIN})
    public ResponseEntity<PaymentResponseDto> createPayment(@RequestBody @Valid PaymentRequestDto dto) {
        log.info("Request to create payment for Order ID: {}", dto.getOrderId());
        PaymentResponseDto response = paymentService.createPayment(dto);
        log.info("Payment created successfully with ID: {}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/cash-in")
    @Auth(allowedRoles = {UserRole.ADMIN})
    public ResponseEntity<PaymentResponseDto> cashInPayment(@PathVariable("id") Long paymentId) {
        log.info("Request to cash in payment with ID: {}", paymentId);
        PaymentResponseDto response = paymentService.cashInPayment(paymentId);
        log.info("Payment with ID: {} cashed in successfully", response.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/reject")
    @Auth(allowedRoles = {UserRole.ADMIN})
    public ResponseEntity<PaymentResponseDto> rejectPayment(@PathVariable("id") Long paymentId) {
        log.info("Request to reject payment with ID: {}", paymentId);
        PaymentResponseDto response = paymentService.rejectPayment(paymentId);
        log.info("Payment with ID: {} rejected successfully", response.getId());
        return ResponseEntity.ok(response);
    }
}
