package com.starbux.coffee.controller.topping;


import com.starbux.coffee.service.ToppingService;
import com.starbux.coffee.domain.Topping;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/toppings")
public class ToppingResource {

    private final ToppingService toppingService;


    @PostMapping
    public ResponseEntity<CreateToppingResponse> createTopping(@RequestBody CreateToppingRequest request) {
        Topping topping = toppingService.createTopping(request.getName(), request.getAmount());

        return ResponseEntity.ok(CreateToppingResponse.builder()
                .name(topping.getName())
                .amount(topping.getAmount())
                .build());
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
    public ResponseEntity<?> deleteProduct(@PathVariable("id") String id) {
        toppingService.deleteTopping(Long.parseLong(id));
        return ResponseEntity.ok().build();
    }



}
