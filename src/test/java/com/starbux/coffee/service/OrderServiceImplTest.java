package com.starbux.coffee.service;

import com.starbux.coffee.domain.Order;
import com.starbux.coffee.domain.OrderItem;
import com.starbux.coffee.domain.Product;
import com.starbux.coffee.domain.Topping;
import com.starbux.coffee.exception.NoInProgressOrderException;
import com.starbux.coffee.exception.ProductNotFoundException;
import com.starbux.coffee.exception.ToppingNotFoundException;
import com.starbux.coffee.repository.OrderItemRepository;
import com.starbux.coffee.repository.OrderRepository;
import com.starbux.coffee.service.impl.OrderServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @MockBean
    private Environment environment;

    @BeforeEach
    public void init() {
        Mockito.doReturn(12D).when(environment).getRequiredProperty("order.discount.min-value", Double.class);
        Mockito.doReturn(3).when(environment).getRequiredProperty("order.discount.min-count", Integer.class);
        Mockito.doReturn(25).when(environment).getRequiredProperty("order.discount.percent", Integer.class);
    }

    @Test
    @DisplayName("when an order item is being added to an existing order item amount, order items count should be increased")
    public void addNewOrderItemToExistingOrder_validInput_addedNewOrderItem() {
        String customerId = "elham";
        Order order = createSampleOrder(customerId);
        OrderItem orderItem = createSampleOrderItem(order);
        //amount = 5D
        Product product = createSampleProduct();
        //totalAmount = 9D
        List<Topping> toppings = toppings();

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);
        orderItems.add(OrderItem.builder().id(15L).amount(14D).toppings(new HashSet<>(toppings)).order(order).product(product).build());

        Mockito.doReturn(Optional.of(order)).when(orderRepository).findByCustomerIdAndStatusTypeEquals(customerId, Order.StatusType.IN_PROGRESS);
        Mockito.doReturn(product).when(productService).findProductByName("Latte");
        Mockito.doReturn(toppings.get(0)).when(toppingService).findToppingByName("Milk");
        Mockito.doReturn(toppings.get(1)).when(toppingService).findToppingByName("Chocolate Sauce");
        Mockito.doReturn(toppings.get(2)).when(toppingService).findToppingByName("Lemon");
        Mockito.doReturn(orderItems).when(orderItemRepository).findAllByOrder(order);

        orderService.addToBasket(customerId, "Latte", Arrays.asList("Milk", "Chocolate Sauce"));
        Assertions.assertThat(order.getTotalAmount()).isEqualTo(21D);
        Assertions.assertThat(order.getDiscount()).isEqualTo(5.25D);
    }


    @Disabled
    @Test
    @DisplayName("when an order item is being added to for a customer with no in progress order, a new order should be created")
    public void addNewOrderItemToNewOrder_validInput_addedNewOrder() {
        String customerId = "elham";
        Order order = Order.builder().build();
        //amount = 5D
        Product product = createSampleProduct();
        //totalAmount = 9D
        List<Topping> toppings = toppings();

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(OrderItem.builder().id(15L).amount(14D).toppings(new HashSet<>(toppings)).order(order).product(product).build());
        Mockito.doReturn(Optional.empty()).when(orderRepository).findByCustomerIdAndStatusTypeEquals(customerId, Order.StatusType.IN_PROGRESS);
        Mockito.doReturn(product).when(productService).findProductByName("Latte");
        Mockito.doReturn(toppings.get(0)).when(toppingService).findToppingByName("Milk");
        Mockito.doReturn(toppings.get(1)).when(toppingService).findToppingByName("Chocolate Sauce");
        Mockito.doReturn(toppings.get(2)).when(toppingService).findToppingByName("Lemon");
        Mockito.doReturn(orderItems).when(orderItemRepository).findAllByOrder(order);

        orderService.addToBasket(customerId, "Latte", Arrays.asList("Milk", "Chocolate Sauce"));
        Assertions.assertThat(order.getTotalAmount()).isEqualTo(14D);
        Assertions.assertThat(order.getDiscount()).isEqualTo(5.25D);
    }


    @Test
    @DisplayName("Test adding new order item with not existing product, then throw product not found exception")
    public void testAddOrderItem_InvalidProduct_ThrowException() {
        String customerId = "elham";
        Mockito.doThrow(new ProductNotFoundException()).when(productService).findProductByName("Latte");
        assertThrows(ProductNotFoundException.class, () -> orderService.addToBasket(customerId, "Latte", Arrays.asList("Milk", "Chocolate Sauce")));
    }

    @Test
    @DisplayName("Test adding new order item with not existing topping, then throw topping not found exception")
    public void testAddOrderItem_InvalidTopping_ThrowException() {
        String customerId = "elham";
        Mockito.doThrow(new ToppingNotFoundException()).when(toppingService).findToppingByName("Milk");
        assertThrows(ToppingNotFoundException.class, () -> orderService.addToBasket(customerId, "Latte", Arrays.asList("Milk", "Chocolate Sauce")));
    }

    @Test
    @DisplayName("when a customer calls checkout, an in progress order should be completed")
    public void testCheckoutOrder_existingOrder_completedOrder() {
        String customerId = "elham";
        Order order = createSampleOrder(customerId);
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(createSampleOrderItem(order));
        orderItems.add(createSampleOrderItem(order));

        Mockito.doReturn(Optional.of(order)).when(orderRepository).findByCustomerIdAndStatusTypeEquals(customerId, Order.StatusType.IN_PROGRESS);
        Mockito.doReturn(orderItems).when(orderItemRepository).findAllByOrder(order);

        orderService.checkout(customerId);
        Assertions.assertThat(order.getStatusType()).isEqualTo(Order.StatusType.COMPLETED);
        Assertions.assertThat(order.getTotalAmount()).isEqualTo(14D);
        Assertions.assertThat(order.getDiscount()).isEqualTo(3.5D);
    }

    @Test
    @DisplayName("when a customer calls checkout for an order with more than 3 order item, discount should be equals to less product amount")
    public void testCheckoutOrder_threeItem_lessProductAmountDiscount() {
        String customerId = "elham";
        Order order = createSampleOrder(customerId);
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(createSampleOrderItem(order));
        orderItems.add(createSampleOrderItem(order));
        OrderItem thirdOrderItem = createSampleOrderItem(order);
        thirdOrderItem.setAmount(12D);
        orderItems.add(thirdOrderItem);

        Mockito.doReturn(Optional.of(order)).when(orderRepository).findByCustomerIdAndStatusTypeEquals(customerId, Order.StatusType.IN_PROGRESS);
        Mockito.doReturn(orderItems).when(orderItemRepository).findAllByOrder(order);

        orderService.checkout(customerId);
        Assertions.assertThat(order.getStatusType()).isEqualTo(Order.StatusType.COMPLETED);
        Assertions.assertThat(order.getTotalAmount()).isEqualTo(26D);
        Assertions.assertThat(order.getDiscount()).isEqualTo(7D);
    }

    @Test
    @DisplayName("when a customer calls checkout with no in progress order, then throw exception")
    public void testCheckoutOrder_notInprogressOrder_ThrowException() {
        String customerId = "elham";
        Mockito.doReturn(Optional.empty()).when(orderRepository).findByCustomerIdAndStatusTypeEquals(customerId, Order.StatusType.IN_PROGRESS);
        assertThrows(NoInProgressOrderException.class, () -> orderService.checkout(customerId));
    }

    private Order createSampleOrder(String customerId) {
        return Order.builder()
                .id(10L)
                .customerId(customerId)
                .statusType(Order.StatusType.IN_PROGRESS)
                .createDate(LocalDateTime.now())
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

    private OrderItem createSampleOrderItem(Order order){
        Set<Topping> toppings = new HashSet<>();
        toppings.add(createSampleTopping());
        return OrderItem.builder()
                .id(10L)
                .product(createSampleProduct())
                .toppings(toppings)
                .amount(7D)
                .order(order)
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
