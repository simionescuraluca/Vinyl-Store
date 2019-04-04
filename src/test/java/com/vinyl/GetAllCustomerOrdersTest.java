package com.vinyl;

import com.vinyl.model.Product;
import com.vinyl.model.Purchase;
import com.vinyl.model.PurchaseProduct;
import com.vinyl.modelDTO.OrderListDTO;
import com.vinyl.repository.PurchaseProductRepository;
import com.vinyl.repository.PurchaseRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.HttpStatus.OK;

public class GetAllCustomerOrdersTest extends ManagerBaseIntegration {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private PurchaseProductRepository purchaseProductRepository;

    private Product product;
    private Purchase order;

    @Override
    public void setUp() {
        super.setUp();
        product = defaultEntitiesHelper.createProduct();
        order = purchaseSetup();
    }

    @Test
    public void testWhenOk() {
        ResponseEntity<OrderListDTO> response = setUpHeaderAndGetTheResponse();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(OK);
        Assertions.assertThat(response.getBody().getOrders().size()).isEqualTo(1);
        Assertions.assertThat(response.getBody().getOrders().get(0).getStatus()).isEqualTo(order.getStatus());

        Double expectedCost = 200.0;
        Assertions.assertThat(response.getBody().getOrders().get(0).getCost()).isEqualTo(expectedCost);
    }

    @Test
    public void testWhenUserIdIsInvalid() {
        ResponseEntity<?> response = setUpHeaderAndGetTheResponse("/users/" + 00 + "/orders");
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    protected ResponseEntity<OrderListDTO> setUpHeaderAndGetTheResponse() {
        return setUpHeaderAndGetTheResponse(getUrl());
    }

    private ResponseEntity<OrderListDTO> setUpHeaderAndGetTheResponse(String url) {
        HttpHeaders headers = tokenHeaderHelper.setupToken(token.getHash());
        ResponseEntity<OrderListDTO> response = trt.exchange(url, getMethod(), getHttpEntity(headers), OrderListDTO.class);

        return response;
    }

    @Override
    protected String getUrl() {
        return "/users/" + user.getId() + "/orders";
    }

    @Override
    protected HttpMethod getMethod() {
        return HttpMethod.GET;
    }

    private Purchase purchaseSetup() {
        Purchase purchase = defaultEntitiesHelper.createPurchase(user);
        PurchaseProduct pp = defaultEntitiesHelper.createPurchaseProduct(purchase, product);
        purchase.setProducts(Lists.newArrayList(pp));

        return purchaseRepository.save(purchase);
    }
}