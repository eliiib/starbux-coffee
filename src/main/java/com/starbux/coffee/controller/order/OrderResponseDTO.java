package com.starbux.coffee.controller.order;

import com.starbux.coffee.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {

    @Getter
    @Builder
    public static class OrderItem {
        private Product product;
    }

    private Double totalAmount;
    private Double discount;
    private Double paymentAmount;
    private List<OrderItem> orderItems;
}
