package com.starbux.coffee.service;

import com.starbux.coffee.domain.Topping;

public interface ToppingService {

    Topping createTopping(String name, Double amount);

    Topping updateTopping(Long id, String name, Double amount);

    void deleteTopping(Long id);

    Topping findToppingByName(String name);
}
