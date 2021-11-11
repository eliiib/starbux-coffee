package com.starbux.coffee.controller.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateProductRequest {

    @NotBlank
    @ApiModelProperty(notes = "Name of product")
    private String name;

    @NotNull
    @ApiModelProperty(notes = "Amount of product")
    private Double amount;

}
