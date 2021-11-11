package com.starbux.coffee.controller.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
public class AddToBasketRequest {

    @NotBlank
    @ApiModelProperty(notes = "The product id of a new order item")
    private String productId;

    @NotEmpty
    @ApiModelProperty(notes = "List of toppings of a new order item")
    private List<String> toppingIds;
}
