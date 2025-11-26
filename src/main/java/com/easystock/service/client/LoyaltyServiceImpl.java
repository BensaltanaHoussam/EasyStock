package com.easystock.service.client;

import com.easystock.entity.Client;
import com.easystock.entity.enums.CustomerTier;
import com.easystock.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoyaltyServiceImpl implements LoyaltyService {

    private final ClientRepository clientRepository;

    @Override
    public CustomerTier calculateNewTier(int totalOrders, double totalSpent) {
        if (totalOrders >= 20 || totalSpent >= 15000) {
            return CustomerTier.PLATINUM;
        }
        if (totalOrders >= 10 || totalSpent >= 5000) {
            return CustomerTier.GOLD;
        }
        if (totalOrders >= 3 || totalSpent >= 1000) {
            return CustomerTier.SILVER;
        }
        return CustomerTier.BASIC;
    }

    @Override
    public double calculateDiscount(CustomerTier currentTier, double orderSubtotal) {
        double discountPercentage = 0.0;

        switch (currentTier) {
            case PLATINUM:
                if (orderSubtotal >= 1200) {
                    discountPercentage = 0.15;
                }
                break;
            case GOLD:
                if (orderSubtotal >= 800) {
                    discountPercentage = 0.10;
                }
                break;
            case SILVER:
                if (orderSubtotal >= 500) {
                    discountPercentage = 0.05;
                }
                break;
            case BASIC:
            default:
                break;
        }

        return orderSubtotal * discountPercentage;
    }

    @Override
    public void updateClientTier(Client client) {
        CustomerTier currentTier = client.getLoyaltyTier();
        CustomerTier newTier = calculateNewTier(client.getTotalOrders(), client.getTotalSpent());

        if (currentTier != newTier) {
            client.setLoyaltyTier(newTier);
            clientRepository.save(client);
        }
    }
}