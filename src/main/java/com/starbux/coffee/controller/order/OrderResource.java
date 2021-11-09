package com.starbux.coffee.controller.order;

import com.starbux.coffee.domain.Order;
import com.starbux.coffee.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderResource {

    private final OrderService orderService;


    @PostMapping
    public ResponseEntity<OrderResponseDTO> addToBasket(@RequestHeader("customerId") final String customerId,
                                                        @RequestBody AddToBasketRequest request) {
        Order order = orderService.addToBasket(customerId, Long.parseLong(request.getProductId()),
                request.getToppingIds().stream().map(Long::parseLong).collect(Collectors.toList()));

        return ResponseEntity.ok(OrderResponseDTO.builder()
                .totalAmount(order.getTotalAmount())
                .discount(order.getDiscount())
                .paymentAmount(order.getPaymentAmount())
                .build());
    }


    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> checkout(@PathVariable("id") String id) {
        Order order = orderService.checkout(Long.parseLong(id));

        return ResponseEntity.ok(OrderResponseDTO.builder()
                .totalAmount(order.getTotalAmount())
                .discount(order.getDiscount())
                .paymentAmount(order.getPaymentAmount())
                .build());
    }
}
