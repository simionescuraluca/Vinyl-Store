package com.vinyl;

import com.vinyl.helper.TokenHeaderHelper;
import com.vinyl.model.Cart;
import com.vinyl.model.Product;
import com.vinyl.model.ProductCart;
import com.vinyl.model.Token;
import com.vinyl.modelDTO.AddProductToCartDTO;
import com.vinyl.repository.CartRepository;
import com.vinyl.repository.ProductCartRepository;
import com.vinyl.repository.TokenRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import java.time.LocalDate;

public class AddProductToCartTest extends BaseIntegration {

    @Autowired
    private TokenHeaderHelper tokenHeaderHelper;

    @Autowired
    private ProductCartRepository productCartRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private TokenRepository tokenRepository;

    private AddProductToCartDTO request;

    private Product product;

    @Override
    public void setUp() {
        super.setUp();
        product = defaultEntitiesHelper.createProduct();
        request = new AddProductToCartDTO();
        request.setQuantity(3);
    }

    public ResponseEntity<?> setUpHeaderAndGetTheResponse(Token token){
        HttpHeaders headers=tokenHeaderHelper.setupToken(token.getHash());
        ResponseEntity<?> response = trt.exchange("/products/" + product.getId()+ "/cart",HttpMethod.POST,new HttpEntity<>(request,headers),Void.class);

        return response;
    }

    @Test
    public void testWhenUserLoggedInAndItemExistsInCart(){
        Token token = defaultEntitiesHelper.createToken(user);
        Cart cart =defaultEntitiesHelper.createCart(user);
        ProductCart pc=defaultEntitiesHelper.createProductCart(cart, product);
        cart.setProducts(Lists.newArrayList(pc));

        ResponseEntity<?> response = setUpHeaderAndGetTheResponse(token);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(productCartRepository.findByProductAndCart(product,cart).getNrItems()).isEqualTo(pc.getNrItems() + request.getQuantity());
    }

    @Test
    public void testWhenUserLoggedInAndItemDoesNotExistInCart() {
        Token token = defaultEntitiesHelper.createToken(user);

        ResponseEntity<?> response = setUpHeaderAndGetTheResponse(token);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Cart cart = cartRepository.findByUser(user);
        Assertions.assertThat(productCartRepository.findByProductAndCart(product, cart).getNrItems()).isEqualTo(request.getQuantity());
    }

    @Test
    public void testWhenTokenIsMissing(){
        ResponseEntity<?> response = trt.exchange("/products/" + product.getId()+ "/cart",HttpMethod.POST,new HttpEntity<>(request),Void.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testWhenTokenIsInvalid() {
        HttpHeaders headers=tokenHeaderHelper.setupToken("INVALID_TOKEN");
        ResponseEntity<?> response = trt.exchange("/products/" + product.getId()+ "/cart",HttpMethod.POST,new HttpEntity<>(request,headers),Void.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testWhenTokenIsExpired() {
        Token token = defaultEntitiesHelper.createToken(user);
        token.setValidUntil(LocalDate.now().minusMonths(3));
        tokenRepository.save(token);

        ResponseEntity<?> response = setUpHeaderAndGetTheResponse(token);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testWhenQuantityIsTooLarge() {
        Token token = defaultEntitiesHelper.createToken(user);
        product.setStock(900);
        request.setQuantity(100000);

        ResponseEntity<?> response = setUpHeaderAndGetTheResponse(token);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testWhenProductIsInvalid() {
        Token token = defaultEntitiesHelper.createToken(user);
        product.setId(0000);

        ResponseEntity<?> response = setUpHeaderAndGetTheResponse(token);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testWhenQuantityIsNull(){
        Token token = defaultEntitiesHelper.createToken(user);
        request.setQuantity(null);

        ResponseEntity<?> response = setUpHeaderAndGetTheResponse(token);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}