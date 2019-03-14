package com.vinyl;

import com.vinyl.helper.TokenHeaderHelper;
import com.vinyl.model.*;
import com.vinyl.repository.CartRepository;
import com.vinyl.repository.ProductCartRepository;
import com.vinyl.repository.TokenRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import java.time.LocalDate;

public class DeleteProductFromCartTest extends BaseIntegration {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private TokenHeaderHelper tokenHeaderHelper;

    @Autowired
    private ProductCartRepository productCartRepository;

    @Autowired
    private TokenRepository tokenRepository;

    private Product product;

    @Override
    public void setUp() {
        super.setUp();
        product = defaultEntitiesHelper.createProduct();
    }

    @Test
    public void testWhenUserIsLoggedInAndCartExists() {
        Cart cart =cartSetup();

        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(productCartRepository.findByProductAndCart(product,cart)).isNull();
    }

    @Test
    public void testWhenUserIsLoggedInAndCartDoesNotExist() {
        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testWhenUserIsLoggedInAndTriesToDeleteFromAnotherCart() {
        cartSetup();

        User otherUser = createUser("otheruser@email.com");
        HttpHeaders headers=tokenHeaderHelper.setupToken(token.getHash());
        ResponseEntity<?> response = trt.exchange("/users/" + otherUser.getId() + "/" + product.getId(), HttpMethod.POST,new HttpEntity<>(headers),Void.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testWhenProductToDeleteIsInvalid() {
        defaultEntitiesHelper.createCart(user);
        HttpHeaders headers=tokenHeaderHelper.setupToken(token.getHash());

        Product otherProduct = createOtherProduct();
        ResponseEntity<?> response = trt.exchange("/users/" + user.getId() + "/" + otherProduct.getId(), HttpMethod.POST,new HttpEntity<>(headers),Void.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testWhenTokenIsMissing(){
        ResponseEntity<?> response = trt.exchange("/users/" + user.getId() + "/" +product.getId(), HttpMethod.POST,HttpEntity.EMPTY,Void.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testWhenTokenIsInvalid(){
        HttpHeaders headers=tokenHeaderHelper.setupToken("INVALID_TOKEN");
        ResponseEntity<?> response = trt.exchange("/users/" + user.getId() +"/"+product.getId(), HttpMethod.POST,new HttpEntity<>(headers),Void.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testWhenTokenIsExpired(){
        token.setValidUntil(LocalDate.now().minusMonths(3));
        tokenRepository.save(token);

        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<?> setUpHeaderAndGetTheResponse(){
        HttpHeaders headers=tokenHeaderHelper.setupToken(token.getHash());
        ResponseEntity<?> response = trt.exchange("/users/" + user.getId() +"/"+product.getId(), HttpMethod.POST,new HttpEntity<>(headers),Void.class);

        return response;
    }

    private Product createOtherProduct(){
        Product product = new Product();
        product.setArtist("MJ");
        product.setCategory("Pop Music");
        product.setDescription("Best Seller");
        product.setPrice(80.5);
        product.setProductName("MJ Vinyl");
        product.setStock(14);

        return productRepository.save(product);
    }

    private Cart cartSetup(){
        Cart cart =defaultEntitiesHelper.createCart(user);
        ProductCart pc=defaultEntitiesHelper.createProductCart(cart, product);
        cart.setProducts(Lists.newArrayList(pc));

        return cartRepository.save(cart);
    }
}