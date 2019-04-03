package com.vinyl.service;

import com.vinyl.model.*;
import com.vinyl.modelDTO.AddProductToCartDTO;
import com.vinyl.modelDTO.InventoryDTO;
import com.vinyl.modelDTO.InventoryListDTO;
import com.vinyl.modelDTO.ProductManagementDTO;
import com.vinyl.repository.CartRepository;
import com.vinyl.repository.ProductCartRepository;
import com.vinyl.repository.ProductRepository;
import com.vinyl.repository.TokenRepository;
import com.vinyl.service.exception.BadRequestException;
import com.vinyl.service.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        validatorFactory.getAdminValidator().validate(tokenHash);
        validatorFactory.getProductManagementValidator().validate(productToAdd);
        Product newProduct = new Product();
        populateProduct(productToAdd, newProduct);
        productRepository.save(newProduct);
    }

    public void updateProduct(ProductManagementDTO updatedProduct, String tokenHash, Integer productId) {
        validatorFactory.getAdminValidator().validate(tokenHash);
        validatorFactory.getProductManagementValidator().validate(updatedProduct);
        validateProductId(productId);
        Product product = productRepository.findById(productId).get();
        populateProduct(updatedProduct, product);
        productRepository.save(product);
    }

    public void removeProductFromStore(String tokenHash, Integer productId) {
        validatorFactory.getAdminValidator().validate(tokenHash);
        validateProductId(productId);
        productRepository.delete(productRepository.findById(productId).get());
    }

    public InventoryListDTO getInventory(String tokenHash) {
        validatorFactory.getAdminValidator().validate(tokenHash);
        List<InventoryDTO> inventory = productRepository.findAll().stream().map(p -> new InventoryDTO(p.getId(), p.getProductName(), p.getStock())).collect(Collectors.toList());
        return new InventoryListDTO(inventory);
    }

    private void validateProductId(Integer productId) {
        productRepository.findById(productId).orElseThrow(() -> new BadRequestException("Product not found!"));
    }

    private void populateProduct(ProductManagementDTO requestProduct, Product product) {
        product.setStock(requestProduct.getStock());
        product.setPrice(requestProduct.getPrice());
        product.setProductName(requestProduct.getProductName());
        product.setArtist(requestProduct.getArtist());
        product.setCategory(requestProduct.getCategory());
        product.setDescription(requestProduct.getDescription());
    }
}