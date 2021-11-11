package com.starbux.coffee.controller.topping;


import com.starbux.coffee.domain.Topping;
import com.starbux.coffee.service.ToppingService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/toppings")
public class ToppingResource {

    private final ToppingService toppingService;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create a new topping", response = CreateToppingResponse.class)
    public ResponseEntity<CreateToppingResponse> createTopping(@Valid @RequestBody CreateToppingRequest request) {
        Topping topping = toppingService.createTopping(request.getName(), request.getAmount());

        return ResponseEntity.ok(CreateToppingResponse.builder()
                .name(topping.getName())
                .amount(topping.getAmount())
                .build());
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update an existing topping", response = CreateToppingResponse.class)
    public ResponseEntity<CreateToppingResponse> updateTopping(@PathVariable("id") String id, @Valid @RequestBody CreateToppingRequest request) {
        Topping topping = toppingService.updateTopping(Long.parseLong(id), request.getName(), request.getAmount());

        return ResponseEntity.ok(CreateToppingResponse.builder()
                .name(topping.getName())
                .amount(topping.getAmount())
                .build());
    }

    @DeleteMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete a topping")
    public ResponseEntity<?> deleteTopping(@PathVariable("id") String id) {
        toppingService.deleteTopping(Long.parseLong(id));
        return ResponseEntity.ok().build();
    }



}
