package com.easystock.service.payment;

import com.easystock.dto.payment.PaymentRequestDto;
import com.easystock.dto.payment.PaymentResponseDto;
import com.easystock.entity.Order;
import com.easystock.entity.Payment;
import com.easystock.entity.enums.PaymentMethod;
import com.easystock.entity.enums.PaymentStatus;
import com.easystock.mapper.PaymentMapper;
import com.easystock.repository.OrderRepository;
import com.easystock.repository.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;
    private static final double MAX_CASH_AMOUNT = 20000.0;

    @Override
    @Transactional
    public PaymentResponseDto createPayment(PaymentRequestDto dto) {
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + dto.getOrderId()));


        if (dto.getAmount() > order.getAmountRemaining()) {
            throw new IllegalArgumentException("Payment amount cannot be greater than the remaining amount on the order.");
        }
        if (dto.getPaymentMethod() == PaymentMethod.ESPECE && dto.getAmount() > MAX_CASH_AMOUNT) {
            throw new IllegalArgumentException("Cash payments cannot exceed " + MAX_CASH_AMOUNT + " MAD.");
        }


        long existingPayments = paymentRepository.countByOrderId(order.getId());
        int paymentNumber = (int) existingPayments + 1;

        Payment payment = paymentMapper.toEntity(dto);
        payment.setOrder(order);
        payment.setPaymentNumber(paymentNumber);
        payment.setPaymentDate(dto.getPaymentDate() != null ? dto.getPaymentDate() : LocalDate.now());

        // Set initial status
        if (dto.getPaymentMethod() == PaymentMethod.ESPECE) {
            payment.setStatus(PaymentStatus.ENCAISSE);
            payment.setCashInDate(LocalDate.now());
        } else {
            payment.setStatus(PaymentStatus.EN_ATTENTE);
        }

        Payment savedPayment = paymentRepository.save(payment);

        // Update order's remaining amount
        order.setAmountRemaining(order.getAmountRemaining() - dto.getAmount());
        orderRepository.save(order);

        return paymentMapper.toDto(savedPayment);
    }

    @Override
    @Transactional
    public PaymentResponseDto cashInPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + paymentId));

        if (payment.getStatus() != PaymentStatus.EN_ATTENTE) {
            throw new IllegalStateException("Only payments with status EN_ATTENTE can be cashed in.");
        }

        payment.setStatus(PaymentStatus.ENCAISSE);
        payment.setCashInDate(LocalDate.now());
        return paymentMapper.toDto(paymentRepository.save(payment));
    }

    @Override
    @Transactional
    public PaymentResponseDto rejectPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + paymentId));

        if (payment.getStatus() != PaymentStatus.EN_ATTENTE) {
            throw new IllegalStateException("Only payments with status EN_ATTENTE can be rejected.");
        }

        payment.setStatus(PaymentStatus.REJETE);


        Order order = payment.getOrder();
        order.setAmountRemaining(order.getAmountRemaining() + payment.getAmount());
        orderRepository.save(order);

        return paymentMapper.toDto(paymentRepository.save(payment));
    }
}