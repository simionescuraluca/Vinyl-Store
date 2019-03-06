package com.vinyl.repository;

import com.vinyl.model.Cart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vinyl.model.ProductCart;

import java.util.List;

@Repository
public interface ProductCartRepository extends CrudRepository<ProductCart, Integer> {

    List<ProductCart> findByCart(Cart cart);
}
