package com.starbux.coffee.controller.report;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MostUsedToppingDTO {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MostUsedTopping {
        private String productName;
        private String toppingName;
    }

    private List<MostUsedTopping> mostUsedToppings;
}
