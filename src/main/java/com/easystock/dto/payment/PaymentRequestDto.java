package com.easystock.dto.payment;

import com.easystock.entity.enums.PaymentMethod;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PaymentRequestDto {
    @NotNull(message = "Order ID cannot be null")
    private Long orderId;

    @NotNull(message = "Payment method cannot be null")
    private PaymentMethod paymentMethod;

    @Min(value = 1, message = "Payment amount must be positive")
    private double amount;

    // Optional fields depending on payment method
    private LocalDate paymentDate;
    private String reference; // For Cheque or Bank Transfer
    private String bank;      // For Cheque
    private LocalDate dueDate;   // For Cheque
}