package com.vinyl;

import com.vinyl.helper.TokenHeaderHelper;
import com.vinyl.model.Cart;
import com.vinyl.model.Product;
import com.vinyl.model.ProductCart;
import com.vinyl.modelDTO.AddProductToCartDTO;
import com.vinyl.repository.CartRepository;
import com.vinyl.repository.ProductCartRepository;
import com.vinyl.repository.ProductRepository;
import com.vinyl.repository.TokenRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import java.time.LocalDate;

public class AddProductToCartTest extends BaseIntegration {

    @Autowired
    ProductRepository productRepository;
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

    @Test
    public void testWhenUserLoggedInAndItemExistsInCart() {
        Cart cart = defaultEntitiesHelper.createCart(user);
        ProductCart pc = defaultEntitiesHelper.createProductCart(cart, product);
        cart.setProducts(Lists.newArrayList(pc));
        cartRepository.save(cart);

        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(productCartRepository.findByProductAndCart(product, cart).getNrItems()).isEqualTo(pc.getNrItems() + request.getQuantity());
    }

    @Test
    public void testWhenUserLoggedInAndItemDoesNotExistInCart() {
        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Cart cart = cartRepository.findByUser(user);
        Assertions.assertThat(productCartRepository.findByProductAndCart(product, cart).getNrItems()).isEqualTo(request.getQuantity());
    }

    @Test
    public void testWhenTokenIsMissing() {
        ResponseEntity<?> response = trt.exchange("/products/" + product.getId() + "/cart", HttpMethod.POST, new HttpEntity<>(request), Void.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testWhenTokenIsInvalid() {
        HttpHeaders headers = tokenHeaderHelper.setupToken("INVALID_TOKEN");
        ResponseEntity<?> response = trt.exchange("/products/" + product.getId() + "/cart", HttpMethod.POST, new HttpEntity<>(request, headers), Void.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testWhenTokenIsExpired() {
        token.setValidUntil(LocalDate.now().minusMonths(3));
        tokenRepository.save(token);

        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testWhenQuantityIsTooLarge() {
        product.setStock(900);
        request.setQuantity(100000);

        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testWhenProductIsInvalid() {
        HttpHeaders headers = tokenHeaderHelper.setupToken(token.getHash());
        ResponseEntity<?> response = trt.exchange("/products/" + 000 + "/cart", HttpMethod.POST, new HttpEntity<>(request, headers), Void.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testWhenQuantityIsNull() {
        request.setQuantity(null);
        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testWhenQuantityIsZero() {
        request.setQuantity(0);
        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<?> setUpHeaderAndGetTheResponse() {
        HttpHeaders headers = tokenHeaderHelper.setupToken(token.getHash());
        ResponseEntity<?> response = trt.exchange("/products/" + product.getId() + "/cart", HttpMethod.POST, new HttpEntity<>(request, headers), Void.class);

        return response;
    }
}