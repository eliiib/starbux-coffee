package com.starbux.coffee.controller.report;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class MostUsedToppingDTO {

    @Getter
    @Setter
    @Builder
    public static class MostUsedTopping {
        private String productName;
        private String toppingName;
    }

    private List<MostUsedTopping> mostUsedToppings;
}
