package com.starbux.coffee.controller.product;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateProductResponse {

    private String name;
    private Double amount;
    private String createDate;
}
