package com.easystock.controller.payment;

import com.easystock.config.auth.Auth;
import com.easystock.dto.payment.PaymentRequestDto;
import com.easystock.dto.payment.PaymentResponseDto;
import com.easystock.entity.enums.UserRole;
import com.easystock.service.payment.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @Auth(allowedRoles = {UserRole.ADMIN})
    public ResponseEntity<PaymentResponseDto> createPayment(@RequestBody @Valid PaymentRequestDto dto) {
        return new ResponseEntity<>(paymentService.createPayment(dto), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/cash-in")
    @Auth(allowedRoles = {UserRole.ADMIN})
    public ResponseEntity<PaymentResponseDto> cashInPayment(@PathVariable("id") Long paymentId) {
        return ResponseEntity.ok(paymentService.cashInPayment(paymentId));
    }

    @PostMapping("/{id}/reject")
    @Auth(allowedRoles = {UserRole.ADMIN})
    public ResponseEntity<PaymentResponseDto> rejectPayment(@PathVariable("id") Long paymentId) {
        return ResponseEntity.ok(paymentService.rejectPayment(paymentId));
    }
}