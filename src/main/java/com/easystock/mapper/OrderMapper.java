package com.easystock.mapper;

import com.easystock.dto.order.OrderResponseDto;
import com.easystock.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ClientMapper.class, ProductMapper.class})
public interface OrderMapper {
    OrderResponseDto toDto(Order order);
}