package com.starbux.coffee.controller.topping;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateToppingRequest {

    @NotBlank
    private String name;

    @NotNull
    private Double amount;
}
