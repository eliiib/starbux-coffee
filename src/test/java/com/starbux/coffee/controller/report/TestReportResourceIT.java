package com.starbux.coffee.controller.report;

import com.starbux.coffee.domain.Order;
import com.starbux.coffee.domain.OrderItem;
import com.starbux.coffee.domain.Product;
import com.starbux.coffee.domain.Topping;
import com.starbux.coffee.repository.OrderItemRepository;
import com.starbux.coffee.repository.OrderRepository;
import com.starbux.coffee.repository.ProductRepository;
import com.starbux.coffee.repository.ToppingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TestReportResourceIT {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ToppingRepository toppingRepository;

	@LocalServerPort
	private int serverPort;

	@BeforeEach
	public void init() {
		this.orderItemRepository.deleteAll();
		this.orderRepository.deleteAll();
		this.productRepository.deleteAll();
		this.toppingRepository.deleteAll();
		this.productRepository.save(createSampleProduct());
		this.toppingRepository.saveAll(toppings());
	}

	@Test
	@DisplayName("When getting product most used topping is called, return list of products with its most used topping")
	public void testProductsMostUsedToppings() {
		initDB();
		ResponseEntity<MostUsedToppingDTO> response = restTemplate.getForEntity(createUrl("/reports/products/most-used-topping"), MostUsedToppingDTO.class);
		assertThat(response.getStatusCode().value()).isEqualTo(200);
		assertThat(Objects.requireNonNull(response.getBody()).getMostUsedToppings().size()).isEqualTo(1);
		assertThat(response.getBody().getMostUsedToppings().get(0).getProductName()).isEqualTo("Latte");
		assertThat(response.getBody().getMostUsedToppings().get(0).getToppingName()).isEqualTo("Milk");
	}

	@Test
	@DisplayName("When customer total order amount is called, retunr sum of the total amount of all completed order of customer")
	public void testCustomerTotalOrderAmountReport() {
		getCustomerTotalOrderAmountInitDB();
		String customerId = "123456";
		ResponseEntity<CustomerTotalOrdersAmountResponse> response = restTemplate.getForEntity(createUrl("/reports/customers/total-amounts?customerId=" + customerId), CustomerTotalOrdersAmountResponse.class);
		assertThat(response.getStatusCode().value()).isEqualTo(200);
		assertThat(Objects.requireNonNull(response.getBody()).getTotalAmount()).isEqualTo(42D);
	}

	@Test

	private String createUrl(String address) {
		return new StringBuilder("http://localhost:")
				.append(serverPort).append("/starbux").append(address).toString();
	}

	private Order createSampleOrder() {
		return Order.builder()
				.customerId("123456")
				.totalAmount(21D)
				.discount(0D)
				.statusType(Order.StatusType.IN_PROGRESS)
				.createDate(LocalDateTime.now())
				.updateDate(LocalDateTime.now())
				.build();
	}

	private Product createSampleProduct() {
		return Product.builder()
				.name("Latte")
				.amount(5D)
				.createDate(LocalDateTime.now())
				.isDeleted(false)
				.build();
	}

	private OrderItem createSampleOrderItem(Order order){
		Set<Topping> toppings = new HashSet<>();
		toppings.add(toppingRepository.findByName("Milk").orElse(null));
		return OrderItem.builder()
				.product(productRepository.findByName("Latte").orElse(null))
				.toppings(toppings)
				.amount(7D)
				.order(order)
				.build();
	}

	private OrderItem createSecondSampleOrderItem(Order order){
		Set<Topping> toppings = new HashSet<>();
		toppings.add(toppingRepository.findByName("Lemon").orElse(null));
		return OrderItem.builder()
				.product(productRepository.findByName("Latte").orElse(null))
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

	private void initDB() {
		Order order = orderRepository.save(createSampleOrder());

		Set<OrderItem> orderItems = new HashSet<>();
		orderItems.add(createSampleOrderItem(order));
		orderItems.add(createSampleOrderItem(order));
		orderItems.add(createSampleOrderItem(order));
		orderItems.add(createSecondSampleOrderItem(order));
		orderItemRepository.saveAll(orderItems);
	}

	private void getCustomerTotalOrderAmountInitDB() {
		Order order1 = createSampleOrder();
		order1.setStatusType(Order.StatusType.COMPLETED);
		orderRepository.save(order1);
		Set<OrderItem> orderItems = new HashSet<>();
		orderItems.add(createSampleOrderItem(order1));
		orderItems.add(createSampleOrderItem(order1));
		orderItemRepository.saveAll(orderItems);

		Order order2 = createSampleOrder();
		order2.setStatusType(Order.StatusType.COMPLETED);
		orderRepository.save(order2);
		Set<OrderItem> orderItems2 = new HashSet<>();
		orderItems2.add(createSampleOrderItem(order2));
		orderItems2.add(createSampleOrderItem(order2));
		orderItemRepository.saveAll(orderItems2);

		Order order3 = orderRepository.save(createSampleOrder());
		Set<OrderItem> orderItems3 = new HashSet<>();
		orderItems3.add(createSampleOrderItem(order3));
		orderItems3.add(createSampleOrderItem(order3));
		orderItemRepository.saveAll(orderItems3);
	}
}
