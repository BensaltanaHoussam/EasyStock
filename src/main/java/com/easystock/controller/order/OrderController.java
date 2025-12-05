package com.easystock.controller.order;

import com.easystock.config.auth.Auth;
import com.easystock.dto.order.OrderRequestDto;
import com.easystock.dto.order.OrderResponseDto;
import com.easystock.entity.enums.UserRole;
import com.easystock.service.order.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Auth(allowedRoles = {UserRole.ADMIN})
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody @Valid OrderRequestDto requestDto) {
        log.info("Request to create order for client ID: {}", requestDto.getClientId());
        OrderResponseDto response = orderService.createOrder(requestDto);
        log.info("Order created successfully with ID: {}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Auth(allowedRoles = {UserRole.ADMIN, UserRole.CLIENT})
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long id) {
        log.info("Request to get order by ID: {}", id);
        return ResponseEntity.ok(orderService.findOrderById(id));
    }

    @GetMapping
    @Auth(allowedRoles = {UserRole.ADMIN})
    public ResponseEntity<Page<OrderResponseDto>> getAllOrders(Pageable pageable) {
        log.info("Request to get all orders for page: {}", pageable.getPageNumber());
        return ResponseEntity.ok(orderService.findAllOrders(pageable));
    }

    @PostMapping("/{id}/confirm")
    @Auth(allowedRoles = {UserRole.ADMIN})
    public ResponseEntity<OrderResponseDto> confirmOrder(@PathVariable Long id) {
        log.info("Request to confirm order with ID: {}", id);
        OrderResponseDto response = orderService.confirmOrder(id);
        log.info("Order with ID: {} confirmed successfully", response.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/cancel")
    @Auth(allowedRoles = {UserRole.ADMIN})
    public ResponseEntity<OrderResponseDto> cancelOrder(@PathVariable Long id) {
        log.info("Request to cancel order with ID: {}", id);
        OrderResponseDto response = orderService.cancelOrder(id);
        log.info("Order with ID: {} canceled successfully", response.getId());
        return ResponseEntity.ok(response);
    }
}
