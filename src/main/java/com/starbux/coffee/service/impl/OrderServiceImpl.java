package com.starbux.coffee.service.impl;

import com.starbux.coffee.domain.Order;
import com.starbux.coffee.domain.OrderItem;
import com.starbux.coffee.domain.Product;
import com.starbux.coffee.domain.Topping;
import com.starbux.coffee.exception.NoInProgressOrderException;
import com.starbux.coffee.repository.OrderItemRepository;
import com.starbux.coffee.repository.OrderRepository;
import com.starbux.coffee.service.OrderService;
import com.starbux.coffee.service.ProductService;
import com.starbux.coffee.service.ToppingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.threads.TaskQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {

    @Value("${order.discount.percent}")
    private String discountPercentage;

    @Value("${order.discount.min-value}")
    private String minValue;

    @Value("${order.discount.min-count}")
    private String minCount;

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductService productService;
    private final ToppingService toppingService;


    @Override
    public Order addToBasket(String customerId, String productName, List<String> toppingNames) {
        log.info("Add product: {} to order of customer: {}", productName, customerId);
        return orderRepository.findByCustomerIdAndStatusTypeEquals(customerId, Order.StatusType.IN_PROGRESS).map(
                order -> {
                    log.info("Update existing order: {} with product: {} for customer: {}", order.getId(), productName, customerId);
                    addNewItem(order, productName, toppingNames);
                    return order;
                }
        ).orElseGet(() -> {
            log.info("Create new order for customer: {} with product: {}", customerId, productName);
            Order order = createNewOrder(customerId);
            addNewItem(order, productName, toppingNames);
            return order;
        });
    }

    @Override
    public Order checkout(String customerId) {
        log.info("Checkout basket for customer: {}", customerId);
        Order order = orderRepository.findByCustomerIdAndStatusTypeEquals(customerId, Order.StatusType.IN_PROGRESS)
                .orElseThrow(() -> new NoInProgressOrderException("There is not any in progress order to checkout!"));

        order.setStatusType(Order.StatusType.COMPLETED);
        order.setTotalAmount(calculateTotalAmount(order));
        order.setDiscount(calculateDiscount(order));
        order.setPaymentAmount(calculatePaymentAmount(order));
        return order;
    }

    @Override
    public Double getCustomerTotalOrdersAmount(String customerId) {
        return orderRepository.sumTotalAmountByCustomerIdAndStatusType(customerId, Order.StatusType.COMPLETED);
    }

    public Order addNewItem(Order order, String productName, List<String> toppingName) {
        createNewOrderItem(order, productName, toppingName);
        order.setTotalAmount(calculateTotalAmount(order));
        order.setDiscount(calculateDiscount(order));
        order.setPaymentAmount(calculatePaymentAmount(order));
        return order;
    }

    public OrderItem createNewOrderItem(Order order, String productName, List<String> toppingNames) {
        Product product = productService.findProductByName(productName);
        List<Topping> toppings = toppingNames.stream().map(
                toppingService::findToppingByName
        ).collect(Collectors.toList());

        return orderItemRepository.save(
                OrderItem.builder()
                        .product(product)
                        .toppings(new HashSet<>(toppings))
                        .amount(calculateOrderItemAmount(product, toppings))
                        .order(order)
                        .build()
        );
    }

    private Order createNewOrder(String customerId) {
        return orderRepository.save(Order.builder()
                .customerId(customerId)
                .statusType(Order.StatusType.IN_PROGRESS)
                .createDate(LocalDateTime.now())
                .build());
    }

    public Double calculateOrderItemAmount(Product product, List<Topping> toppings) {
        return product.getAmount() + toppings.stream().mapToDouble(Topping::getAmount).sum();
    }

    private Double calculateTotalAmount(Order order) {
        return orderItemRepository.findAllByOrder(order).stream().mapToDouble(OrderItem::getAmount).sum();
    }

    private Double calculateDiscount(Order order) {
        double minValueDiscount = 0;
        double minCountDiscount = 0;
        double orderAmount = calculateTotalAmount(order);
        if(orderAmount > Double.parseDouble(minValue)) {
            minValueDiscount = orderAmount * Double.parseDouble(discountPercentage) / 100;
        }

        List<OrderItem> orderItems = orderItemRepository.findAllByOrder(order);
        if(orderItems.size() >= Double.parseDouble(minCount)) {
            minCountDiscount = orderItems.stream().mapToDouble(OrderItem::getAmount).min().orElse(0.0);
        }
        return Math.max(minValueDiscount, minCountDiscount);
    }

    private Double calculatePaymentAmount(Order order) {
        return order.getTotalAmount() - order.getDiscount();
    }
}
