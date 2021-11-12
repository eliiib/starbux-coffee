package com.starbux.coffee.service.impl;


import com.starbux.coffee.domain.Topping;
import com.starbux.coffee.exception.ToppingNotFoundException;
import com.starbux.coffee.repository.ToppingRepository;
import com.starbux.coffee.service.ToppingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ToppingServiceImpl implements ToppingService {

    private final ToppingRepository toppingRepository;


    @Override
    public Topping createTopping(String name, Double amount) {
        log.info("create topping with name: {} and amount: {}", name, amount);
        Topping topping = Topping.builder()
                .name(name)
                .amount(amount)
                .createDate(LocalDateTime.now())
                .build();
        toppingRepository.save(topping);
        return topping;
    }

    @Override
    public Topping updateTopping(Long id, String name, Double amount) {
        log.info("Update topping with id: {} to name: {} and amount: {}", id, name, amount);
        return toppingRepository.findById(id).map(
                topping -> {
                    topping.setName(name);
                    topping.setAmount(amount);
                    return topping;
                }
        ).orElseThrow(() -> new ToppingNotFoundException("Topping with id: " + id + " not found!"));
    }

    @Override
    public void deleteTopping(Long id) {
        log.info("Delete topping with id: {}", id);
        Topping topping = toppingRepository.findById(id).orElseThrow(() -> new ToppingNotFoundException("There is no topping with this id"));
        topping.setIsDeleted(true);
        toppingRepository.save(topping);
    }

    @Override
    public Topping findToppingByName(String name) {
        log.info("Try to find topping with name: {}", name);
        return toppingRepository.findByName(name).orElseThrow(() -> new ToppingNotFoundException("Topping with name: " + name + " not found!"));
    }
}
