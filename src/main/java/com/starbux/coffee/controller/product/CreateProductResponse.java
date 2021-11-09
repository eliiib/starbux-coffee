package com.starbux.coffee.controller.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductResponse {

    private String name;
    private Double amount;
    private String createDate;
}
