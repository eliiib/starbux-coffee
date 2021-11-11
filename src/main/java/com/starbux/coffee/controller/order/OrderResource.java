package com.starbux.coffee.controller.order;

import com.starbux.coffee.domain.Order;
import com.starbux.coffee.service.OrderService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderResource {

    private final OrderService orderService;


    @PostMapping(path = "/basket", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Add an item to basket", response = OrderResponseDTO.class)
    public ResponseEntity<OrderResponseDTO> addToBasket(@RequestHeader("customerId") final String customerId,
                                                        @Valid @RequestBody AddToBasketRequest request) {
        Order order = orderService.addToBasket(customerId, Long.parseLong(request.getProductId()),
                request.getToppingIds().stream().map(Long::parseLong).collect(Collectors.toList()));

        return ResponseEntity.ok(OrderResponseDTO.builder()
                .totalAmount(order.getTotalAmount())
                .discount(order.getDiscount())
                .paymentAmount(order.getPaymentAmount())
                .build());
    }


    @PutMapping(path = "checkout", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Checkout the basket", response = OrderResponseDTO.class)
    public ResponseEntity<OrderResponseDTO> checkout(@RequestHeader("customerId") String customerId) {
        Order order = orderService.checkout(customerId);

        return ResponseEntity.ok(OrderResponseDTO.builder()
                .totalAmount(order.getTotalAmount())
                .discount(order.getDiscount())
                .paymentAmount(order.getPaymentAmount())
                .build());
    }
}
