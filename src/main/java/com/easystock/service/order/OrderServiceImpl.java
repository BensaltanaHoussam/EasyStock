package com.easystock.service.order;

import com.easystock.dto.order.OrderRequestDto;
import com.easystock.dto.order.OrderResponseDto;
import com.easystock.entity.Client;
import com.easystock.entity.Order;
import com.easystock.entity.OrderItem;
import com.easystock.entity.Product;
import com.easystock.entity.enums.OrderStatus;
import com.easystock.mapper.OrderMapper;
import com.easystock.repository.ClientRepository;
import com.easystock.repository.OrderRepository;
import com.easystock.repository.ProductRepository;
import com.easystock.service.client.LoyaltyService;
import com.easystock.service.product.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Service implementation for managing Orders.
 * @author BensaltanaHoussam
 * @date 2025-11-26
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final LoyaltyService loyaltyService;
    private final OrderMapper orderMapper;

    @Value("${app.tva.rate}")
    private double tvaRate;

    @Override
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto requestDto) {

        Client client = clientRepository.findById(requestDto.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + requestDto.getClientId()));

        Order order = new Order();
        order.setClient(client);

        List<OrderItem> orderItems = new ArrayList<>();
        double subtotal = 0.0;


        for (var itemDto : requestDto.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + itemDto.getProductId()));

            if (product.isDeleted() || !productService.isStockAvailable(product.getId(), itemDto.getQuantity())) {
                order.setStatus(OrderStatus.REJECTED);
                orderRepository.save(order);
                throw new IllegalStateException("Product " + product.getName() + " is unavailable or out of stock.");
            }

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemDto.getQuantity())
                    .unitPrice(product.getUnitPrice()) // Capture price at time of order
                    .order(order)
                    .build();

            orderItems.add(orderItem);
            subtotal += orderItem.getUnitPrice() * orderItem.getQuantity();
        }

        order.setItems(orderItems);


        double loyaltyDiscount = loyaltyService.calculateDiscount(client.getLoyaltyTier(), subtotal);
        double promoDiscount = calculatePromoDiscount(requestDto.getPromoCode(), subtotal);
        double totalDiscount = loyaltyDiscount + promoDiscount;

        double htAfterDiscount = subtotal - totalDiscount;
        double taxAmount = htAfterDiscount * tvaRate;
        double total = htAfterDiscount + taxAmount;

        order.setSubtotal(subtotal);
        order.setDiscountAmount(totalDiscount);
        order.setTaxAmount(taxAmount);
        order.setTotal(total);
        order.setAmountRemaining(total); // Initially, the full amount is remaining
        order.setStatus(OrderStatus.PENDING);
        order.setPromoCode(requestDto.getPromoCode());

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    @Transactional
    public OrderResponseDto confirmOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));


        if (order.getAmountRemaining() > 0) {
            throw new IllegalStateException("Order cannot be confirmed because it is not fully paid. Amount remaining: " + order.getAmountRemaining());
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Only orders with PENDING status can be confirmed.");
        }


        for (OrderItem item : order.getItems()) {
            productService.decreaseStock(item.getProduct().getId(), item.getQuantity());
        }


        Client client = order.getClient();
        client.setTotalOrders(client.getTotalOrders() + 1);
        client.setTotalSpent(client.getTotalSpent() + order.getTotal());
        client.setLastOrderDate(LocalDate.now());
        if (client.getFirstOrderDate() == null) {
            client.setFirstOrderDate(LocalDate.now());
        }
        clientRepository.save(client);


        loyaltyService.updateClientTier(client);

        order.setStatus(OrderStatus.CONFIRMED);
        Order confirmedOrder = orderRepository.save(order);
        return orderMapper.toDto(confirmedOrder);
    }

    @Override
    public OrderResponseDto findOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
        return orderMapper.toDto(order);
    }

    @Override
    public Page<OrderResponseDto> findAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).map(orderMapper::toDto);
    }

    @Override
    @Transactional
    public OrderResponseDto cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));

        if (order.getStatus() == OrderStatus.CONFIRMED || order.getStatus() == OrderStatus.CANCELED) {
            throw new IllegalStateException("Cannot cancel an order that is already confirmed or canceled.");
        }

        order.setStatus(OrderStatus.CANCELED);
        Order canceledOrder = orderRepository.save(order);
        return orderMapper.toDto(canceledOrder);
    }


    private double calculatePromoDiscount(String promoCode, double subtotal) {
        if (Objects.equals(promoCode, "PROMO10")) {
            return subtotal * 0.10; // 10% discount
        }
        if (Objects.equals(promoCode, "50OFF")) {
            return 50.0; // Flat 50 DH discount
        }
        return 0.0;
    }
}