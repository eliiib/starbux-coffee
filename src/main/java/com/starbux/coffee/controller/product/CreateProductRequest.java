package com.starbux.coffee.controller.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductRequest {

    private String name;
    private Double amount;

}
