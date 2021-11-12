package com.starbux.coffee.service.impl;


import com.starbux.coffee.domain.Product;
import com.starbux.coffee.exception.ProductNotFoundException;
import com.starbux.coffee.repository.ProductRepository;
import com.starbux.coffee.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;


    @Override
    public Product createProduct(String name, Double amount) {
        return productRepository.save(Product.builder()
                .name(name)
                .amount(amount)
                .createDate(LocalDateTime.now())
                .build());
    }

    @Override
    public Product updateProduct(Long id, String name, Double amount) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("There is no product with this id"));
        product.setName(name);
        product.setAmount(amount);
        return product;
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("There is no product with this id"));
        product.setIsDeleted(true);
        productRepository.save(product);
    }

    @Override
    public Product findProductByName(String name) {
        return productRepository.findByName(name).orElseThrow(() -> new ProductNotFoundException("There is no product with this name"));
    }
}
