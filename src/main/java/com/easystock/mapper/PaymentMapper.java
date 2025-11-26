package com.easystock.mapper;

import com.easystock.dto.payment.PaymentRequestDto;
import com.easystock.dto.payment.PaymentResponseDto;
import com.easystock.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(source = "order.id", target = "orderId")
    PaymentResponseDto toDto(Payment payment);

    Payment toEntity(PaymentRequestDto dto);
}