package com.vinyl.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vinyl.model.PurchaseProduct;

@Repository
public interface PurchaseProductRepository extends CrudRepository<PurchaseProduct, Integer> {

}
