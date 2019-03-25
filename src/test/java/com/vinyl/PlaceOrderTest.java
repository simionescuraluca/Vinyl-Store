package com.vinyl;

import com.vinyl.model.Cart;
import com.vinyl.model.Product;
import com.vinyl.model.ProductCart;
import com.vinyl.repository.CartRepository;
import com.vinyl.repository.ProductCartRepository;
import com.vinyl.repository.ProductRepository;
import com.vinyl.repository.PurchaseRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

public class PlaceOrderTest extends LoggedInBaseIntegration {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ProductCartRepository productCartRepository;

    @Autowired
    private ProductRepository productRepository;

    private Cart cart;
    private Product product;

    @Override
    public void setUp() {
        super.setUp();
        product = defaultEntitiesHelper.createProduct();
        cart = cartSetup();
    }

    @Test
    public void testWhenOk() {
        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(purchaseRepository.findByUser(user)).isNotNull();
    }

    @Test
    public void testWhenCartIsEmpty() {
        deleteCart();

        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testWhenStockIsTooSmall() {
        product.setStock(0);
        productRepository.save(product);

        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity setUpHeaderAndGetTheResponse() {
        HttpHeaders headers = tokenHeaderHelper.setupToken(token.getHash());
        ResponseEntity<?> response = trt.exchange(getUrl(), getMethod(), new HttpEntity<>(headers), Void.class);

        return response;
    }

    private Cart cartSetup() {
        Cart cart = defaultEntitiesHelper.createCart(user);
        ProductCart pc = defaultEntitiesHelper.createProductCart(cart, product);
        cart.setProducts(Lists.newArrayList(pc));

        return cartRepository.save(cart);
    }

    private void deleteCart() {
        productCartRepository.deleteAll();
        cartRepository.deleteAll();
    }

    @Override
    protected String getUrl() {
        return "/users/orders";
    }

    @Override
    protected HttpMethod getMethod() {
        return HttpMethod.PUT;
    }
}
