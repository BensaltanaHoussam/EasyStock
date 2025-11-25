package com.easystock.entity;

import com.easystock.entity.enums.CustomerTier;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "clients")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerTier loyaltyTier = CustomerTier.BASIC;

    // Statistics fields
    @Builder.Default
    private int totalOrders = 0;

    @Builder.Default
    private double totalSpent = 0.0;

    private LocalDate firstOrderDate;
    private LocalDate lastOrderDate;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;
}