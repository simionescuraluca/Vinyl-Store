package com.vinyl;

import com.vinyl.helper.TokenHeaderHelper;
import com.vinyl.model.Product;
import com.vinyl.model.Purchase;
import com.vinyl.model.PurchaseProduct;
import com.vinyl.modelDTO.StatusDTO;
import com.vinyl.repository.PurchaseRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

public class ChangeOrderStatusTest extends ManagerBaseIntegration {

    @Autowired
    protected TokenHeaderHelper tokenHeaderHelper;

    @Autowired
    protected PurchaseRepository purchaseRepository;

    protected Purchase order;

    protected Product product;

    protected StatusDTO status;

    @Override
    public void setUp() {
        super.setUp();
        product = defaultEntitiesHelper.createProduct();
        order = purchaseSetup();
        status = new StatusDTO();
        status.setStatus("SENT");
    }

    @Test
    public void testWhenOk() {
        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testWhenStatusIsInvalid(){
        status.setStatus("INVALID_STATUS");
        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testWhenOrderIdIsInvalid(){
        order.setId(00);
        purchaseRepository.save(order);
        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> setUpHeaderAndGetTheResponse() {
        HttpHeaders headers = tokenHeaderHelper.setupToken(token.getHash());
        ResponseEntity<?> response = trt.exchange("/orders/" + order.getId(), HttpMethod.PUT, new HttpEntity<>(status, headers), Void.class);

        return response;
    }

    private Purchase purchaseSetup() {
        Purchase purchase = defaultEntitiesHelper.createPurchase(user);
        PurchaseProduct pp = defaultEntitiesHelper.createPurchaseProduct(purchase, product);
        purchase.setProducts(Lists.newArrayList(pp));

        return purchaseRepository.save(purchase);
    }
}
