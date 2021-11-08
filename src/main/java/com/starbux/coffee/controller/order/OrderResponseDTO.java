package com.starbux.coffee.controller.order;

import com.starbux.coffee.domain.Product;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
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
