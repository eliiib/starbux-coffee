package com.starbux.coffee.domain;


import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(targetEntity = Product.class)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToMany
    private Set<Topping> toppings;

    private Double amount;

    @ManyToOne (targetEntity = Order.class)
    @JoinColumn(name = "order_id")
    private Order order;
}
