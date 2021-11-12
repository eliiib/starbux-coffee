package com.starbux.coffee.domain;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ORDER_")
public class Order {

    public enum StatusType {
        IN_PROGRESS, COMPLETED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String customerId;
    private double totalAmount;
    private double discount;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    @Enumerated(EnumType.STRING)
    private StatusType statusType;

    @Transient
    private Set<OrderItem> orderItems;

    @Transient
    private double paymentAmount;
}
