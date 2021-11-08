package com.starbux.coffee.Service.impl;


import com.starbux.coffee.Repository.ProductRepository;
import com.starbux.coffee.Service.ProductService;
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
}
