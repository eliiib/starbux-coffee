package com.starbux.coffee.controller.report;

import com.starbux.coffee.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportResource {

    private final OrderService orderService;


    @GetMapping("/customers/total-amounts")
    public ResponseEntity<CustomerTotalOrdersAmountResponse> getCustomerTotalOrdersAmount(@RequestParam("customerId") String customerId) {
        return ResponseEntity.ok(CustomerTotalOrdersAmountResponse.builder()
                .totalAmount(orderService.getCustomerTotalOrdersAmount(customerId))
                .build());
    }
}
