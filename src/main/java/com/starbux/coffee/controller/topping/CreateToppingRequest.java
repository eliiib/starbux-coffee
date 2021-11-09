package com.starbux.coffee.controller.topping;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateToppingRequest {

    private String name;
    private Double amount;
}
