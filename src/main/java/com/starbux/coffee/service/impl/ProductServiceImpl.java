package com.starbux.coffee.service.impl;


import com.starbux.coffee.repository.ProductRepository;
import com.starbux.coffee.service.ProductService;
import com.starbux.coffee.domain.Product;
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
        return productRepository.findById(id).map(
                product -> {
                    product.setName(name);
                    product.setAmount(amount);
                    return product;
                }
        ).orElseThrow(RuntimeException::new);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id).ifPresent(
                product ->
                        product.setIsDeleted(true)
        );
    }

    @Override
    public Product findProductById(Long id) {
        return productRepository.findById(id).orElseThrow();
    }
}
