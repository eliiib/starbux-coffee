package com.starbux.coffee.Service.impl;


import com.starbux.coffee.Repository.ToppingRepository;
import com.starbux.coffee.Service.ToppingService;
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
        ).orElseThrow(RuntimeException::new);
    }

    @Override
    public void deleteTopping(Long id) {
        toppingRepository.findById(id).ifPresent(
                product ->
                        product.setIsDeleted(true)
        );
    }
}
