package com.easystock.service.client;

import com.easystock.entity.Client;
import com.easystock.entity.enums.CustomerTier;

public interface LoyaltyService {

    CustomerTier calculateNewTier(int totalOrders, double totalSpent);


    double calculateDiscount(CustomerTier currentTier, double orderSubtotal);

    void updateClientTier(Client client);
}