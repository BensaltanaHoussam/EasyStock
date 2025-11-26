package com.easystock.service.order;

import com.easystock.dto.order.OrderRequestDto;
import com.easystock.dto.order.OrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto requestDto);
    OrderResponseDto findOrderById(Long id);
    Page<OrderResponseDto> findAllOrders(Pageable pageable);
    OrderResponseDto confirmOrder(Long id);
    OrderResponseDto cancelOrder(Long id);
}