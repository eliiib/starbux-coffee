package com.starbux.coffee.controller.product;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateProductRequest {

    @NotBlank
    private String name;

    @NotNull
    private Double amount;

}
