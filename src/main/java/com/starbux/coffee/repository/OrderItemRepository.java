package com.starbux.coffee.repository;

import com.starbux.coffee.domain.Order;
import com.starbux.coffee.domain.OrderItem;
import com.starbux.coffee.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findAllByOrder(Order order);

    List<OrderItem> findAllByProduct(Product product);
}
