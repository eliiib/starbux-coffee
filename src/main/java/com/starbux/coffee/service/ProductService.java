package com.starbux.coffee.service;

import com.starbux.coffee.domain.Product;

import java.util.List;

public interface ProductService {
    Product createProduct(String name, Double amount);

    Product updateProduct(Long id, String name, Double amount);

    void deleteProduct(Long id);

    Product findProductByName(String name);

    List<Product> getProducts();
}
