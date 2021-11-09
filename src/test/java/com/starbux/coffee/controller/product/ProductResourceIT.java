package com.starbux.coffee.controller.product;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.starbux.coffee.domain.Product;
import com.starbux.coffee.repository.ProductRepository;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductResourceIT {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ProductRepository repository;
	

    @LocalServerPort
    private int serverPort;
    
	@BeforeEach
	public void init() {
		this.repository.deleteAll();
	}

	@Test
	@DisplayName("when the inputs are correct, a new product should be added")
	public void createProduct_validInputs_productCreated() {
		CreateProductRequest request = new CreateProductRequest();
		request.setName("Latte");
		request.setAmount(5D);
		HttpEntity<CreateProductRequest> httpEntity = new HttpEntity<>(request);
		ResponseEntity<CreateProductResponse> response = restTemplate.exchange(createUrl("/products"), HttpMethod.POST,
				httpEntity, CreateProductResponse.class);
		assertThat(response.getStatusCode().value()).isEqualTo(200);
		assertThat(response.getBody().getName()).isEqualTo("Latte");
		assertThat(response.getBody().getAmount()).isEqualTo(5D);

		List<Product> savedProduct = repository.findAll();
		assertThat(savedProduct).isNotEmpty();
		assertThat(savedProduct.get(0).getName()).isEqualTo("Latte");
		assertThat(savedProduct.get(0).getAmount()).isEqualTo(5D);
	}
	
	@Test
	@DisplayName("when the name is empty in the request, the response should be 400")
	public void createProduct_emptyName_shouldReturnBadRequest() {
		CreateProductRequest request = new CreateProductRequest();
		request.setAmount(5D);
		HttpEntity<CreateProductRequest> httpEntity = new HttpEntity<>(request);
		ResponseEntity<CreateProductResponse> response = restTemplate.exchange(createUrl("/products"), HttpMethod.POST,
				httpEntity, CreateProductResponse.class);
		assertThat(response.getStatusCode().value()).isEqualTo(400);
	}
	
	@Test
	@DisplayName("when the amount is empty in the request, the response should be 400")
	public void createProduct_nullAmount_shouldReturnBadRequest() {
		CreateProductRequest request = new CreateProductRequest();
		request.setName("Latte");
		HttpEntity<CreateProductRequest> httpEntity = new HttpEntity<>(request);
		ResponseEntity<CreateProductResponse> response = restTemplate.exchange(createUrl("/products"), HttpMethod.POST,
				httpEntity, CreateProductResponse.class);
		assertThat(response.getStatusCode().value()).isEqualTo(400);
	}
	
	private String createUrl(String address) {
		return new StringBuilder("http://localhost:")
				.append(serverPort).append("/").append(address).toString();
	}
	
}
