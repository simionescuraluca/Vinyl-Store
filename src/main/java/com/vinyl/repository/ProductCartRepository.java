package com.vinyl.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vinyl.model.ProductCart;

@Repository
public interface ProductCartRepository extends CrudRepository<ProductCart, Integer> {

}
