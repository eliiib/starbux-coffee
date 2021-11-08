package com.starbux.coffee.Controller.topping;


import com.starbux.coffee.Service.ToppingService;
import com.starbux.coffee.domain.Topping;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ToppingResource {

    private final ToppingService toppingService;


    @PostMapping
    public CreateToppingResponse createTopping(@RequestBody CreateToppingRequest request) {
        Topping topping = toppingService.createTopping(request.getName(), request.getAmount());

        return CreateToppingResponse.builder()
                .name(topping.getName())
                .amount(topping.getAmount())
                .build();
    }

    @PutMapping("/{id}")
    public CreateToppingResponse updateTopping(@PathVariable("id") String id, @RequestBody CreateToppingRequest request) {
        Topping topping = toppingService.updateTopping(Long.parseLong(id), request.getName(), request.getAmount());

        return CreateToppingResponse.builder()
                .name(topping.getName())
                .amount(topping.getAmount())
                .build();
    }

    @DeleteMapping("/{id}")
    public void createProduct(@PathVariable("id") String id) {
        toppingService.deleteTopping(Long.parseLong(id));
    }



}
