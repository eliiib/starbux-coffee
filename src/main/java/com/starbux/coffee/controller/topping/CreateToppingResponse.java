package com.starbux.coffee.controller.topping;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateToppingResponse {

    private String name;
    private Double amount;
    private String createDate;
}
