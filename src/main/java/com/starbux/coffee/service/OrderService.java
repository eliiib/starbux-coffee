package com.starbux.coffee.service;

import com.starbux.coffee.domain.Order;

import java.util.List;

public interface OrderService {

    Order addToBasket(String customerId, String productName, List<String> toppingNames);

    Order checkout(String customerId);

    Double getCustomerTotalOrdersAmount(String customerId);
}
