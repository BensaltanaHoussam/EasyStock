package com.easystock.controller.order;

import com.easystock.config.auth.Auth;
import com.easystock.dto.order.OrderRequestDto;
import com.easystock.dto.order.OrderResponseDto;
import com.easystock.entity.enums.UserRole;
import com.easystock.service.order.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Auth(allowedRoles = {UserRole.ADMIN})
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody @Valid OrderRequestDto requestDto) {
        return new ResponseEntity<>(orderService.createOrder(requestDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Auth(allowedRoles = {UserRole.ADMIN, UserRole.CLIENT})
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findOrderById(id));
    }

    @GetMapping
    @Auth(allowedRoles = {UserRole.ADMIN})
    public ResponseEntity<Page<OrderResponseDto>> getAllOrders(Pageable pageable) {
        return ResponseEntity.ok(orderService.findAllOrders(pageable));
    }

    @PostMapping("/{id}/confirm")
    @Auth(allowedRoles = {UserRole.ADMIN})
    public ResponseEntity<OrderResponseDto> confirmOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.confirmOrder(id));
    }

    @PostMapping("/{id}/cancel")
    @Auth(allowedRoles = {UserRole.ADMIN})
    public ResponseEntity<OrderResponseDto> cancelOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }
}