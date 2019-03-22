package com.vinyl.repository;

import com.vinyl.model.Cart;
import com.vinyl.model.Product;
import com.vinyl.model.ProductCart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCartRepository extends CrudRepository<ProductCart, Integer> {

    List<ProductCart> findByCart(Cart cart);

    ProductCart findByProductAndCart(Product product, Cart cart);

    ProductCart findByCartIdAndProductId(Integer id, Integer productId);
}
