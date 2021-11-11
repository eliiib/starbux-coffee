package com.starbux.coffee.controller.topping;


import com.starbux.coffee.domain.Topping;
import com.starbux.coffee.repository.ToppingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestToppingResourceIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ToppingRepository toppingRepository;

    @LocalServerPort
    private int serverPort;

    @BeforeEach
    private void init() {
        this.toppingRepository.deleteAll();
    }

    @Test
    @DisplayName("When the inputs are correct, a new topping should be added")
    public void createTopping_validInput_toppingCreated() {
        CreateToppingRequest request = new CreateToppingRequest();
        request.setName("Milk");
        request.setAmount(2D);
        HttpEntity<CreateToppingRequest> httpEntity = new HttpEntity<>(request);
        ResponseEntity<CreateToppingResponse> response = restTemplate.exchange(createUrl("/toppings"), HttpMethod.POST,
                httpEntity, CreateToppingResponse.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody().getName()).isEqualTo("Milk");
        assertThat(response.getBody().getAmount()).isEqualTo(2D);

        List<Topping> savedTopping = toppingRepository.findAll();
        assertThat(savedTopping).isNotEmpty();
        assertThat(savedTopping.get(0).getName()).isEqualTo("Milk");
    }
    private String createUrl(String address) {
        return new StringBuilder("http://localhost:")
                .append(serverPort).append("/").append(address).toString();
    }

}
