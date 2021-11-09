package com.starbux.coffee.controller.topping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateToppingResponse {

    private String name;
    private Double amount;
    private String createDate;
}
