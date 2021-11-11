package com.starbux.coffee.service;


import com.starbux.coffee.domain.Product;
import com.starbux.coffee.exception.ProductNotFoundException;
import com.starbux.coffee.repository.ProductRepository;
import com.starbux.coffee.service.impl.ProductServiceImpl;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@Import(ProductServiceImpl.class)
public class ProductServiceImplTest {

    @Autowired
    private ProductServiceImpl productService;

    @MockBean
    private ProductRepository productRepository;


    @Test
    @DisplayName("when delete product is called and the id is valid, the delete flag should set true")
    public void deleteProduct_validId_setDeleteFlag() {
        Product product = this.createSampleProduct();
        Mockito.doReturn(Optional.of(product)).when(productRepository).findById(12L);
        productService.deleteProduct(12L);

        assertThat(product.getIsDeleted()).isTrue();
        Mockito.verify(productRepository, times(1)).save(product);

    }


    @Test
    @DisplayName("Test deleting not existing product, then throw product not found exception")
    public void testDeleteProduct_productNoExist_ThrowException() {
        Mockito.doReturn(Optional.empty()).when(productRepository).findById(13L);
//        productService.deleteProduct(13L);
        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(13L));
    }

    private Product createSampleProduct() {
        return Product.builder()
                .id(12L)
                .name("Latte")
                .amount(5D)
                .createDate(LocalDateTime.now())
                .isDeleted(false)
                .build();
    }
}
