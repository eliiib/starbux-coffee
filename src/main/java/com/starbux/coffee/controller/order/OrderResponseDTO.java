package com.starbux.coffee.controller.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {

    @ApiModelProperty(notes = "Order total amount")
    private Double totalAmount;

    @ApiModelProperty(notes = "Order discount")
    private Double discount;

    @ApiModelProperty(notes = "Order payment amount")
    private Double paymentAmount;
}
