# Starbux Coffee Service

## Info
**Context Path** : /starbux

**Swagger API** : /starbux/swagger-ui/

## Prerequisites: 
1) Java
2) Maven
3) Docker

## Run
```
mvn clean install
docker build -t starbux-coffee:latest .
docker run -d --name starbux-coffee -p 8080:8080 starbux-coffee:latest
```

## Assumptions:
- There is an gateway service by which customer could be authenticated and customerId fetched and passed to Starbux-coffee service

## Domain Models:
### Product:
- Contains product info
### Topping:
- Contains topping info
### Order:
- Contains order info for customer
- Each customer could have many orders
- Every order could have many order items

## Sample Api Calls:
### Add Product:
```
curl --location --request POST 'localhost:8080/starbux/products' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Mocha",
    "amount": 6
}'
```

### Add Topping:
```
curl --location --request POST 'localhost:8080/starbux/toppings' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name" : "Lemon",
    "amount": 2
}'
```

### Add to Order Item to Basket:
```
curl --location --request POST 'localhost:8080/starbux/orders/basket' \
--header 'customerId: 123456' \
--header 'Content-Type: application/json' \
--data-raw '{
    "productName": "Mocha",
    "toppingNames": ["Lemon"]
}'
```

### Checkout Order:
```
curl --location --request POST 'localhost:8080/starbux/orders/checkout' \
--header 'customerId: 123456' \
--data-raw ''
```
