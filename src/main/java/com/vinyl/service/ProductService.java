package com.vinyl.service;

import com.vinyl.model.*;
import com.vinyl.modelDTO.AddProductToCartDTO;
import com.vinyl.modelDTO.ProductManagementDTO;
import com.vinyl.repository.CartRepository;
import com.vinyl.repository.ProductCartRepository;
import com.vinyl.repository.ProductRepository;
import com.vinyl.repository.TokenRepository;
import com.vinyl.service.exception.BadRequestException;
import com.vinyl.service.exception.NotFoundException;
import com.vinyl.service.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("productService")
public class ProductService {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductCartRepository productCartRepository;

    @Autowired
    private ValidatorFactory validatorFactory;

    public void addProductToCart(AddProductToCartDTO addProductToCartDTO, Integer productId, String tokenHash) {

        validatorFactory.getTokenValidator().validate(tokenHash);

        Product product = productRepository.findById(productId).orElseThrow(() -> new BadRequestException("Product not found!"));

        Integer quantity = addProductToCartDTO.getQuantity();
        if (quantity == null || quantity <= 0 || (quantity > product.getStock())) {
            throw new BadRequestException("The quantity is not valid!");
        }

        Token token = tokenRepository.findByHash(tokenHash);
        Cart cart = findOrCreateCart(token.getUser());
        createOrUpdateProductCart(product, cart, addProductToCartDTO.getQuantity());
    }

    private Cart findOrCreateCart(User user) {
        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            cart = new Cart(user);
        }
        return cartRepository.save(cart);
    }

    private ProductCart createOrUpdateProductCart(Product product, Cart cart, Integer nrOfItems) {
        ProductCart productCart = productCartRepository.findByProductAndCart(product, cart);

        if (productCart == null) {
            productCart = new ProductCart(product, cart);
            productCart.setNrItems(nrOfItems);
        } else {
            productCart.setNrItems(nrOfItems + productCart.getNrItems());
        }
        productCart.setProductPrice(product.getPrice());

        return productCartRepository.save(productCart);
    }

    public void addProductToStore(ProductManagementDTO productToAdd, String tokenHash) {
        validateAdminToken(tokenHash);
        validatorFactory.getProductManagementValidator().validate(productToAdd);

        Product product = setAndGetProduct(productToAdd);
        productRepository.save(product);
    }

    public void updateProduct(ProductManagementDTO updatedProduct, String tokenHash, Integer productId) {
        validateAdminToken(tokenHash);
        validatorFactory.getProductManagementValidator().validate(updatedProduct);
        validateProductId(productId);

        Product newProduct = setAndGetProduct(updatedProduct);
        productRepository.delete(productRepository.findById(productId).get());
        productRepository.save(newProduct);
    }

    public void removeProductFromStore(String tokenHash, Integer productId) {
        validateAdminToken(tokenHash);
        validateProductId(productId);
        productRepository.delete(productRepository.findById(productId).get());
    }

    private void validateProductId(Integer productId) {
        productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product not found!"));
    }

    private Product setAndGetProduct(ProductManagementDTO requestProduct) {
        Product newProduct = new Product();
        newProduct.setStock(requestProduct.getStock());
        newProduct.setPrice(requestProduct.getPrice());
        newProduct.setProductName(requestProduct.getProductName());
        newProduct.setArtist(requestProduct.getArtist());
        newProduct.setCategory(requestProduct.getCategory());
        newProduct.setDescription(requestProduct.getDescription());

        return newProduct;
    }

    public void validateAdminToken(String tokenHash) {
        validatorFactory.getTokenValidator().validate(tokenHash);
        validatorFactory.getAdminValidator().validate(tokenRepository.findByHash(tokenHash));
    }
}