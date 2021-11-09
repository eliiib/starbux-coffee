package com.starbux.coffee.repository;

import com.starbux.coffee.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByCustomerIdAndStatusTypeEquals(String customerId, Order.StatusType statusType);


    @Query("select sum(o.totalAmount) from Order o where o.customerId = :customerId and o.statusType = :statusType")
    Double sumTotalAmountByCustomerIdAndStatusType(@Param("customerId") String customerId,
                                                   @Param("statusType") Order.StatusType statusType);
}
