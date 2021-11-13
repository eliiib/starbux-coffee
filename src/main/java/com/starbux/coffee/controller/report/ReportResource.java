package com.starbux.coffee.controller.report;

import com.starbux.coffee.service.OrderService;
import com.starbux.coffee.service.ReportService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportResource {

    private final OrderService orderService;
    private final ReportService reportService;


    @GetMapping("/customers/total-amounts")
    @ApiOperation(value = "Get customer total orders amount", response = CustomerTotalOrdersAmountResponse.class)
    public ResponseEntity<CustomerTotalOrdersAmountResponse> getCustomerTotalOrdersAmount(@RequestParam("customerId") String customerId) {
        return ResponseEntity.ok(CustomerTotalOrdersAmountResponse.builder()
                .totalAmount(orderService.getCustomerTotalOrdersAmount(customerId))
                .build());
    }


    @GetMapping("/products/most-used-topping")
    @ApiOperation(value = "Get most used topping of a product", response = CustomerTotalOrdersAmountResponse.class)
    public ResponseEntity<MostUsedToppingDTO> getMostUsedTopping() {
        return ResponseEntity.ok(MostUsedToppingDTO.builder()
                .mostUsedToppings(reportService.mostUsedToppingReport().stream().map(
                        mostUsedToppingModel -> MostUsedToppingDTO.MostUsedTopping.builder()
                                .productName(mostUsedToppingModel.getProduct() != null ? mostUsedToppingModel.getProduct().getName() : StringUtils.EMPTY)
                                .toppingName(mostUsedToppingModel.getTopping() != null ? mostUsedToppingModel.getTopping().getName() : StringUtils.EMPTY)
                                .build()
                ).collect(Collectors.toList()))
                .build());
    }
}
