package com.starbux.coffee.Service;

import com.starbux.coffee.domain.Product;

public interface ProductService {
    Product createProduct(String name, Double amount);

    Product updateProduct(Long id, String name, Double amount);

    void deleteProduct(Long id);
}
