package com.starbux.coffee.controller.order;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddToBasketRequest {

    private String productId;
    private List<String> toppingIds;
}
