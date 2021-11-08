package com.starbux.coffee.controller.order;

import lombok.Getter;

import java.util.List;

@Getter
public class AddToBasketRequest {

    //TODO: move customerId to header
    private String customerId;
    private String productId;
    private List<String> toppingIds;
}
