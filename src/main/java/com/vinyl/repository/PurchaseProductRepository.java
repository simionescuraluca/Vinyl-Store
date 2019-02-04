package com.vinyl.repository;

import org.springframework.data.repository.CrudRepository;

import com.vinyl.model.PurchaseProduct;

public interface PurchaseProductRepository extends CrudRepository<PurchaseProduct, Integer> {

}
