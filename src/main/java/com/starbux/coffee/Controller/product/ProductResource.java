package com.starbux.coffee.Controller.product;


import com.starbux.coffee.Service.ProductService;
import com.starbux.coffee.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductResource {

    private final ProductService productService;


    @PostMapping
    public CreateProductResponse createProduct(@RequestBody CreateProductRequest request) {
        Product product = productService.createProduct(request.getName(), request.getAmount());

        return CreateProductResponse.builder()
                .name(product.getName())
                .amount(product.getAmount())
                .build();
    }

    @PutMapping("/{id}")
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
