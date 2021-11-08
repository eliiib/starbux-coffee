package com.starbux.coffee.repository;

import com.starbux.coffee.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByCustomerIdAndStatusTypeEquals(String customerId, Order.StatusType statusType);
}
