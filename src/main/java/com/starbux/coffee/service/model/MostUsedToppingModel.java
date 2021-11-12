package com.starbux.coffee.service.model;

import com.starbux.coffee.domain.Product;
import com.starbux.coffee.domain.Topping;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class MostUsedToppingModel {

    private Product product;
    private Topping topping;
}
