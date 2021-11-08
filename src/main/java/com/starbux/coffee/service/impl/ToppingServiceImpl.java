package com.starbux.coffee.service.impl;


import com.starbux.coffee.domain.Product;
import com.starbux.coffee.exception.ToppingNotFoundException;
import com.starbux.coffee.repository.ToppingRepository;
import com.starbux.coffee.service.ToppingService;
import com.starbux.coffee.domain.Topping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ToppingServiceImpl implements ToppingService {

    private final ToppingRepository toppingRepository;


    @Override
    public Topping createTopping(String name, Double amount) {
        return toppingRepository.save(Topping.builder()
                .name(name)
                .amount(amount)
                .createDate(LocalDateTime.now())
                .build());
    }

    @Override
    public Topping updateTopping(Long id, String name, Double amount) {
        return toppingRepository.findById(id).map(
                product -> {
                    product.setName(name);
                    product.setAmount(amount);
                    return product;
                }
        ).orElseThrow(ToppingNotFoundException::new);
    }

    @Override
    public void deleteTopping(Long id) {
        toppingRepository.findById(id).ifPresent(
                product ->
                        product.setIsDeleted(true)
        );
    }

    @Override
    public Topping findToppingById(Long id) {
        return toppingRepository.findById(id).orElseThrow(ToppingNotFoundException::new);
    }
}
