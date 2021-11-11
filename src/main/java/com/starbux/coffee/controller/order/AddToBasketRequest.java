package com.starbux.coffee.controller.order;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class AddToBasketRequest {

    @NotBlank
    private String productId;
    private List<String> toppingIds;
}
