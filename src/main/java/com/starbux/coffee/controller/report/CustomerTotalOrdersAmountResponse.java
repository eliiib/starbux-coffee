package com.starbux.coffee.controller.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTotalOrdersAmountResponse {

    private Double totalAmount;
}
