package com.starbux.coffee.service.impl;

import com.starbux.coffee.domain.OrderItem;
import com.starbux.coffee.domain.Product;
import com.starbux.coffee.domain.Topping;
import com.starbux.coffee.repository.OrderItemRepository;
import com.starbux.coffee.service.ProductService;
import com.starbux.coffee.service.ReportService;
import com.starbux.coffee.service.model.MostUsedToppingModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final OrderItemRepository orderItemRepository;
    private final ProductService productService;


    @Override
    public List<MostUsedToppingModel> mostUsedToppingReport() {
        List<Product> products = productService.getProducts();

        return products.stream().map(product -> {
                    List<OrderItem> orderItems = orderItemRepository.findAllByProduct(product);
                    List<Topping> toppings = new ArrayList<>();
                    orderItems.forEach(orderItem -> toppings.addAll(new ArrayList<Topping>(orderItem.getToppings())));

                    return MostUsedToppingModel.builder()
                            .product(product)
                            .topping(getMostUsedTopping(toppings))
                            .build();
                }
        )   .collect(Collectors.toList());
    }

    private Topping getMostUsedTopping(List<Topping> toppings){
        HashMap<Topping, Integer> frequencyMap = new HashMap<>();
        Topping mostUsedTopping = null;
        int maxCount = 0;

        for(Topping topping : toppings) {
            if(frequencyMap.containsKey(topping)) {
                frequencyMap.put(topping, frequencyMap.get(topping) +1 );
            }else{
                frequencyMap.put(topping, 1);
            }
        }

        for (Topping topping : frequencyMap.keySet()) {
            if(frequencyMap.get(topping) > maxCount){
                maxCount = frequencyMap.get(topping);
                mostUsedTopping = topping;
            }
        }
        return mostUsedTopping;
    }
}
