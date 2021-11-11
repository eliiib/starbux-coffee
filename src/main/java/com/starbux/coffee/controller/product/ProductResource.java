package com.starbux.coffee.controller.product;


import com.starbux.coffee.service.ProductService;
import com.starbux.coffee.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductResource {

    private final ProductService productService;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CreateProductResponse createProduct(@Valid @RequestBody CreateProductRequest request) {
        Product product = productService.createProduct(request.getName(), request.getAmount());

        return CreateProductResponse.builder()
                .name(product.getName())
                .amount(product.getAmount())
                .build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CreateProductResponse updateProduct(@PathVariable("id") String id, @RequestBody CreateProductRequest request) {
        Product product = productService.updateProduct(Long.parseLong(id), request.getName(), request.getAmount());

        return CreateProductResponse.builder()
                .name(product.getName())
                .amount(product.getAmount())
                .build();
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable("id") String id) {
        productService.deleteProduct(Long.parseLong(id));
    }
}
