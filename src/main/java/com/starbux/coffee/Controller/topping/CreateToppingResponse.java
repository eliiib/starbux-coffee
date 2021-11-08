package com.starbux.coffee.Controller.topping;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateToppingResponse {

    private String name;
    private Double amount;
    private String createDate;
}
