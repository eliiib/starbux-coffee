package com.starbux.coffee.service;

import com.starbux.coffee.domain.Order;

import java.util.List;

public interface OrderService {

    Order addToBasket(String customerId, Long productId, List<Long> toppings);

    Order checkout(Long id);

    Double getCustomerTotalOrdersAmount(String customerId);
}
