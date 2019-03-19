package com.vinyl;

import com.vinyl.helper.TokenHeaderHelper;
import com.vinyl.model.Cart;
import com.vinyl.model.Product;
import com.vinyl.model.ProductCart;
import com.vinyl.model.User;
import com.vinyl.repository.CartRepository;
import com.vinyl.repository.ProductCartRepository;
import com.vinyl.repository.TokenRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

public class DeleteProductFromCartTest extends LoggedInBaseIntegration {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private TokenHeaderHelper tokenHeaderHelper;

    @Autowired
    private ProductCartRepository productCartRepository;

    @Autowired
    private TokenRepository tokenRepository;

    private Product product;

    private Cart cart;

    @Override
    public void setUp() {
        super.setUp();
        product = defaultEntitiesHelper.createProduct();
        cart = cartSetup();
    }

    @Override
    String getUrl() {
        return "/users/" + user.getId() + "/" + product.getId();
    }

    @Test
    public void testWhenUserIsLoggedInAndCartExists() {
        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(productCartRepository.findByProductAndCart(product, cart)).isNull();
    }

    @Test
    public void testWhenUserIsLoggedInAndCartDoesNotExist() {
        deleteCart();

        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testWhenUserIsLoggedInAndTriesToDeleteFromAnotherCart() {
        User otherUser = createUser("otheruser@email.com");
        Cart otherCart = defaultEntitiesHelper.createCart(otherUser);
        defaultEntitiesHelper.createProductCart(otherCart,product);
        HttpHeaders headers = tokenHeaderHelper.setupToken(token.getHash());

        ResponseEntity<?> response = trt.exchange("/users/" + otherUser.getId() + "/" + product.getId(), HttpMethod.POST, new HttpEntity<>(headers), Void.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        Assertions.assertThat(productCartRepository.findByProductAndCart(product,otherCart)).isNotNull();
    }

    @Test
    public void testWhenProductNotInCart() {
        deleteCart();
        defaultEntitiesHelper.createCart(user);
        HttpHeaders headers = tokenHeaderHelper.setupToken(token.getHash());

        Product otherProduct = createOtherProduct();
        ResponseEntity<?> response = trt.exchange("/users/" + user.getId() + "/" + otherProduct.getId(), HttpMethod.POST, new HttpEntity<>(headers), Void.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testWhenProductIsInvalid() {
        deleteCart();
        defaultEntitiesHelper.createCart(user);
        HttpHeaders headers = tokenHeaderHelper.setupToken(token.getHash());
        Integer invalidProductId=000;

        ResponseEntity<?> response = trt.exchange("/users/" + user.getId() + "/" + invalidProductId, HttpMethod.POST, new HttpEntity<>(headers), Void.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    private Product createOtherProduct() {
        Product product = new Product();
        product.setArtist("MJ");
        product.setCategory("Pop Music");
        product.setDescription("Best Seller");
        product.setPrice(80.5);
        product.setProductName("MJ Vinyl");
        product.setStock(14);

        return productRepository.save(product);
    }

    private Cart cartSetup() {
        Cart cart = defaultEntitiesHelper.createCart(user);
        ProductCart pc = defaultEntitiesHelper.createProductCart(cart, product);
        cart.setProducts(Lists.newArrayList(pc));

        return cartRepository.save(cart);
    }

    private void deleteCart() {
        productCartRepository.deleteAll();
        productRepository.deleteAll();
        cartRepository.deleteAll();
    }

    private ResponseEntity<?> setUpHeaderAndGetTheResponse() {
        HttpHeaders headers = tokenHeaderHelper.setupToken(token.getHash());
        ResponseEntity<?> response = trt.exchange("/users/" + user.getId() + "/" + product.getId(), HttpMethod.POST, new HttpEntity<>(headers), Void.class);

        return response;
    }
}