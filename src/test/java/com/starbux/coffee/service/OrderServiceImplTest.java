package com.starbux.coffee.service;

import com.starbux.coffee.domain.Order;
import com.starbux.coffee.domain.OrderItem;
import com.starbux.coffee.domain.Product;
import com.starbux.coffee.domain.Topping;
import com.starbux.coffee.repository.OrderItemRepository;
import com.starbux.coffee.repository.OrderRepository;
import com.starbux.coffee.service.impl.OrderServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(SpringExtension.class)
@Import(OrderServiceImpl.class)
public class OrderServiceImplTest {

    @Autowired
    private OrderServiceImpl orderService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private OrderItemRepository orderItemRepository;

    @MockBean
    private ProductService productService;

    @MockBean
    private ToppingService toppingService;

    @Test
    @DisplayName("Add new order item to existing order, order item should be added")
    public void addProductToExistingOrder_validProduct_productAdded() {
        String customerId = "123456";
        Order order = createSampleOrder();
//        Mockito.doReturn(Optional.of(order)).when(orderRepository).findByCustomerIdAndStatusTypeEquals(customerId, Order.StatusType.IN_PROGRESS);
//        orderService.addToBasket(customerId, createSampleProduct(), toppings());

    }

    @Test
    @DisplayName("Calculate order item amount, amount should be correct")
    public void creatNewOrderItem_validInput_addedNewOrderItem() {
        Order order = createSampleOrder();
        Product product = createSampleProduct();
        List<Topping> toppings = toppings();
        Mockito.doReturn(product).when(productService).findProductById(product.getId());
        Mockito.doReturn(toppings.get(0)).when(toppingService).findToppingById(toppings.get(0).getId());
        Mockito.doReturn(toppings.get(1)).when(toppingService).findToppingById(toppings.get(1).getId());
        Mockito.doReturn(toppings.get(2)).when(toppingService).findToppingById(toppings.get(2).getId());
        double orderItemAmount = orderService.calculateOrderItemAmount(createSampleProduct(), toppings());
        Assertions.assertThat(orderItemAmount).isEqualTo(14D);
    }


    @Test
    @DisplayName("Calculate order item amount, amount should be correct")
    public void calculateOrderItemAmount_validInput_correctAmount() {
        double orderItemAmount = orderService.calculateOrderItemAmount(createSampleProduct(), toppings());
        Assertions.assertThat(orderItemAmount).isEqualTo(14D);
    }

    private Order createSampleOrder() {
        Set<OrderItem> orderItems = new HashSet<>();
        orderItems.add(createSampleOrderItem());
        return Order.builder()
                .id(10L)
                .customerId("123456")
                .totalAmount(7D)
                .discount(0D)
                .orderItems(orderItems)
                .statusType(Order.StatusType.IN_PROGRESS)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
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

    private Topping createSampleTopping() {
        return Topping.builder()
                .id(12L)
                .name("Milk")
                .amount(2D)
                .createDate(LocalDateTime.now())
                .isDeleted(false)
                .build();
    }

    private OrderItem createSampleOrderItem(){
        Set<Topping> toppings = new HashSet<>();
        toppings.add(createSampleTopping());
        return OrderItem.builder()
                .id(10L)
                .product(createSampleProduct())
                .toppings(toppings)
                .amount(7D)
                .build();
    }

    private List<Topping> toppings() {
        Topping milk = Topping.builder()
                .id(10L)
                .name("Milk")
                .amount(2D)
                .createDate(LocalDateTime.now())
                .isDeleted(false)
                .build();

        Topping chocolate = Topping.builder()
                .id(11L)
                .name("Chocolate Sauce")
                .amount(5D)
                .createDate(LocalDateTime.now())
                .isDeleted(false)
                .build();

        Topping lemon = Topping.builder()
                .id(12L)
                .name("Lemon")
                .amount(2D)
                .createDate(LocalDateTime.now())
                .isDeleted(false)
                .build();

        List<Topping> toppings = new ArrayList<>();
        toppings.add(milk);
        toppings.add(chocolate);
        toppings.add(lemon);
        return toppings;
    }
}
