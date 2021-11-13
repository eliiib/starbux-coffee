package com.starbux.coffee.controller.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@Builder
public class AddToBasketRequest {

    @NotBlank
    @ApiModelProperty(notes = "The product name of a new order item")
    private String productName;

    @NotEmpty
    @ApiModelProperty(notes = "List of toppings of a new order item")
    private List<String> toppingNames;
}
