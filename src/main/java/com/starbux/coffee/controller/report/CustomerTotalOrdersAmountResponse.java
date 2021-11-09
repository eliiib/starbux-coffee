package com.starbux.coffee.controller.report;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomerTotalOrdersAmountResponse {

    private Double totalAmount;
}
