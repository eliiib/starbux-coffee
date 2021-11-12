package com.starbux.coffee.controller.order;

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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TestOrderResourceIT {

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
	@DisplayName("When the inputs are correct, a new order should be created for customer")
	public void createOrder_validInputs_orderCreated() {
		AddToBasketRequest request = AddToBasketRequest.builder()
				.productName("Latte")
				.toppingNames(Arrays.asList("Milk", "Chocolate Sauce", "Lemon"))
				.build();


		HttpHeaders headers = new HttpHeaders();
		headers.set("customerId", "123456");

		HttpEntity<AddToBasketRequest> httpEntity = new HttpEntity<>(request, headers);
		ResponseEntity<OrderResponseDTO> response = restTemplate.exchange(createUrl("/orders/basket"), HttpMethod.POST,
				httpEntity, OrderResponseDTO.class);
		assertThat(response.getStatusCode().value()).isEqualTo(200);
		assertThat(response.getBody().getTotalAmount()).isEqualTo(14D);
		assertThat(response.getBody().getDiscount()).isEqualTo(3.5);
		assertThat(response.getBody().getPaymentAmount()).isEqualTo(10.5);

		List<Order> savedOrder = orderRepository.findAll();
		assertThat(savedOrder).isNotEmpty();
		assertThat(savedOrder.get(0).getCustomerId()).isEqualTo("123456");
	}

	@Test
	@DisplayName("When the product name is empty in the request, the response should be 400")
	public void createOrder_invalidInputs_shouldreturnBadRequest() {
		AddToBasketRequest request = AddToBasketRequest.builder()
				.toppingNames(Arrays.asList("Milk", "Chocolate Sauce", "Lemon"))
				.build();

		HttpHeaders headers = new HttpHeaders();
		headers.set("customerId", "123456");

		HttpEntity<AddToBasketRequest> httpEntity = new HttpEntity<>(request, headers);
		ResponseEntity<OrderResponseDTO> response = restTemplate.exchange(createUrl("/orders/basket"), HttpMethod.POST,
				httpEntity, OrderResponseDTO.class);
		assertThat(response.getStatusCode().value()).isEqualTo(400);
	}

	@Test
	@DisplayName("When topping list is empty in the request, the response should be 400")
	public void createProduct_nullAmount_shouldReturnBadRequest() {
		AddToBasketRequest request = AddToBasketRequest.builder()
				.productName("Latte")
				.build();

		HttpHeaders headers = new HttpHeaders();
		headers.set("customerId", "123456");

		HttpEntity<AddToBasketRequest> httpEntity = new HttpEntity<>(request, headers);
		ResponseEntity<OrderResponseDTO> response = restTemplate.exchange(createUrl("/orders/basket"), HttpMethod.POST,
				httpEntity, OrderResponseDTO.class);
		assertThat(response.getStatusCode().value()).isEqualTo(400);
	}

	@Test
	@DisplayName("When order is checked out, in progress status should be completed")
	public void checkoutOrder_validInputs_orderCompleted() {
		initDB();

		HttpHeaders headers = new HttpHeaders();
		headers.set("customerId", "123456");

		HttpEntity<AddToBasketRequest> httpEntity = new HttpEntity<>(headers);
		ResponseEntity<OrderResponseDTO> response = restTemplate.exchange(createUrl("/orders/checkout"), HttpMethod.POST,
				httpEntity, OrderResponseDTO.class);
		assertThat(response.getStatusCode().value()).isEqualTo(200);
		assertThat(response.getBody().getTotalAmount()).isEqualTo(21D);
		assertThat(response.getBody().getDiscount()).isEqualTo(7D);
		assertThat(response.getBody().getPaymentAmount()).isEqualTo(14D);

		List<Order> savedOrder = orderRepository.findAll();
		assertThat(savedOrder).isNotEmpty();
		assertThat(savedOrder.get(0).getCustomerId()).isEqualTo("123456");
		assertThat(savedOrder.get(0).getStatusType()).isEqualTo(Order.StatusType.COMPLETED);
	}

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

	private Topping createSampleTopping() {
		return Topping.builder()
				.name("Milk")
				.amount(2D)
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
		orderItemRepository.saveAll(orderItems);
	}
}
