package com.starbux.coffee.service.impl;

import com.starbux.coffee.domain.Order;
import com.starbux.coffee.domain.OrderItem;
import com.starbux.coffee.domain.Product;
import com.starbux.coffee.domain.Topping;
import com.starbux.coffee.exception.OrderNotFoundException;
import com.starbux.coffee.repository.OrderItemRepository;
import com.starbux.coffee.repository.OrderRepository;
import com.starbux.coffee.service.OrderService;
import com.starbux.coffee.service.ProductService;
import com.starbux.coffee.service.ToppingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductService productService;
    private final ToppingService toppingService;


    @Override
    public Order addToBasket(String customerId, Long productId, List<Long> toppings) {
        return orderRepository.findByCustomerIdAndStatusTypeEquals(customerId, Order.StatusType.IN_PROGRESS).map(
                order -> addNewItem(order, productId, toppings)
        ).orElseGet(() -> {
            Order order = createNewOrder(customerId);
            addNewItem(order, productId, toppings);
            return order;
        });
    }

    @Override
    public Order checkout(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);

        if(order.getStatusType().equals(Order.StatusType.COMPLETED)) {
            log.info("Order has already been completed!");
        }

        order.setStatusType(Order.StatusType.COMPLETED);
        order.setTotalAmount(calculateTotalAmount(order));
        order.setDiscount(calculateDiscount(order));
        order.setPaymentAmount(order.getTotalAmount() - order.getDiscount());
        order.setOrderItems(new HashSet<>(orderItemRepository.findAllByOrder(order)));
        return order;
    }

    private Order addNewItem(Order order, Long productId, List<Long> toppings) {
        createNewOrderItem(order, productId, toppings);
        order.setTotalAmount(calculateTotalAmount(order));
        order.setOrderItems(new HashSet<>(orderItemRepository.findAllByOrder(order)));
        return order;
    }

    private OrderItem createNewOrderItem(Order order, Long productId, List<Long> toppingIds) {
        Product product = productService.findProductById(productId);
        List<Topping> toppings = toppingIds.stream().map(
                toppingService::findToppingById
        ).collect(Collectors.toList());

        return orderItemRepository.save(
                OrderItem.builder()
                        .product(productService.findProductById(productId))
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

    private Double calculateOrderItemAmount(Product product, List<Topping> toppings) {
        return product.getAmount() + toppings.stream().mapToDouble(Topping::getAmount).sum();
    }

    private Double calculateTotalAmount(Order order) {
        return orderItemRepository.findAllByOrder(order).stream().mapToDouble(OrderItem::getAmount).sum();
    }

    private Double calculateDiscount(Order order) {
        return 0.0;
    }

}
