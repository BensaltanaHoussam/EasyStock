package com.easystock.dto.payment;

import com.easystock.entity.enums.PaymentMethod;
import com.easystock.entity.enums.PaymentStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PaymentResponseDto {
    private Long id;
    private Long orderId;
    private int paymentNumber;
    private double amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private LocalDate paymentDate;
    private LocalDate cashInDate;
    private String reference;
    private String bank;
    private LocalDate dueDate;
}